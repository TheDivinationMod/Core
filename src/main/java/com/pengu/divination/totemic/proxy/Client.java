package com.pengu.divination.totemic.proxy;

import com.pengu.divination.core.client.render.entity.RenderNPC;
import com.pengu.divination.totemic.client.gui.GuiSealNote;
import com.pengu.divination.totemic.client.isr.ItemRenderSealNote;
import com.pengu.divination.totemic.client.tesr.TESRTotemicSeal;
import com.pengu.divination.totemic.entity.npc.EntityShaman;
import com.pengu.divination.totemic.init.ItemsDT;
import com.pengu.divination.totemic.seals.core.EnumSealColor;
import com.pengu.divination.totemic.seals.core.TotemicSeal;
import com.pengu.divination.totemic.tiles.TileTotemicSeal;
import com.pengu.hammercore.client.render.item.ItemRenderingHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class Client extends Common
{
	@Override
	public void preInit()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityShaman.class, RenderNPC.FACTORY);
	}
	
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
		
		ItemRenderingHandler.INSTANCE.appendItemRender(ItemsDT.SEAL_NOTE, new ItemRenderSealNote());
	}
	
	@Override
	public void openSealNote(TotemicSeal seal)
	{
		Minecraft.getMinecraft().displayGuiScreen(new GuiSealNote(seal));
	}
	
	@Override
	public void applyClientConfigs(Configuration cfg)
	{
		
	}
}