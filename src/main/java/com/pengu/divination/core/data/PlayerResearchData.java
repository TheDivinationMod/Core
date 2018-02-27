package com.pengu.divination.core.data;

import java.util.ArrayList;

import com.pengu.divination.totemic.init.ItemsDT;
import com.pengu.hammercore.net.HCNetwork;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants.NBT;

public class PlayerResearchData
{
	public final ArrayList<String> researches = new ArrayList<>();
	public NBTTagCompound persisted = new NBTTagCompound();
	
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
		return this;
	}
	
	public boolean isResearchCompleted(String res)
	{
//		if(res.equals("totemic.seal.guidance"))
//			return true;
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