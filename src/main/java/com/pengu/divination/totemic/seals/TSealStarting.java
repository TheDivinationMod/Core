package com.pengu.divination.totemic.seals;

import java.util.Random;
import java.util.function.Consumer;

import com.pengu.divination.Divination;
import com.pengu.divination.proxy.EffectManager;
import com.pengu.divination.totemic.entity.npc.EntityShaman;
import com.pengu.divination.totemic.tiles.TileTotemicSeal;
import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.net.pkt.thunder.Thunder;
import com.pengu.hammercore.net.pkt.thunder.Thunder.Layer;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.Constants.NBT;

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
		
		if(!t.getWorld().isRemote)
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
		{
			for(int j = 0; j < 2; ++j)
			{
				int i8 = 15;
				
				for(int i = 0; i < i8; ++i)
				{
					float f8 = 90F / i8;
					
					float sin = MathHelper.sin(time + i * f8 + j * 90);
					float cos = MathHelper.cos(time + i * f8 + j * 90);
					
					float x = sin * .5F;
					float z = cos * .5F;
					
					float px = t.getPos().getX() + .5F + x, py = t.getPos().getY() + i * (2F / i8) + .1F, pz = t.getPos().getZ() + .5F + z;
					float tx = px + rng.nextFloat() - rng.nextFloat(), ty = py + rng.nextFloat() - rng.nextFloat(), tz = pz + rng.nextFloat() - rng.nextFloat();
					
					if(time >= maxTime - 1000 && rng.nextInt(i8 * 2) < 2)
						HammerCore.particleProxy.spawnSimpleThunder(t.getWorld(), new Vec3d(px, py, pz), new Vec3d(tx, ty, tz), rng.nextLong(), 3, 1F, new Thunder.Layer(771, 0x22FF22, true), new Thunder.Layer(772, 0x22FF22, true));
					
					if(time >= maxTime - 40)
						EffectManager.fx().wisp(t.getWorld(), px, py, pz, tx, ty, tz, 1F + rng.nextFloat(), new Layer(1, 0x11FF11, true));
					else
						EffectManager.fx().wisp(t.getWorld(), px, py, pz, 1F + rng.nextFloat(), new Layer(1, 0x11FF11, true));
				}
			}
		}
		
		int im = Math.round(5 * (time / (float) maxTime));
		
		float step = 45F / im;
		
		for(int j = 0; j < im; ++j)
		{
			float sin = MathHelper.sin(time / 20F + j * step);
			float cos = MathHelper.cos(time / 20F + j * step);
			
			float x = sin * .4F;
			float z = cos * .4F;
			
			float px = t.getPos().getX() + .5F + x, py = t.getPos().getY(), pz = t.getPos().getZ() + .5F + z;
			float tx = px + (rng.nextFloat() - rng.nextFloat()) * .8F, ty = py + 2F, tz = pz + (rng.nextFloat() - rng.nextFloat()) * .8F;
			
			EffectManager.fx().wisp(t.getWorld(), px, py, pz, tx, ty, tz, .25F + rng.nextFloat() * .25F, new Layer(1, 0x44CC44, true));
		}
		
		float l = 600;
		if(time >= maxTime - l)
		{
			int tl = (int) l - maxTime + time;
			float rad = tl / l * .2F;
			im = (int) ((1 - rad / .2F) * 20) + 4;
			step = 1F / im;
			
			for(int j = 0; j < im; ++j)
			{
				float s = (2F + rng.nextFloat() * 3F) * rad / .2F;
				float sin = MathHelper.sin(j * step);
				float cos = MathHelper.cos(j * step);
				
				float x = sin * rad;
				float z = cos * rad;
				
				float px = t.getPos().getX() + .5F + x, py = t.getPos().getY(), pz = t.getPos().getZ() + .5F + z;
				
				EffectManager.fx().wisp(t.getWorld(), px, py + (j / (float) im) * 2F, pz, s * 2F, new Layer(1, 0x44FF44, true));
			}
		}
	}
}