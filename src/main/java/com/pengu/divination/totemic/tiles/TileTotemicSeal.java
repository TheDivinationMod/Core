package com.pengu.divination.totemic.tiles;

import java.util.ArrayList;
import java.util.List;

import com.pengu.divination.totemic.seals.core.EnumSealColor;
import com.pengu.divination.totemic.seals.core.TSealPoint;
import com.pengu.divination.totemic.seals.core.TotemicSeal;
import com.pengu.hammercore.tile.TileSyncableTickable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants.NBT;

public class TileTotemicSeal extends TileSyncableTickable
{
	public List<TSealPoint> points = new ArrayList<>();
	public TotemicSeal cseal;
	public NBTTagCompound customSealData = new NBTTagCompound();
	
	@Override
	public void tick()
	{
		/** May be tedious task, execute once per 2 seconds */
		if(atTickRate(40))
			defineSeal(true);
		
		if(!world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP))
		{
			getLocation().setAir();
			return;
		}
		
		if(cseal != null && cseal.tick != null)
			cseal.tick.accept(this);
		
		if(!world.isRemote && points.isEmpty())
			getLocation().setAir();
	}
	
	public void defineSeal(boolean resetNBT)
	{
		TotemicSeal prev = cseal;
		cseal = null;
		for(TotemicSeal s : TotemicSeal.seals)
			if(s.equalsIgnoreRotation(points))
			{
				if(prev != s && resetNBT)
					customSealData = new NBTTagCompound();
				cseal = s;
				break;
			}
	}
	
	public boolean paint(int x, int y, EnumSealColor color)
	{
		x %= 16;
		y %= 16;
		
		for(TSealPoint p : points)
			if(p.x == x && p.y == y)
				return false;
			
		points.add(new TSealPoint((byte) x, (byte) y, color));
		sync();
		
		defineSeal(true);
		
		return true;
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		customSealData = nbt.getCompoundTag("CustomSealData");
		
		points.clear();
		NBTTagList ps = nbt.getTagList("Points", NBT.TAG_COMPOUND);
		for(int i = 0; i < ps.tagCount(); ++i)
		{
			NBTTagCompound n = ps.getCompoundTagAt(i);
			points.add(new TSealPoint(n.getByte("x"), n.getByte("y"), EnumSealColor.values()[n.getByte("c")]));
		}
		
		defineSeal(false);
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		nbt.setTag("CustomSealData", customSealData);
		
		NBTTagList ps = new NBTTagList();
		for(TSealPoint p : points)
		{
			NBTTagCompound n = new NBTTagCompound();
			n.setByte("x", p.x);
			n.setByte("y", p.y);
			n.setByte("c", (byte) p.color.ordinal());
			ps.appendTag(n);
		}
		nbt.setTag("Points", ps);
	}
}