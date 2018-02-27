package com.pengu.divination.core.world;

import com.pengu.hammercore.world.gen.WorldGenFeatureOre;

import net.minecraft.block.state.IBlockState;

public class WorldGenMysteriumOre extends WorldGenFeatureOre
{
	public WorldGenMysteriumOre(IBlockState ore)
	{
		super(ore);
		
		maxClusters = 6;
		maxCusterSize = 3;
		minY = 0;
		maxY = 64;
	}
}