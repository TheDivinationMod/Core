package com.pengu.divination.mirror;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pengu.divination.Divination;
import com.pengu.divination.api.iModule;
import com.pengu.divination.api.iModuleProxy;
import com.pengu.divination.core.constants.InfoDC;
import com.pengu.divination.core.init.ItemsDC;
import com.pengu.divination.mirror.init.BlocksDM;
import com.pengu.divination.mirror.init.ItemsDM;
import com.pengu.divination.mirror.proxy.Common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.SidedProxy;

public class MirrorModule implements iModule
{
	@SidedProxy(clientSide = "com.pengu.divination.mirror.proxy.Client", serverSide = "com.pengu.divination.mirror.proxy.Common")
	public static Common proxy;
	
	public static final Logger LOG = LogManager.getLogger(InfoDC.MOD_NAME + ": Mirror");
	public static Configuration configs;
	public static final CreativeTabs tab = new CreativeTabs(InfoDC.MOD_ID + ".mirror")
	{
		@Override
		public ItemStack getTabIconItem()
		{
			return new ItemStack(ItemsDC.MYSTERIUM_CLUSTER);
		}
	};
	
	@Override
	public void preInit()
	{
		proxy.preInit();
		
		Divination.registerBlocksFrom(BlocksDM.class, tab);
		Divination.registerItemsFrom(ItemsDM.class, tab);
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
		return "Mirror";
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