package com.pengu.divination.totemic.seals;

import java.util.Random;
import java.util.function.Consumer;

import com.pengu.divination.client.fx.FXWisp;
import com.pengu.divination.totemic.entity.npc.EntityShaman;
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
		
		if(t.getWorld().isRemote)
			randomDisplayTick(t);
		
		if(time >= maxTime)
		{
			// /blockdata -161 4 575 {Tags:{CustomSealData:{Ticks:3300}}}
			t.getLocation().setAir();
			EntityShaman sh = new EntityShaman(t.getWorld());
			sh.setPosition(t.getPos().getX() + .5, t.getPos().getY() + .25, t.getPos().getZ() + .5);
			if(!t.getWorld().isRemote)
				t.getWorld().spawnEntity(sh);
		}
		
		t.customSealData.setInteger("Ticks", time + 1);
	}
	
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(TileTotemicSeal t)
	{
		int time = t.customSealData.getInteger("Ticks");
		int maxTime = t.customSealData.getInteger("MaxTime");
		
		if(time < 100)
			return;
		
		float progress = time / (float) maxTime;
		int ni = (int) Math.ceil((1 - progress) * 20);
		
		Random rng = t.getRNG();
		
		if(ni > 0 && rng.nextInt(ni) == 0)
			new FXWisp(t.getWorld(), t.getPos().getX() + .2F + rng.nextFloat() * .6F, t.getPos().getY() + rng.nextFloat() * 2F, t.getPos().getZ() + .2F + rng.nextFloat() * .6F, .5F + rng.nextFloat(), new Layer(1, 0x11FF11, true)).spawn();
	}
}