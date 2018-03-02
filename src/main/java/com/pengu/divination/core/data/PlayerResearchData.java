package com.pengu.divination.core.data;

import java.util.ArrayList;

import com.pengu.divination.api.events.UnlockDefaultResearchesEvent;
import com.pengu.divination.core.constants.InfoDC;
import com.pengu.divination.core.items.ItemNotes;
import com.pengu.divination.core.net.PacketStopMusic;
import com.pengu.divination.proxy.EffectManager;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.pkt.thunder.Thunder.Layer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.NBT;

public class PlayerResearchData
{
	public final ArrayList<String> researches = new ArrayList<>();
	public NBTTagCompound persisted = new NBTTagCompound();
	
	{
		unlockDefaults();
	}
	
	public void unlockDefaults()
	{
		MinecraftForge.EVENT_BUS.post(new UnlockDefaultResearchesEvent(this));
	}
	
	public void tick(EntityPlayerMP mp)
	{
		int s = researches.size();
		// researches.clear();
		if(s != researches.size())
			ResearchSystem.sync(mp);
		
		if(persisted.hasKey("NoteTime", NBT.TAG_INT))
		{
			int time = persisted.getInteger("NoteTime");
			
			if(time > 0)
			{
				persisted.setInteger("NoteTime", time - 1);
				--time;
				Vec3d vec = iPacket.Helper.getVec3d(persisted, "BookVec");
				
				double progress = Math.max(time / 200D, .1D);
				
				double x = mp.posX + (vec.x - mp.posX) * progress;
				double y = mp.posY + (vec.y - mp.posY) * progress;
				double z = mp.posZ + (vec.z - mp.posZ) * progress;
				
				if(time == 0)
				{
					persisted.removeTag("NoteTime");
					persisted.removeTag("BookVecX");
					persisted.removeTag("BookVecY");
					persisted.removeTag("BookVecZ");
					
					EntityItem ei = WorldUtil.spawnItemStack(mp.world, x, y, z, ItemNotes.withText("notes." + InfoDC.MOD_ID + ":mysterium_dust_craft"));
					ei.setNoGravity(true);
					persisted.setInteger("MagnetEntity", ei.getEntityId());
					HCNetwork.manager.sendTo(new PacketStopMusic(), mp);
				} else
					for(int i = -5; i <= 5; ++i)
						if(time - i > 0)
						{
							progress = Math.max((time - i) / 200D, .1D);
							
							x = mp.posX + (vec.x - mp.posX) * progress;
							y = mp.posY + (vec.y - mp.posY) * progress;
							z = mp.posZ + (vec.z - mp.posZ) * progress;
							
							int[] colors = new int[] { 0x00FF00, 0xCC00FF, 0x22FFFF };
							EffectManager.fx().wisp(mp.world, x, y, z, 2F + mp.getRNG().nextFloat() * 2F, new Layer(771, colors[mp.getRNG().nextInt(colors.length)], true));
						}
			}
		}
		
		magn: if(persisted.hasKey("MagnetEntity"))
		{
			int ent = persisted.getInteger("MagnetEntity");
			Entity me = mp.world.getEntityByID(ent);
			if(me == null)
			{
				persisted.removeTag("MagnetEntity");
				break magn;
			}
			me.motionX = (mp.posX - me.posX) / 32;
			me.motionY = (mp.posY - me.posY) / 32;
			me.motionZ = (mp.posZ - me.posZ) / 32;
		}
		
		if(mp.ticksExisted % 200 == 0)
			ResearchSystem.sync(mp);
	}
	
	public void spawnNotes(EntityPlayerMP mp)
	{
		persisted.setInteger("NoteTime", 100);
		
		double xf = (mp.world.rand.nextFloat() - mp.world.rand.nextFloat()) * 12;
		double zf = (mp.world.rand.nextFloat() - mp.world.rand.nextFloat()) * 12;
		double yf = 4 + mp.world.rand.nextFloat() * 12;
		
		iPacket.Helper.setVec3d(persisted, "BookVec", new Vec3d(mp.posX + xf, mp.posY + yf, mp.posZ + zf).addVector(0, mp.getEyeHeight(), 0));
	}
	
	public void completeResearch(Item res, boolean isCompleted)
	{
		completeResearch(res.getRegistryName().toString(), isCompleted);
	}
	
	public void completeResearch(String res, boolean isCompleted)
	{
		if(isCompleted && !isResearchCompleted(res))
			researches.add(res);
		else if(!isCompleted && isResearchCompleted(res))
			researches.remove(res);
	}
	
	public NBTTagCompound serializeSpecialData()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("Persisted", persisted);
		NBTTagList list = new NBTTagList();
		for(String r : researches)
			list.appendTag(new NBTTagString(r));
		nbt.setTag("Research", list);
		return nbt;
	}
	
	public PlayerResearchData deserializeSpecialData(NBTTagCompound nbt)
	{
		persisted = nbt.getCompoundTag("Persisted").copy();
		NBTTagList list = nbt.getTagList("Research", NBT.TAG_STRING);
		researches.clear();
		for(int i = 0; i < list.tagCount(); ++i)
			researches.add(list.getStringTagAt(i));
		unlockDefaults();
		return this;
	}
	
	public boolean isResearchCompleted(String res)
	{
		return researches.contains(res);
	}
	
	public boolean isResearchCompleted(Item res)
	{
		return researches.contains(res.getRegistryName().toString());
	}
	
	public boolean syncSafe(EntityPlayer player)
	{
		if(player == null || player.world == null || player.world.isRemote || !(player instanceof EntityPlayerMP))
			return false;
		HCNetwork.manager.sendTo(new PacketUpdateClientRD(this), (EntityPlayerMP) player);
		return true;
	}
}