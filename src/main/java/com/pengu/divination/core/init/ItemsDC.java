package com.pengu.divination.core.init;

import com.pengu.divination.api.items.ItemResearchable;
import com.pengu.divination.core.items.ItemNotes;

import net.minecraft.item.Item;

public class ItemsDC
{
	public static final Item MYSTERIUM_CLUSTER = new Item().setUnlocalizedName("mysterium_cluster");
	public static final Item MYSTERIUM_BOOK = new Item().setUnlocalizedName("mysterium_book");
	public static final Item MYSTERIUM_DUST = new ItemResearchable().setUnlocalizedName("mysterium_dust");
	public static final ItemNotes NOTES = new ItemNotes();
}