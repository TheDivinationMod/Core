package com.pengu.divination.core.data;

import java.util.ArrayList;

import com.pengu.divination.InfoDC;
import com.pengu.divination.core.items.ItemNotes;
import com.pengu.divination.core.net.PacketStopMusic;
import com.pengu.divination.proxy.EffectManager;
import com.pengu.divination.totemic.init.ItemsDT;
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
import net.minecraftforge.common.util.Constants.NBT;

public class PlayerResearchData
{
	public final ArrayList<String> researches = new ArrayList<>();
	public boolean gotNote = false;
	public NBTTagCompound persisted = new NBTTagCompound();
	
	public void tick(EntityPlayerMP mp)
	{
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
					persisted.removeTag("BookTime");
					persisted.removeTag("BookVecX");
					persisted.removeTag("BookVecY");
					persisted.removeTag("BookVecZ");
					
					EntityItem ei = WorldUtil.spawnItemStack(mp.world, x, y, z, ItemNotes.withText("notes." + InfoDC.MOD_ID + ":mysterium_dust_craft"));
					ei.setNoGravity(true);
					persisted.setInteger("MagnetEntity", ei.getEntityId());
					HCNetwork.manager.sendTo(new PacketStopMusic(), mp);
				} else
				{
					EffectManager.fx().wisp(mp.world, x, y, z, 2F, new Layer(771, 0x00FF00, true));
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
		double yf = mp.world.rand.nextFloat() * 12;
		
		iPacket.Helper.setVec3d(persisted, "BookVec", new Vec3d(mp.posX + xf, mp.posY + yf, mp.posZ + zf));
		
		gotNote = true;
	}
	
	public NBTTagCompound serializeSpecialData()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("Persisted", persisted);
		NBTTagList list = new NBTTagList();
		for(String r : researches)
			list.appendTag(new NBTTagString(r));
		nbt.setTag("Research", list);
		nbt.setBoolean("GotNote", gotNote);
		return nbt;
	}
	
	public PlayerResearchData deserializeSpecialData(NBTTagCompound nbt)
	{
		persisted = nbt.getCompoundTag("Persisted").copy();
		NBTTagList list = nbt.getTagList("Research", NBT.TAG_STRING);
		researches.clear();
		for(int i = 0; i < list.tagCount(); ++i)
			researches.add(list.getStringTagAt(i));
		gotNote = nbt.getBoolean("GotNote");
		return this;
	}
	
	public boolean isResearchCompleted(String res)
	{
		// if(res.equals("totemic.seal.guidance"))
		// return true;
		if(res.equals(ItemsDT.SEAL_NOTE.getRegistryName().toString()))
			return true;
		
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