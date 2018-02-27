package com.pengu.divination.proxy;

import com.pengu.divination.core.client.fx.FXWisp;
import com.pengu.hammercore.net.pkt.thunder.Thunder.Layer;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ClientEffectManager extends EffectManager
{
	@Override
	public void wisp(World world, Vec3d pos, Vec3d target, float size, Layer type)
	{
		if(world.isRemote)
		{
			if(target != null)
				new FXWisp(world, pos.x, pos.y, pos.z, target.x, target.y, target.z, size, type).spawn();
			else
				new FXWisp(world, pos.x, pos.y, pos.z, size, type).spawn();
		}
		else
			super.wisp(world, pos, target, size, type);
	}
}