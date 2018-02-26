package com.pengu.divination.totemic.seals.core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import com.pengu.divination.totemic.tiles.TileTotemicSeal;

public class TotemicSeal
{
	public static final List<TotemicSeal> seals = new ArrayList<>();
	public String name;
	public String tex;
	public final List<TSealPoint> points;
	public final Consumer<TileTotemicSeal> tick;
	public BufferedImage image;
	
	public TotemicSeal(List<TSealPoint> points, Consumer<TileTotemicSeal> tick)
	{
		this(points, tick, true);
	}
	
	private TotemicSeal(List<TSealPoint> points, Consumer<TileTotemicSeal> tick, boolean register)
	{
		this.points = points;
		this.tick = tick;
		if(register)
			seals.add(this);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof TotemicSeal && equalsIgnoreRotation(((TotemicSeal) obj).points);
	}
	
	public boolean equalsIgnoreRotation(List<TSealPoint> seal)
	{
		if(seal.size() != points.size())
			return false;
		for(TSealPoint p : points)
			if(!seal.contains(p))
				return false;
		return true;
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
		
		return new TotemicSeal(Collections.unmodifiableList(np), tick, false);
	}
}