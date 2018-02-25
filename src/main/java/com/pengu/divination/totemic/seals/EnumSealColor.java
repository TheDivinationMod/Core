package com.pengu.divination.totemic.seals;

import javax.annotation.Nullable;

import com.pengu.hammercore.utils.ColorHelper;

public enum EnumSealColor
{
	GREEN(0x00FF00), //
	RED(0xFF0000), //
	YELLOW(0xFFFF00);
	
	public final int color, r, g, b;
	
	private EnumSealColor(int color)
	{
		this.color = color;
		this.r = (int) (ColorHelper.getRed(color) * 255F);
		this.g = (int) (ColorHelper.getGreen(color) * 255F);
		this.b = (int) (ColorHelper.getBlue(color) * 255F);
	}
	
	public int getColor()
	{
		return color;
	}
	
	public float getR()
	{
		return r / 255F;
	}
	
	public float getB()
	{
		return b / 255F;
	}
	
	public float getG()
	{
		return g / 255F;
	}
	
	@Nullable
	public static EnumSealColor choose(int argb)
	{
		if(ColorHelper.getAlpha(argb) >= .5F)
		{
			int r = (int) (ColorHelper.getRed(argb) * 255F);
			int g = (int) (ColorHelper.getGreen(argb) * 255F);
			int b = (int) (ColorHelper.getBlue(argb) * 255F);
			
			EnumSealColor closestMatch = null;
			int minMSE = Integer.MAX_VALUE;
			for(EnumSealColor c : values())
			{
				int mse = c.computeMSE(r, g, b);
				if(mse < minMSE)
				{
					minMSE = mse;
					closestMatch = c;
				}
			}
			
			return closestMatch;
		}
		
		return null;
	}
	
	private int computeMSE(int pixR, int pixG, int pixB)
	{
		return ((pixR - r) * (pixR - r) + (pixG - g) * (pixG - g) + (pixB - b) * (pixB - b)) / 3;
	}
}