package com.pengu.divination.totemic.init;

import com.pengu.divination.totemic.seals.TSealStarting;
import com.pengu.divination.totemic.seals.core.SealDefiner;

public class TotemicSealsDT
{
	public static void load()
	{
		
	}
	
	static
	{
		SealDefiner.loadSeal("starting", new TSealStarting(), "guidance");
	}
}