package com.pengu.divination.core.proc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.endie.lib.tuple.TwoTuple;
import com.pengu.divination.core.items.ItemMysteriumDust;
import com.pengu.divination.proxy.EffectManager;
import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.api.iProcess;
import com.pengu.hammercore.net.pkt.thunder.Thunder.Layer;
import com.pengu.hammercore.utils.WorldLocation;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProcessTransformBlock implements iProcess
{
	public static Map<Integer, Set<Long>> WORLD_ACTIVE = new HashMap<>();
	
	public WorldLocation loc;
	public int ticks;
	
	@Override
	public void update()
	{
		TwoTuple<Function<IBlockState, IBlockState>, String> out;
		if((out = ItemMysteriumDust.WORLD_TRANSMUTATION.get(loc.getBlock())) != null)
		{
			Function<IBlockState, IBlockState> stateMap = out.get1();
			
			World world = loc.getWorld();
			
			AxisAlignedBB aabb = loc.getState().getBoundingBox(world, loc.getPos());
			Vec3d center = aabb != null ? aabb.getCenter() : new Vec3d(.5, .5, .5);
			Vec3d pos = new Vec3d(loc.getPos()).add(center);
			if(ticks >= 300)
			{
				int dim = loc.getWorld().provider.getDimension();
				Set<Long> active = WORLD_ACTIVE.get(dim);
				if(active == null)
					WORLD_ACTIVE.put(dim, active = new HashSet<>());
				
				active.remove(loc.getPos().toLong());
				
				loc.setState(stateMap.apply(loc.getState()));
			} else if(ticks >= 20)
				for(int j = 0; j < 2; ++j)
				{
					Vec3d rng = new Vec3d((world.rand.nextFloat() - world.rand.nextFloat()) * 2F, (world.rand.nextFloat() - world.rand.nextFloat()) * 2F, (world.rand.nextFloat() - world.rand.nextFloat()) * 2F);
					EffectManager.fx().wisp(world, pos, pos.add(rng), 3F + world.rand.nextFloat() * 4F, new Layer(world.rand.nextBoolean() ? 771 : 1, world.rand.nextInt(9) == 0 ? 0xCC00FF : 0x2222FF, true));
				}
			
			if(ticks == 280)
				for(int i = 0; i < 100; ++i)
				{
					Vec3d rng = new Vec3d((world.rand.nextFloat() - world.rand.nextFloat()) * 2F, (world.rand.nextFloat() - world.rand.nextFloat()) * 2F, (world.rand.nextFloat() - world.rand.nextFloat()) * 2F);
					Vec3d rng2 = new Vec3d((world.rand.nextFloat() - world.rand.nextFloat()) * .5F, (world.rand.nextFloat() - world.rand.nextFloat()) * .5F, (world.rand.nextFloat() - world.rand.nextFloat()) * .5F);
					EffectManager.fx().wisp(world, pos.add(rng2), pos.add(rng), 7F + world.rand.nextFloat() * 6F, new Layer(world.rand.nextBoolean() ? 771 : 1, world.rand.nextInt(9) == 0 ? 0xCC00FF : 0x2222FF, true));
				}
			else if(ticks >= 220 && ticks % 2 == 0)
			{
				Vec3d rng = new Vec3d((world.rand.nextFloat() - world.rand.nextFloat()) * 2F, (world.rand.nextFloat() - world.rand.nextFloat()) * 2F, (world.rand.nextFloat() - world.rand.nextFloat()) * 2F);
				Vec3d rng2 = new Vec3d((world.rand.nextFloat() - world.rand.nextFloat()) * .5F, (world.rand.nextFloat() - world.rand.nextFloat()) * .5F, (world.rand.nextFloat() - world.rand.nextFloat()) * .5F);
				HammerCore.particleProxy.spawnSimpleThunder(world, pos.add(rng2), pos.add(rng), world.rand.nextLong(), 1, 2F, new Layer(771, world.rand.nextInt(9) == 0 ? 0xCC00FF : 0x2222FF, true), new Layer(1, world.rand.nextInt(9) == 0 ? 0xCC00FF : 0x2222FF, true));
			}
		}
		
		++ticks;
	}
	
	@Override
	public boolean isAlive()
	{
		return ItemMysteriumDust.WORLD_TRANSMUTATION.get(loc.getBlock()) != null && ticks <= 300;
	}
}