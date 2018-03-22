package com.pengu.divination.core.proc;

import com.pengu.divination.core.init.ItemsDC;
import com.pengu.divination.core.items.ItemMysteriumDust;
import com.pengu.divination.proxy.EffectManager;
import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.api.iProcess;
import com.pengu.hammercore.net.pkt.thunder.Thunder.Layer;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProcessTransformItem implements iProcess
{
	public World world;
	public int procEntity, ticks;
	
	@Override
	public void update()
	{
		EntityItem ent = (EntityItem) world.getEntityByID(procEntity);
		
		if(ent != null)
		{
			ItemStack stack = ent.getItem();
			
			if(!stack.isEmpty() && ItemMysteriumDust.ITEM_TRANSMUTATION.get(stack.getItem()) != null)
			{
				ent.setInfinitePickupDelay();
				if(ticks >= 200)
				{
					ent.setNoPickupDelay();
					ent.setItem(new ItemStack(ItemMysteriumDust.ITEM_TRANSMUTATION.get(stack.getItem()).get1()));
				} else if(ticks == 180)
					for(int i = 0; i < 32; ++i)
					{
						Vec3d rng = new Vec3d((world.rand.nextFloat() - world.rand.nextFloat()) * 1F, (world.rand.nextFloat() - world.rand.nextFloat()) * 1F, (world.rand.nextFloat() - world.rand.nextFloat()) * 1F);
						Vec3d rng2 = new Vec3d((world.rand.nextFloat() - world.rand.nextFloat()) * .5F, (world.rand.nextFloat() - world.rand.nextFloat()) * .5F, (world.rand.nextFloat() - world.rand.nextFloat()) * .5F);
						EffectManager.fx().wisp(world, ent.getPositionVector().addVector(0, ent.getEyeHeight() * 2, 0).add(rng2), ent.getPositionVector().addVector(0, ent.getEyeHeight() * 2, 0).add(rng), 4F + world.rand.nextFloat() * 3F, new Layer(world.rand.nextBoolean() ? 771 : 1, world.rand.nextInt(9) == 0 ? 0xCC00FF : 0x2222FF, true));
					}
				else if(ticks >= 20)
				{
					Vec3d rng = new Vec3d((world.rand.nextFloat() - world.rand.nextFloat()) * 1F, (world.rand.nextFloat() - world.rand.nextFloat()) * 1F, (world.rand.nextFloat() - world.rand.nextFloat()) * 1F);
					EffectManager.fx().wisp(world, ent.getPositionVector().addVector(0, ent.getEyeHeight() * 2, 0), ent.getPositionVector().addVector(0, ent.getEyeHeight() * 2, 0).add(rng), 3F + world.rand.nextFloat(), new Layer(world.rand.nextBoolean() ? 771 : 1, world.rand.nextInt(9) == 0 ? 0xCC00FF : 0x2222FF, true));
				}
				if(ticks >= 120 && ticks % 2 == 0)
				{
					Vec3d pos = ent.getPositionVector().addVector(.25, .25, .25);
					Vec3d rng = new Vec3d((world.rand.nextFloat() - world.rand.nextFloat()) * 2F, (world.rand.nextFloat() - world.rand.nextFloat()) * 2F, (world.rand.nextFloat() - world.rand.nextFloat()) * 2F);
					Vec3d rng2 = new Vec3d((world.rand.nextFloat() - world.rand.nextFloat()) * .5F, (world.rand.nextFloat() - world.rand.nextFloat()) * .5F, (world.rand.nextFloat() - world.rand.nextFloat()) * .5F);
					HammerCore.particleProxy.spawnSimpleThunder(world, pos.add(rng2), pos.add(rng), world.rand.nextLong(), 1, 2F, new Layer(771, world.rand.nextInt(9) == 0 ? 0xCC00FF : 0x2222FF, true), new Layer(1, world.rand.nextInt(9) == 0 ? 0xCC00FF : 0x2222FF, true));
				}
			}
		}
		
		++ticks;
	}
	
	@Override
	public boolean isAlive()
	{
		if(!(world.getEntityByID(procEntity) instanceof EntityItem))
			return false;
		
		EntityItem ent = (EntityItem) world.getEntityByID(procEntity);
		
		if(ent != null)
		{
			ItemStack stack = ent.getItem();
			
			if(!stack.isEmpty() && ItemMysteriumDust.ITEM_TRANSMUTATION.get(stack.getItem()) != null)
				return ticks <= 200;
		}
		
		return false;
	}
}