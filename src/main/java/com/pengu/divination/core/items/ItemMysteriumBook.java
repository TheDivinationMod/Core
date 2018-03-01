package com.pengu.divination.core.items;

import java.util.Random;

import com.pengu.divination.api.items.ItemResearchable;
import com.pengu.divination.core.data.ResearchSystem;
import com.pengu.divination.proxy.EffectManager;
import com.pengu.hammercore.net.pkt.thunder.Thunder;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemMysteriumBook extends ItemResearchable
{
	public ItemMysteriumBook()
	{
		setUnlocalizedName("mysterium_book");
		setMaxStackSize(1);
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		World world = entityItem.world;
		Random rng = world.rand;
		if(!world.isRemote && rng.nextInt(20) == 0)
		{
			Vec3d pos = entityItem.getPositionVector().addVector(0, entityItem.getEyeHeight() * 2, 0);
			Vec3d target = pos.addVector(rng.nextFloat() - rng.nextFloat(), rng.nextFloat(), rng.nextFloat() - rng.nextFloat());
			EffectManager.fx().wisp(world, pos, target, 2F, new Thunder.Layer(world.rand.nextBoolean() ? 771 : 1, world.rand.nextInt(9) == 0 ? 0xCC00FF : 0x2222FF, true));
		}
		return false;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if(worldIn.isRemote)
		{
			
		} else
			ResearchSystem.sync(playerIn);
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}