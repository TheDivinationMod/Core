package com.pengu.divination;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pengu.divination.api.iModule;
import com.pengu.divination.api.iModuleProxy;
import com.pengu.divination.api.events.GetDivinationModulesEvent;
import com.pengu.divination.core.cfg.ConfigsDC;
import com.pengu.divination.core.constants.InfoDC;
import com.pengu.divination.core.constants.URLsDC;
import com.pengu.divination.core.init.BlocksDC;
import com.pengu.divination.core.init.ItemsDC;
import com.pengu.divination.core.init.SoundsDC;
import com.pengu.divination.core.init.WorldGenDC;
import com.pengu.divination.core.proc.ProcessTransformBlock;
import com.pengu.divination.mirror.MirrorModule;
import com.pengu.divination.proxy.Common;
import com.pengu.divination.totemic.TotemicModule;
import com.pengu.hammercore.common.SimpleRegistration;
import com.pengu.hammercore.common.utils.IOUtils;
import com.pengu.hammercore.json.JSONArray;
import com.pengu.hammercore.json.JSONObject;
import com.pengu.musiclayer.api.MusicLayer;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;

@Mod(modid = InfoDC.MOD_ID, version = InfoDC.MOD_VERSION, name = InfoDC.MOD_NAME, dependencies = "required-after:hammercore;required-after:musiclayer", certificateFingerprint = "4d7b29cd19124e986da685107d16ce4b49bc0a97", guiFactory = "com.pengu.divination.core.cfg.ConfigFactoryDC", updateJSON = URLsDC.GIT_JSON_UPDATE)
public class Divination
{
	@Instance
	public static Divination instance;
	
	@SidedProxy(serverSide = "com.pengu.divination.proxy.Common", clientSide = "com.pengu.divination.proxy.Client")
	public static Common proxy;
	
	public static ModMetadata modMeta;
	public static String modDescOrigin;
	public static iModule currentModule;
	public static final List<iModule> modules = new ArrayList<>();
	public static final Map<Item, String> items = new HashMap<>();
	public static final Logger LOG = LogManager.getLogger(InfoDC.MOD_NAME);
	public static boolean globalGuiEdit;
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
	public static MirrorModule mirror = new MirrorModule();
	
	//
	
	@EventHandler
	public void certificateViolation(FMLFingerprintViolationEvent e)
	{
		MusicLayer.LOG.warn("*****************************");
		MusicLayer.LOG.warn("WARNING: Somebody has been tampering with The Divination Mod jar!");
		MusicLayer.LOG.warn("It is highly recommended that you redownload mod from " + InfoDC.CURSEFORGE_PROJECT_URL + " !");
		MusicLayer.LOG.warn("*****************************");
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		LOG.info("Performing preInit!");
		MinecraftForge.EVENT_BUS.register(proxy);
		meta(e.getModMetadata());
		proxy.preInit();
		
		globalGuiEdit = ConfigsDC.globalGuiEdit;

		modules.add(totemic);
		modules.add(mirror);
		
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
		
		SimpleRegistration.registerFieldSoundsFrom(SoundsDC.class);
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
	
	@EventHandler
	public void serverStop(FMLServerStartedEvent e)
	{
		ProcessTransformBlock.WORLD_ACTIVE.clear();
	}
	
	@EventHandler
	public void serverStop(FMLServerStoppedEvent e)
	{
		ProcessTransformBlock.WORLD_ACTIVE.clear();
	}
	
	public static Collection<iModule> getModules()
	{
		return modules;
	}
	
	private static ModMetadata meta(ModMetadata md)
	{
		JSONObject obj = URLsDC.objFromUrl(URLsDC.GIT_JSON_AUTHORS);
		md.authorList = Collections.unmodifiableList(new ArrayList<>(obj.keySet()));
		md.name = InfoDC.MOD_NAME;
		md.modId = InfoDC.MOD_ID;
		md.autogenerated = false;
		{
			md.description = "Short story: A mod about several aspects of magic working together. \nFull Story:\n" + IOUtils.ioget(URLsDC.GIT_TXT_DESCRIPTION).replaceAll("<p>", "").replaceAll("</p>", "");
			while(md.description.endsWith("\n"))
				md.description = md.description.substring(0, md.description.length() - 1);
		}
		md.version = InfoDC.MOD_VERSION;
		md.logoFile = "logo.png";
		md.url = "https://minecraft.curseforge.com/projects/" + InfoDC.CURSEFORGE_PROJECT_ID;
		md.credits = "This mod is possible thanks to our team:";
		for(String k : obj.keySet())
		{
			JSONArray ar = obj.optJSONArray(k);
			int len = ar.length();
			String roles = "";
			for(int i = 0; i < len; ++i)
			{
				String rar = ar.optString(i);
				if(i == 0)
					roles += rar;
				else if(i == len - 1)
					roles += " and " + rar;
				else
					roles += ", " + rar;
			}
			md.credits += "\n \u25A0 " + k + " - " + roles;
		}
		modDescOrigin = md.description;
		modMeta = md;
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