package com.pengu.divination.api;

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
	
	default iModuleProxy getProxy()
	{
		return null;
	}
}