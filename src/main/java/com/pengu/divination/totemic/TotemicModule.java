package com.pengu.divination.totemic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pengu.divination.Divination;
import com.pengu.divination.api.iModule;
import com.pengu.divination.api.iModuleProxy;
import com.pengu.divination.core.constants.InfoDC;
import com.pengu.divination.core.init.EntitiesDC;
import com.pengu.divination.totemic.init.BlocksDT;
import com.pengu.divination.totemic.init.ItemsDT;
import com.pengu.divination.totemic.init.TotemicSealsDT;
import com.pengu.divination.totemic.proxy.Common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.SidedProxy;

public class TotemicModule implements iModule
{
	@SidedProxy(clientSide = "com.pengu.divination.totemic.proxy.Client", serverSide = "com.pengu.divination.totemic.proxy.Common")
	public static Common proxy;
	
	public static final Logger LOG = LogManager.getLogger(InfoDC.MOD_NAME + ": Totemic");
	public static Configuration configs;
	public static final CreativeTabs tab = new CreativeTabs(InfoDC.MOD_ID + ".totemic")
	{
		@Override
		public ItemStack getTabIconItem()
		{
			return new ItemStack(ItemsDT.BRUSH);
		}
	};
	
	@Override
	public void preInit()
	{
		proxy.preInit();
		
		TotemicSealsDT.load();
		
		Divination.registerBlocksFrom(BlocksDT.class, tab);
		Divination.registerItemsFrom(ItemsDT.class, tab);
		
		EntitiesDC.loadTotemic();
	}
	
	@Override
	public void init()
	{
		proxy.init();
	}
	
	@Override
	public void postInit()
	{
		proxy.postInit();
	}
	
	@Override
	public String getName()
	{
		return "Totemic";
	}
	
	@Override
	public void applyConfigs(Configuration cfg)
	{
		configs = cfg;
	}
	
	@Override
	public iModuleProxy getProxy()
	{
		return proxy;
	}
}