package com.pengu.divination.core.init;

import com.pengu.divination.core.world.WorldGenMysteriumOre;
import com.pengu.hammercore.world.gen.WorldRetroGen;

public class WorldGenDC
{
	public static void init()
	{
		WorldRetroGen.addWorldFeature(new WorldGenMysteriumOre(BlocksDC.MYSTERIUM_ORE.getDefaultState()));
	}
}