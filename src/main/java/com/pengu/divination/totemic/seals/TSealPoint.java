package com.pengu.divination.totemic.seals;

public class TSealPoint
{
	public final byte x, y;
	public final EnumSealColor color;
	
	public TSealPoint(byte x, byte y, EnumSealColor col)
	{
		this.x = x;
		this.y = y;
		this.color = col;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof TSealPoint && equalsIgnoreRotation((TSealPoint) obj);
	}
	
	public boolean equalsIgnoreRotation(TSealPoint b)
	{
		if(b.color != color)
			return false;
		if(x == 15 - b.x && y == b.y)
			return true;
		if(x == b.x && y == 15 - b.y)
			return true;
		return x == 15 - b.x && y == 15 - b.y;
	}
}