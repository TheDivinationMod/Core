package com.pengu.divination.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.pengu.divination.InfoDC;
import com.pengu.hammercore.common.SimpleRegistration;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;

public abstract class RecipeHelper
{
	private static final Map<Class<? extends RecipeHelper>, RecipeHelper> instances = new HashMap<>();
	
	public abstract void crafting();
	
	public abstract void smelting();
	
	public static <T extends RecipeHelper> T getInstance(Class<T> t)
	{
		if(!instances.containsKey(t))
			try
			{
				instances.put(t, t.getConstructor().newInstance());
			} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
			{
				e.printStackTrace();
			}
		return (T) instances.get(t);
	}
	
	public static ItemStack enchantedBook(Enchantment ench, int lvl)
	{
		ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
		Map<Enchantment, Integer> enchs = new HashMap<>();
		enchs.put(ench, lvl);
		EnchantmentHelper.setEnchantments(enchs, book);
		return book;
	}
	
	protected List<IRecipe> recipes = new ArrayList<>();
	
	public Collection<IRecipe> collect()
	{
		recipes = new ArrayList<>();
		crafting();
		HashSet<IRecipe> recipes = new HashSet<>(this.recipes);
		this.recipes = null;
		return recipes;
	}
	
	protected void smelting(ItemStack in, ItemStack out)
	{
		smelting(in, out, 0);
	}
	
	protected void smelting(Item in, ItemStack out, float xp)
	{
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(in), out, xp);
	}
	
	protected void smelting(ItemStack in, ItemStack out, float xp)
	{
		FurnaceRecipes.instance().addSmeltingRecipe(in, out, xp);
	}
	
	protected void shaped(ItemStack out, Object... recipeComponents)
	{
		recipes.add(SimpleRegistration.parseShapedRecipe(out, recipeComponents).setRegistryName(InfoDC.MOD_ID, "recipes." + recipes.size()));
	}
	
	protected void shaped(Item out, Object... recipeComponents)
	{
		recipes.add(SimpleRegistration.parseShapedRecipe(new ItemStack(out), recipeComponents).setRegistryName(InfoDC.MOD_ID, "recipes." + recipes.size()));
	}
	
	protected void shaped(Block out, Object... recipeComponents)
	{
		recipes.add(SimpleRegistration.parseShapedRecipe(new ItemStack(out), recipeComponents).setRegistryName(InfoDC.MOD_ID, "recipes." + recipes.size()));
	}
	
	protected void shapeless(ItemStack out, Object... recipeComponents)
	{
		recipes.add(SimpleRegistration.parseShapelessRecipe(out, recipeComponents).setRegistryName(InfoDC.MOD_ID, "recipes." + recipes.size()));
	}
	
	protected void shapeless(Item out, Object... recipeComponents)
	{
		recipes.add(SimpleRegistration.parseShapelessRecipe(new ItemStack(out), recipeComponents).setRegistryName(InfoDC.MOD_ID, "recipes." + recipes.size()));
	}
	
	protected void shapeless(Block out, Object... recipeComponents)
	{
		recipes.add(SimpleRegistration.parseShapelessRecipe(new ItemStack(out), recipeComponents).setRegistryName(InfoDC.MOD_ID, "recipes." + recipes.size()));
	}
	
	protected void recipe(IRecipe recipe)
	{
		recipes.add(recipe);
	}
}