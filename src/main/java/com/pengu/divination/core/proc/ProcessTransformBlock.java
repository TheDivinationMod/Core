package com.pengu.divination.core.proc;

import java.util.function.Function;

import com.endie.lib.tuple.TwoTuple;
import com.pengu.divination.core.items.ItemMysteriumDust;
import com.pengu.divination.proxy.EffectManager;
import com.pengu.hammercore.api.iProcess;
import com.pengu.hammercore.net.pkt.thunder.Thunder.Layer;
import com.pengu.hammercore.utils.WorldLocation;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProcessTransformBlock implements iProcess
{
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
			
			Vec3d pos = new Vec3d(loc.getPos()).addVector(.5, .5, .5);
			if(ticks >= 200)
				loc.setState(stateMap.apply(loc.getState()));
			else if(ticks >= 20)
				for(int j = 0; j < 2; ++j)
				{
					Vec3d rng = new Vec3d((world.rand.nextFloat() - world.rand.nextFloat()) * 1.75F, (world.rand.nextFloat() - world.rand.nextFloat()) * 1.75F, (world.rand.nextFloat() - world.rand.nextFloat()) * 1.75F);
					EffectManager.fx().wisp(world, pos, pos.add(rng), 3F + world.rand.nextFloat() * 4F, new Layer(world.rand.nextBoolean() ? 771 : 1, world.rand.nextInt(9) == 0 ? 0xCC00FF : 0x2222FF, true));
				}
			
			if(ticks == 180)
				for(int i = 0; i < 48; ++i)
				{
					Vec3d rng = new Vec3d((world.rand.nextFloat() - world.rand.nextFloat()) * 1.75F, (world.rand.nextFloat() - world.rand.nextFloat()) * 1.75F, (world.rand.nextFloat() - world.rand.nextFloat()) * 1.75F);
					Vec3d rng2 = new Vec3d((world.rand.nextFloat() - world.rand.nextFloat()) * .5F, (world.rand.nextFloat() - world.rand.nextFloat()) * .5F, (world.rand.nextFloat() - world.rand.nextFloat()) * .5F);
					EffectManager.fx().wisp(world, pos.add(rng2), pos.add(rng), 7F + world.rand.nextFloat() * 6F, new Layer(world.rand.nextBoolean() ? 771 : 1, world.rand.nextInt(9) == 0 ? 0xCC00FF : 0x2222FF, true));
				}
		}
		
		++ticks;
	}
	
	@Override
	public boolean isAlive()
	{
		return ItemMysteriumDust.WORLD_TRANSMUTATION.get(loc.getBlock()) != null && ticks <= 200;
	}
}