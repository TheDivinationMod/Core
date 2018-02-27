package com.pengu.divination.core.data;

import java.util.HashMap;

import com.pengu.hammercore.annotations.MCFBus;
import com.pengu.hammercore.net.HCNetwork;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

@MCFBus
public class ResearchSystem
{
	public static final HashMap<String, PlayerResearchData> COMPLETED = new HashMap<>();
	
	public static PacketUpdateClientRD getPacketFor(EntityPlayer player)
	{
		return new PacketUpdateClientRD(getResearchForPlayer(player.getName()));
	}
	
	public static PlayerResearchData getResearchForPlayer(String playername)
	{
		return COMPLETED.get(playername);
	}
	
	public static boolean isResearchCompleted(EntityPlayer player, Item res)
	{
		return isResearchCompleted(player, res.getRegistryName().toString());
	}
	
	public static boolean isResearchCompleted(EntityPlayer player, String res)
	{
		if(player.world.isRemote)
			return ClientResearchData.isResearchCompleted(res);
		
		if(res == null)
			return true;
		PlayerResearchData researches = COMPLETED.getOrDefault(player.getName(), new PlayerResearchData());
		COMPLETED.put(player.getName(), researches);
		return researches != null && researches.isResearchCompleted(res);
	}
	
	public static void setResearchCompleted(EntityPlayer player, Item res, boolean isCompleted)
	{
		if(player.world.isRemote)
			return;
		
		PlayerResearchData researches = COMPLETED.getOrDefault(player.getName(), new PlayerResearchData());
		COMPLETED.put(player.getName(), researches);
		
		if(isCompleted && !researches.isResearchCompleted(res.getRegistryName().toString()))
			researches.researches.add(res.getRegistryName().toString());
		else if(!isCompleted && researches.isResearchCompleted(res.getRegistryName().toString()))
			researches.researches.remove(res.getRegistryName().toString());
		
		sync(player);
	}
	
	@SubscribeEvent
	public void playerLoaded(PlayerEvent.LoadFromFile e)
	{
		EntityPlayer player = e.getEntityPlayer();
		NBTTagCompound nbt = player.getEntityData().getCompoundTag("TheDivinationModData");
		
		PlayerResearchData comp = ResearchSystem.COMPLETED.getOrDefault(player, new PlayerResearchData()).deserializeSpecialData(nbt);
		ResearchSystem.COMPLETED.put(player.getName(), comp);
		sync(player);
	}
	
	@SubscribeEvent
	public void playerSaved(PlayerEvent.SaveToFile e)
	{
		EntityPlayer player = e.getEntityPlayer();
		PlayerResearchData comp = ResearchSystem.getResearchForPlayer(player.getName());
		player.getEntityData().setTag("TheDivinationModData", comp.serializeSpecialData());
	}
	
	@SubscribeEvent
	public void playerTick(PlayerTickEvent e)
	{
		if(!e.player.world.isRemote && e.player instanceof EntityPlayerMP && e.phase == Phase.START)
			getResearchForPlayer(e.player.getGameProfile().getName()).tick((EntityPlayerMP) e.player);
	}
	
	public static void sync(EntityPlayer player)
	{
		if(player instanceof EntityPlayerMP && !player.world.isRemote)
		{
			EntityPlayerMP mp = (EntityPlayerMP) player;
			HCNetwork.manager.sendTo(getPacketFor(mp), mp);
		}
	}
}