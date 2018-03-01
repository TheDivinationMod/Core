package com.pengu.divination.core;

import com.pengu.divination.api.events.UnlockDefaultResearchesEvent;
import com.pengu.divination.core.data.PlayerResearchData;
import com.pengu.divination.core.data.ResearchSystem;
import com.pengu.divination.core.init.ItemsDC;
import com.pengu.divination.core.net.PacketStartMusic;
import com.pengu.hammercore.annotations.MCFBus;
import com.pengu.hammercore.net.HCNetwork;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

@MCFBus
public class EventListenerCore
{
	@SubscribeEvent
	public void onCraft(ItemCraftedEvent evt)
	{
		EntityPlayer player = evt.player;
		
		if(player instanceof EntityPlayerMP && !(player instanceof FakePlayer) && !player.world.isRemote)
		{
			ItemStack out = evt.crafting;
			if(!out.isEmpty() && out.getItem() == ItemsDC.MYSTERIUM_DUST)
			{
				PlayerResearchData dat = ResearchSystem.getResearchForPlayer(player.getGameProfile().getName());
				if(!dat.isResearchCompleted(ItemsDC.MYSTERIUM_DUST))
				{
					ResearchSystem.setResearchCompleted(player, ItemsDC.MYSTERIUM_DUST, true);
					dat.spawnNotes((EntityPlayerMP) player);
					HCNetwork.manager.sendTo(new PacketStartMusic("https://www.dropbox.com/s/kypjmvyqkk1docf/magical_portal_open.ogg?dl=1"), (EntityPlayerMP) player);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void unlockDefaultResearches(UnlockDefaultResearchesEvent e)
	{
	}
}