package com.pengu.divination.totemic.intr.hammercore;

import com.pengu.divination.totemic.init.BlocksDT;
import com.pengu.hammercore.api.mhb.RaytracePlugin;
import com.pengu.hammercore.api.mhb.iRayCubeRegistry;
import com.pengu.hammercore.api.mhb.iRayRegistry;

@RaytracePlugin
public class RaytracePluginDT implements iRayRegistry
{
	@Override
	public void registerCubes(iRayCubeRegistry cube)
	{
		cube.bindBlockCubeManager(BlocksDT.SEAL, BlocksDT.SEAL);
	}
}