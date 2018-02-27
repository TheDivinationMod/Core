package com.pengu.divination.api.events;

import java.util.List;

import com.pengu.divination.core.iModule;

import net.minecraftforge.fml.common.eventhandler.Event;

public class GetDivinationModulesEvent extends Event
{
	public final List<iModule> moduleList;
	
	public GetDivinationModulesEvent(List<iModule> mds)
	{
		moduleList = mds;
	}
}