package com.pengu.divination.totemic.proxy;

import com.pengu.divination.totemic.client.tesr.TESRTotemicSeal;
import com.pengu.divination.totemic.init.ItemsDT;
import com.pengu.divination.totemic.seals.core.EnumSealColor;
import com.pengu.divination.totemic.tiles.TileTotemicSeal;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class Client extends Common
{
	@Override
	public void init()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileTotemicSeal.class, new TESRTotemicSeal());
		
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tint) ->
		{
			if(tint == 1)
			{
				NBTTagCompound nbt = stack.getTagCompound();
				if(nbt != null && nbt.hasKey("Color", NBT.TAG_INT))
					return EnumSealColor.values()[nbt.getInteger("Color") % EnumSealColor.values().length].getColor();
				return 0x591400;
			}
			
			return 0xFFFFFF;
		}, ItemsDT.BRUSH);
	}
}