package com.pengu.divination.totemic.items;

import com.pengu.divination.totemic.TotemicModule;
import com.pengu.divination.totemic.seals.core.TotemicSeal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemSealNote extends Item
{
	public ItemSealNote()
	{
		setUnlocalizedName("totemic/seal_note");
		setMaxStackSize(1);
	}
	
	public static TotemicSeal getSealOnItem(ItemStack stack)
	{
		if(!stack.isEmpty() && stack.hasTagCompound())
		{
			int seal = stack.getTagCompound().getInteger("Seal");
			return TotemicSeal.seals.get(seal % TotemicSeal.seals.size());
		}
		return TotemicSeal.seals.get(0);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if(worldIn.isRemote)
		{
			TotemicSeal s = getSealOnItem(playerIn.getHeldItem(handIn));
			if(s != null)
				TotemicModule.proxy.openSealNote(s);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}