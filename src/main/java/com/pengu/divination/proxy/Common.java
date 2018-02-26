package com.pengu.divination.proxy;

public class Common
{
	private EffectManager fx;
	
	protected EffectManager createFX()
	{
		return new EffectManager();
	}
	
	public void preInit()
	{
		
	}
	
	public void init()
	{
		
	}
	
	public void playOnce(String url)
	{
		
	}
	
	public EffectManager getFX()
	{
		if(fx == null)
			fx = createFX();
		return fx;
	}
}