package com.pengu.divination.proxy;

import com.pengu.divination.Divination;
import com.pengu.divination.core.net.PacketFXWisp;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.net.pkt.thunder.Thunder;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class EffectManager
{
	public void wisp(World world, Vec3d pos, Vec3d target, float size, Thunder.Layer type)
	{
		PacketFXWisp wisp = new PacketFXWisp();
		
		wisp.data = type;
		wisp.pos = pos;
		wisp.tpos = target;
		wisp.size = size;
		
		double x = pos.x + (target != null ? (target.x - pos.x) / 2 : 0);
		double y = pos.y + (target != null ? (target.y - pos.y) / 2 : 0);
		double z = pos.z + (target != null ? (target.z - pos.z) / 2 : 0);
		
		HCNetwork.manager.sendToAllAround(wisp, new TargetPoint(world.provider.getDimension(), x, y, z, 128));
	}
	
	public void wisp(World world, Vec3d pos, float size, Thunder.Layer type)
	{
		wisp(world, pos, null, size, type);
	}
	
	public void wisp(World world, double x, double y, double z, float size, Thunder.Layer type)
	{
		wisp(world, new Vec3d(x, y, z), size, type);
	}
	
	public void wisp(World world, double x, double y, double z, double tx, double ty, double tz, float size, Thunder.Layer type)
	{
		wisp(world, new Vec3d(x, y, z), new Vec3d(tx, ty, tz), size, type);
	}
	
	public static EffectManager fx()
	{
		return Divination.proxy.getFX();
	}
}