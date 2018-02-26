package com.pengu.divination.core.net;

import com.pengu.divination.Divination;
import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;
import com.pengu.hammercore.net.pkt.thunder.Thunder;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketFXWisp implements iPacket, iPacketListener<PacketFXWisp, iPacket>
{
	public Vec3d pos, tpos;
	public float size;
	public Thunder.Layer data;
	
	@Override
	public iPacket onArrived(PacketFXWisp packet, MessageContext context)
	{
		if(context.side == Side.SERVER)
			packet.server(context.getServerHandler().player);
		if(context.side == Side.CLIENT)
			packet.client();
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	private void client()
	{
		Divination.proxy.getFX().wisp(Minecraft.getMinecraft().world, pos, tpos, size, data);
	}
	
	private void server(EntityPlayerMP sender)
	{
		Divination.proxy.getFX().wisp(sender.world, pos, tpos, size, data);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setFloat("size", size);
		nbt.setTag("data", data.serializeNBT());
		Helper.setVec3d(nbt, "pos", pos);
		if(tpos != null)
			Helper.setVec3d(nbt, "tpos", tpos);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		size = nbt.getFloat("size");
		data = Thunder.Layer.deserializeNBT(nbt.getCompoundTag("data"));
		pos = Helper.getVec3d(nbt, "pos");
		if(nbt.hasKey("tposX"))
			tpos = Helper.getVec3d(nbt, "tpos");
	}
}