package com.pengu.divination.totemic.seals;

import java.util.Random;
import java.util.function.Consumer;

import com.pengu.divination.client.fx.FXWisp;
import com.pengu.divination.totemic.tiles.TileTotemicSeal;
import com.pengu.hammercore.net.pkt.thunder.Thunder.Layer;

import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TSealStarting implements Consumer<TileTotemicSeal>
{
	@Override
	public void accept(TileTotemicSeal t)
	{
		Random rng = t.getRNG();
		
		int time = t.customSealData.getInteger("Ticks");
		if(!t.customSealData.hasKey("MaxTime", NBT.TAG_INT))
			t.customSealData.setInteger("MaxTime", 3600 + rng.nextInt(600) - rng.nextInt(600));
		int maxTime = t.customSealData.getInteger("MaxTime");
		
		if(t.getWorld().isRemote && rng.nextInt(1) == 0)
			randomDisplayTick(t);
		
		if(time >= maxTime)
		{
			// /blockdata -161 4 575 {Tags:{CustomSealData:{Ticks:3300}}}
			t.getLocation().setAir();
		}
		
		t.customSealData.setInteger("Ticks", time + 1);
	}
	
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(TileTotemicSeal t)
	{
		Random rng = t.getRNG();
		new FXWisp(t.getWorld(), t.getPos().getX() + rng.nextFloat(), t.getPos().getY() + rng.nextFloat() * .1F, t.getPos().getZ() + rng.nextFloat(), .5F + rng.nextFloat(), new Layer(1, 0x11FF11, true)).spawn();
	}
}