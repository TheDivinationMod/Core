package com.pengu.divination.core.init;

import com.pengu.divination.ShapelessRecipeBuilder;
import com.pengu.divination.api.RecipeHelper;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RecipesDC extends RecipeHelper
{
	@Override
	public void crafting()
	{
		recipe(ShapelessRecipeBuilder //
		        .builder() //
		        .output(new ItemStack(ItemsDC.MYSTERIUM_DUST)) //
		        .input() //
		        /**/.addItem(new ItemStack(Items.FLINT)) //
		        /**/.stay() //
		        /**/.build() //
		        .input() //
		        /**/.addItem(new ItemStack(Items.BOWL)) //
		        /**/.stay() //
		        /**/.build() //
		        .input() //
		        /**/.addItem(new ItemStack(ItemsDC.MYSTERIUM_CLUSTER)) //
		        /**/.build() //
		        .build());
	}
	
	@Override
	public void smelting()
	{
	}
}