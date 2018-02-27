package com.pengu.divination.core.init;

import com.pengu.divination.Divination;
import com.pengu.divination.InfoDC;
import com.pengu.divination.core.entity.npc.EntityNPC;
import com.pengu.divination.totemic.entity.npc.EntityShaman;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntitiesDC
{
	private static int id;
	
	public static void loadTotemic()
	{
		EntityRegistry.registerModEntity(new ResourceLocation(InfoDC.MOD_ID, "shaman"), EntityShaman.class, "shaman", ++id, Divination.instance, 64, 1, true, 0x55FF55, 0x22CC22);
	}
}