package com.pengu.divination.core.items;

import com.pengu.divination.core.data.ClientResearchData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemResearchable extends Item
{
	@SideOnly(Side.CLIENT)
	@Override
	public FontRenderer getFontRenderer(ItemStack stack)
	{
		return ClientResearchData.isResearchCompleted(this) ? null : Minecraft.getMinecraft().standardGalacticFontRenderer;
	}
}