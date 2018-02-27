package com.pengu.divination;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class ShapelessRecipeBuilder
{
	private NonNullList<ItemStack[]> inputs = NonNullList.create();
	private NonNullList<Integer> stay = NonNullList.create();
	private ItemStack output = ItemStack.EMPTY;
	
	public static ShapelessRecipeBuilder builder()
	{
		return new ShapelessRecipeBuilder();
	}
	
	public ShapelessRecipeBuilder output(ItemStack stack)
	{
		this.output = stack.copy();
		return this;
	}
	
	public BuildableIngredient input()
	{
		return new BuildableIngredient(this);
	}
	
	public IRecipe build()
	{
		NonNullList<Ingredient> ings = NonNullList.withSize(inputs.size(), Ingredient.EMPTY);
		NonNullList<Ingredient> stay = NonNullList.withSize(inputs.size(), Ingredient.EMPTY);
		
		for(int i = 0; i < ings.size(); ++i)
		{
			Ingredient ing = Ingredient.fromStacks(inputs.get(i));
			ings.set(i, ing);
			if(this.stay.contains(i))
				stay.set(i, ing);
		}
		
		return new BakedShapelessRecipe(this, output, ings, stay);
	}
	
	public static class BuildableIngredient
	{
		final ShapelessRecipeBuilder builder;
		Collection<ItemStack> inputs = new ArrayList<>();
		boolean stay;
		
		public BuildableIngredient(ShapelessRecipeBuilder builder)
		{
			this.builder = builder;
		}
		
		public BuildableIngredient addItem(ItemStack stack)
		{
			this.inputs.add(stack.copy());
			return this;
		}
		
		public BuildableIngredient addItems(Collection<ItemStack> stacks)
		{
			stacks.stream().filter(i -> i != null).forEach(this::addItem);
			return this;
		}
		
		public BuildableIngredient addOreDict(String od)
		{
			return addItems(OreDictionary.getOres(od));
		}
		
		public BuildableIngredient stay()
		{
			stay = true;
			return this;
		}
		
		public ShapelessRecipeBuilder build()
		{
			if(!inputs.isEmpty())
			{
				if(stay)
					builder.stay.add(builder.inputs.size());
				builder.inputs.add(inputs.toArray(new ItemStack[inputs.size()]));
			}
			
			return builder;
		}
	}
	
	static final class BakedShapelessRecipe extends ShapelessRecipes
	{
		private final ShapelessRecipeBuilder builder;
		private final NonNullList<Ingredient> stay;
		
		public BakedShapelessRecipe(ShapelessRecipeBuilder builder, ItemStack output, NonNullList<Ingredient> ingredients, NonNullList<Ingredient> stay)
		{
			super("", output, ingredients);
			this.builder = builder;
			this.stay = stay;
		}
		
		@Override
		public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
		{
			NonNullList<ItemStack> its = super.getRemainingItems(inv);
			for(int i = 0; i < its.size(); ++i)
			{
				ItemStack it = inv.getStackInSlot(i);
				for(Ingredient ing : stay)
					if(ing.apply(it))
					{
						its.add(it);
						break;
					}
			}
			return its;
		}
	}
}