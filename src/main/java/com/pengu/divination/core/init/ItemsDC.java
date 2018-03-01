package com.pengu.divination.core.init;

import com.pengu.divination.core.items.ItemMysteriumBook;
import com.pengu.divination.core.items.ItemMysteriumDust;
import com.pengu.divination.core.items.ItemNotes;

import net.minecraft.item.Item;

public class ItemsDC
{
	public static final Item MYSTERIUM_CLUSTER = new Item().setUnlocalizedName("mysterium_cluster");
	public static final Item MYSTERIUM_BOOK = new ItemMysteriumBook();
	public static final Item MYSTERIUM_DUST = new ItemMysteriumDust();
	public static final ItemNotes NOTES = new ItemNotes();
}