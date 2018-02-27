package com.pengu.divination.totemic.items;

import com.pengu.divination.core.items.ItemResearchable;
import com.pengu.divination.totemic.TotemicModule;
import com.pengu.divination.totemic.init.ItemsDT;
import com.pengu.divination.totemic.seals.core.TotemicSeal;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemSealNote extends ItemResearchable
{
	public ItemSealNote()
	{
		setUnlocalizedName("totemic/seal_note");
		setMaxStackSize(1);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if(isInCreativeTab(tab))
			TotemicSeal.seals.forEach(ts -> items.add(withSeal(ts)));
	}
	
	public static TotemicSeal getSealOnItem(ItemStack stack)
	{
		if(!stack.isEmpty() && stack.hasTagCompound())
		{
			int seal = stack.getTagCompound().getInteger("Seal");
			return seal < 0 ? null : TotemicSeal.seals.get(seal % TotemicSeal.seals.size());
		}
		return null;
	}
	
	public static ItemStack withSeal(TotemicSeal seal)
	{
		ItemStack stack = new ItemStack(ItemsDT.SEAL_NOTE);
		stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger("Seal", TotemicSeal.seals.indexOf(seal));
		return stack;
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