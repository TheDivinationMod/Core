package com.pengu.divination;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pengu.divination.api.RecipeHelper;
import com.pengu.divination.api.iModule;
import com.pengu.divination.api.iModuleProxy;
import com.pengu.divination.api.events.GetDivinationModulesEvent;
import com.pengu.divination.core.cfg.ConfigsDC;
import com.pengu.divination.core.constants.InfoDC;
import com.pengu.divination.core.constants.URLsDC;
import com.pengu.divination.core.init.BlocksDC;
import com.pengu.divination.core.init.ItemsDC;
import com.pengu.divination.core.init.RecipesDC;
import com.pengu.divination.core.init.WorldGenDC;
import com.pengu.divination.proxy.Common;
import com.pengu.divination.totemic.TotemicModule;
import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.common.SimpleRegistration;
import com.pengu.musiclayer.api.MusicLayer;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(modid = InfoDC.MOD_ID, version = InfoDC.MOD_VERSION, name = InfoDC.MOD_NAME, dependencies = "required-after:hammercore;required-after:musiclayer", certificateFingerprint = "4d7b29cd19124e986da685107d16ce4b49bc0a97", guiFactory = "com.pengu.divination.core.cfg.ConfigFactoryDC", updateJSON = URLsDC.GIT_JSON_UPDATE)
public class Divination
{
	@Instance
	public static Divination instance;
	
	@SidedProxy(serverSide = "com.pengu.divination.proxy.Common", clientSide = "com.pengu.divination.proxy.Client")
	public static Common proxy;
	
	public static iModule currentModule;
	public static final List<iModule> modules = new ArrayList<>();
	public static final Map<Item, String> items = new HashMap<>();
	public static final Logger LOG = LogManager.getLogger(InfoDC.MOD_NAME);
	public static final CreativeTabs tab = new CreativeTabs(InfoDC.MOD_ID)
	{
		@Override
		public ItemStack getTabIconItem()
		{
			return new ItemStack(BlocksDC.MYSTERIUM_ORE);
		}
	};
	
	//
	
	public static TotemicModule totemic = new TotemicModule();
	
	//
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		LOG.info("Performing preInit!");
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(proxy);
		meta(e.getModMetadata());
		proxy.preInit();
		
		modules.add(totemic);
		
		List<iModule> mds = new ArrayList<>();
		MinecraftForge.EVENT_BUS.post(new GetDivinationModulesEvent(mds));
		modules.addAll(mds);
		
		// modules.removeIf(mod -> !ConfigsDC.cfg.getBoolean(mod.getName(),
		// "Modules", true, "Should module '" + mod.getName() + "' be
		// active?"));
		
		if(ConfigsDC.cfg.hasChanged())
			ConfigsDC.cfg.save();
		
		SimpleRegistration.registerFieldBlocksFrom(BlocksDC.class, InfoDC.MOD_ID, tab);
		SimpleRegistration.registerFieldItemsFrom(ItemsDC.class, InfoDC.MOD_ID, tab);
		
		forEachModule(mod ->
		{
			mod.applyConfigs(ConfigsDC.forModule(mod));
			iModuleProxy prox = mod.getProxy();
			if(prox != null)
				prox.applyClientConfigs(ConfigsDC.forClientModule(mod));
		});
		
		forEachModule(mod ->
		{
			LOG.info("PreLoad module '" + mod.getName() + "'");
			mod.preInit();
		});
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e)
	{
		LOG.info("Performing init!");
		WorldGenDC.init();
		proxy.init();
		
		forEachModule(mod ->
		{
			LOG.info("Load module '" + mod.getName() + "'");
			mod.init();
			mod.getRecipes().smelting();
		});
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
		LOG.info("Performing postInit!");
		forEachModule(mod ->
		{
			LOG.info("PostLoad module '" + mod.getName() + "'");
			mod.postInit();
		});
	}
	
	@SubscribeEvent
	public void addRecipes(RegistryEvent.Register<IRecipe> reg)
	{
		IForgeRegistry<IRecipe> fr = reg.getRegistry();
		RecipeHelper.getInstance(RecipesDC.class) //
		        .collect().stream().filter(r -> r != null).forEach(fr::register);
		forEachModule(mod -> mod.getRecipes().collect().stream().filter(r -> r != null).forEach(fr::register));
	}
	
	public static Collection<iModule> getModules()
	{
		return modules;
	}
	
	@EventHandler
	public void certificateViolation(FMLFingerprintViolationEvent e)
	{
		MusicLayer.LOG.warn("*****************************");
		MusicLayer.LOG.warn("WARNING: Somebody has been tampering with The Divination Mod jar!");
		MusicLayer.LOG.warn("It is highly recommended that you redownload mod from https://minecraft.curseforge.com/projects/289086 !");
		MusicLayer.LOG.warn("*****************************");
	}
	
	private static ModMetadata meta(ModMetadata md)
	{
		md.authorList = HammerCore.getHCAuthorsArray();
		md.name = InfoDC.MOD_NAME;
		md.modId = InfoDC.MOD_ID;
		md.autogenerated = false;
		md.description = "A mod about several aspects of magic working together.";
		md.version = InfoDC.MOD_VERSION;
		md.logoFile = "logo.png";
		return md;
	}
	
	public static void registerItemsFrom(Class<?> c, CreativeTabs tab)
	{
		if(currentModule == null)
			throw new IllegalStateException("Invalid attempt to register items without assigning current module!");
		for(Field f : c.getDeclaredFields())
			if(Item.class.isAssignableFrom(f.getType()) && Modifier.isStatic(f.getModifiers()))
			{
				Item it = null;
				try
				{
					it = (Item) f.get(null);
				} catch(Throwable er)
				{
					er.printStackTrace();
				}
				SimpleRegistration.registerItem(it, InfoDC.MOD_ID, tab);
				items.put(it, currentModule.getName());
			}
	}
	
	public static void registerBlocksFrom(Class<?> c, CreativeTabs tab)
	{
		if(currentModule == null)
			throw new IllegalStateException("Invalid attempt to register blocks without assigning current module!");
		for(Field f : c.getDeclaredFields())
			if(Block.class.isAssignableFrom(f.getType()) && Modifier.isStatic(f.getModifiers()))
			{
				Block it = null;
				try
				{
					it = (Block) f.get(null);
				} catch(Throwable er)
				{
					er.printStackTrace();
				}
				SimpleRegistration.registerBlock(it, InfoDC.MOD_ID, tab);
				Item i = Item.getItemFromBlock(it);
				if(i != null)
					items.put(i, currentModule.getName());
			}
	}
	
	public String getModuleName(Item it)
	{
		return items.get(it);
	}
	
	public static void forEachModule(Consumer<iModule> m)
	{
		for(iModule mod : getModules())
		{
			currentModule = mod;
			m.accept(mod);
		}
		currentModule = null;
	}
	
	public static iModule currentModule()
	{
		return currentModule;
	}
}