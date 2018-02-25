package com.pengu.divination.core.entity.npc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PlayerTalkToNpcEvent extends PlayerEvent
{
	public final EntityNPC npc;
	
	public PlayerTalkToNpcEvent(EntityPlayer player, EntityNPC npc)
	{
		super(player);
		this.npc = npc;
	}
}