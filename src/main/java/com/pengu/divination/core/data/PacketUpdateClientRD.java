package com.pengu.divination.core.data;

import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketUpdateClientRD implements iPacket, iPacketListener<PacketUpdateClientRD, iPacket>
{
	public NBTTagCompound nbt;
	
	public PacketUpdateClientRD()
	{
	}
	
	public PacketUpdateClientRD(PlayerResearchData researches)
	{
		nbt = researches.serializeSpecialData();
	}
	
	@Override
	public iPacket onArrived(PacketUpdateClientRD packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			ClientResearchData.fromNBT(packet.nbt);
		return null;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.nbt = nbt.getCompoundTag("Data");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("Data", this.nbt);
	}
}