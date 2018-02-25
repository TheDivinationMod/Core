package com.pengu.divination.totemic.seals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class TotemicSeal
{
	public final List<TSealPoint> points;
	public final Consumer<TileTotemicSeal> tick;
	
	public TotemicSeal(List<TSealPoint> points, Consumer<TileTotemicSeal> tick)
	{
		this.points = points;
		this.tick = tick;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof TotemicSeal && equalsIgnoreRotation((TotemicSeal) obj);
	}
	
	public boolean equalsIgnoreRotation(TotemicSeal seal)
	{
		if(this.points.size() == seal.points.size())
			return this.points.containsAll(seal.points);
		return false;
	}
	
	public TotemicSeal rotate(int angle)
	{
		List<TSealPoint> np = new ArrayList<>();
		
		switch(angle % 4 - 1)
		{
		case 0:
			for(TSealPoint p : points)
				np.add(new TSealPoint((byte) (15 - p.y), p.x, p.color));
		case 1:
			for(TSealPoint p : points)
				np.add(new TSealPoint((byte) (15 - p.x), (byte) (15 - p.y), p.color));
		case 2:
			for(TSealPoint p : points)
				np.add(new TSealPoint(p.y, (byte) (15 - p.x), p.color));
		case 3:
			return this;
		}
		
		return new TotemicSeal(Collections.unmodifiableList(np), tick);
	}
}