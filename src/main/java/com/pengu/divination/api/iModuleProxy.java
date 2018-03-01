package com.pengu.divination.api;

import net.minecraftforge.common.config.Configuration;

public interface iModuleProxy
{
	void applyClientConfigs(Configuration cfg);
	
	default void preInit()
	{
		
	}
	
	default void init()
	{
		
	}
	
	default void postInit()
	{
		
	}
}