package com.pengu.divination.core.data;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

public class ClientResearchData
{
	public static final PlayerResearchData COMPLETED = new PlayerResearchData();
	
	public static void fromNBT(NBTTagCompound nbt)
	{
		COMPLETED.deserializeSpecialData(nbt);
	}
	
	public static boolean isResearchCompleted(Item res)
	{
		return isResearchCompleted(res.getRegistryName().toString());
	}
	
	public static boolean isResearchCompleted(String res)
	{
		return COMPLETED.isResearchCompleted(res);
	}
}