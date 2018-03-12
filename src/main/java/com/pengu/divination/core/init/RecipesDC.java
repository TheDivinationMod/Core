package com.pengu.divination.core.init;

import com.pengu.divination.core.constants.InfoDC;
import com.pengu.hammercore.common.SimpleRegistration;
import com.pengu.hammercore.recipeAPI.helper.RecipeRegistry;
import com.pengu.hammercore.recipeAPI.helper.RegisterRecipes;
import com.pengu.hammercore.recipeAPI.helper.ShapelessRecipeBuilder;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@RegisterRecipes(modid = InfoDC.MOD_ID)
public class RecipesDC extends RecipeRegistry
{
	@Override
	public void crafting()
	{
		shaped(new ItemStack(BlocksDC.ICE_BRICKS, 4), "is", "si", 'i', Blocks.ICE, 's', Blocks.SNOW);
		shaped(new ItemStack(BlocksDC.ICE_BRICKS, 4), "si", "is", 'i', Blocks.ICE, 's', Blocks.SNOW);
		
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