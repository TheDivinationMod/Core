package com.pengu.divination.core.items;

import com.pengu.divination.Divination;
import com.pengu.divination.api.items.ItemResearchable;
import com.pengu.divination.core.init.ItemsDC;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemNotes extends ItemResearchable
{
	public ItemNotes()
	{
		setUnlocalizedName("notes");
		setMaxStackSize(1);
	}
	
	public static String getTextOnItem(ItemStack stack)
	{
		if(!stack.isEmpty() && stack.hasTagCompound())
			return stack.getTagCompound().getString("Text");
		return "";
	}
	
	public static ItemStack withText(String text)
	{
		ItemStack stack = new ItemStack(ItemsDC.NOTES);
		stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setString("Text", text);
		return stack;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if(worldIn.isRemote)
		{
			String s = getTextOnItem(playerIn.getHeldItem(handIn));
			if(s != null)
				Divination.proxy.openNotes(s);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}