package com.pengu.divination.api.events;

import com.pengu.divination.core.data.PlayerResearchData;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.Event;

public class UnlockDefaultResearchesEvent extends Event
{
	public final PlayerResearchData data;
	
	public UnlockDefaultResearchesEvent(PlayerResearchData data)
	{
		this.data = data;
	}
	
	public void unlock(Item item)
	{
		data.completeResearch(item, true);
	}
	
	public void unlock(String str)
	{
		data.completeResearch(str, true);
	}
}