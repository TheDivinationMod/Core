package com.pengu.divination.core.net;

import com.pengu.divination.Divination;
import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketStartMusic implements iPacket, iPacketListener<PacketStartMusic, iPacket>
{
	public String url;
	
	public PacketStartMusic(String url)
	{
		this.url = url;
	}
	
	public PacketStartMusic()
	{
	}
	
	@Override
	public iPacket onArrived(PacketStartMusic packet, MessageContext context)
	{
		Divination.proxy.playOnce(url);
		return null;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("URL", url);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		url = nbt.getString("URL");
	}
}