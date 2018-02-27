package com.pengu.divination.core.net;

import com.pengu.divination.Divination;
import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketStopMusic implements iPacket, iPacketListener<PacketStopMusic, iPacket>
{
	public PacketStopMusic()
	{
	}
	
	@Override
	public iPacket onArrived(PacketStopMusic packet, MessageContext context)
	{
		Divination.proxy.stopCurrentMusic();
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
	}
}