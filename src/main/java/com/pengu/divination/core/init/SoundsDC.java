package com.pengu.divination.core.init;

import com.pengu.divination.core.constants.InfoDC;
import com.pengu.hammercore.utils.SoundObject;

public class SoundsDC
{
	public static final SoundObject //
	DUST = so("dust");
	
	private static SoundObject so(String p)
	{
		return new SoundObject(InfoDC.MOD_ID, p);
	}
}