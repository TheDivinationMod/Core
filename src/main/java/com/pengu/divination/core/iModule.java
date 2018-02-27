package com.pengu.divination.core;

import net.minecraftforge.common.config.Configuration;

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
	
	default String getName()
	{
		return getClass().getSimpleName().replaceAll("Module", "");
	}
	
	default void applyConfigs(Configuration cfg)
	{
		
	}
}