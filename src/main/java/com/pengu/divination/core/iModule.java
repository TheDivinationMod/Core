package com.pengu.divination.core;

public interface iModule
{
	RecipeHelper getRecipes();
	
	default void preInit()
	{
	};
	
	default void init()
	{
	};
	
	default void postInit()
	{
	};
}