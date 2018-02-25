package com.pengu.divination.totemic.seals;

import com.pengu.hammercore.tile.TileSyncableTickable;

import net.minecraft.nbt.NBTTagCompound;

public class TileTotemicSeal extends TileSyncableTickable
{
	public NBTTagCompound customSealData = new NBTTagCompound();
	
	@Override
	public void tick()
	{
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		customSealData = nbt.getCompoundTag("CustomSealData");
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		nbt.setTag("CustomSealData", customSealData);
	}
}