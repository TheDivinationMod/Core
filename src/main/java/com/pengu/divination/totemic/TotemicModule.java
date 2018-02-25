package com.pengu.divination.totemic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pengu.divination.InfoDC;
import com.pengu.divination.core.RecipeHelper;
import com.pengu.divination.core.iModule;
import com.pengu.divination.totemic.init.BlocksDT;
import com.pengu.divination.totemic.init.ItemsDT;
import com.pengu.divination.totemic.init.RecipesDT;
import com.pengu.divination.totemic.init.TotemicSealsDT;
import com.pengu.divination.totemic.proxy.Common;
import com.pengu.hammercore.common.SimpleRegistration;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.SidedProxy;

public class TotemicModule implements iModule
{
	@SidedProxy(clientSide = "com.pengu.divination.totemic.proxy.Client", serverSide = "com.pengu.divination.totemic.proxy.Common")
	public static Common proxy;
	
	public static final Logger LOG = LogManager.getLogger(InfoDC.MOD_NAME + " Totemic");
	
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
		LOG.info("Loading Totemic Module...");
		
		TotemicSealsDT.load();
		
		SimpleRegistration.registerFieldBlocksFrom(BlocksDT.class, InfoDC.MOD_ID, tab);
		SimpleRegistration.registerFieldItemsFrom(ItemsDT.class, InfoDC.MOD_ID, tab);
	}
	
	@Override
	public void init()
	{
		proxy.init();
	}
	
	@Override
	public RecipeHelper getRecipes()
	{
		return RecipeHelper.getInstance(RecipesDT.class);
	}
}