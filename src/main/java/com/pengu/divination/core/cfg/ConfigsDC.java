package com.pengu.divination.core.cfg;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.pengu.divination.api.iModule;
import com.pengu.divination.core.constants.InfoDC;
import com.pengu.hammercore.cfg.HCModConfigurations;
import com.pengu.hammercore.cfg.iConfigReloadListener;
import com.pengu.hammercore.cfg.fields.ModConfigPropertyBool;

import net.minecraftforge.common.config.Configuration;

@HCModConfigurations(modid = InfoDC.MOD_ID)
public class ConfigsDC implements iConfigReloadListener
{
	public static final Map<String, Configuration> moduleCfgs = new HashMap<>();
	public static Configuration cfg;
	
	@ModConfigPropertyBool(category = "GlobalClient", comment = "Should user be able to configure common part of modules?", defaultValue = true, name = "ModuleCommonEdit")
	public static boolean globalGuiEdit;
	
	@Override
	public void reloadCustom(Configuration cfgs)
	{
		cfg = cfgs;
		cfgs.setCategoryRequiresMcRestart("GlobalClient", true);
	}
	
	@Override
	public File getSuggestedConfigurationFile()
	{
		File f = new File("config", getModid());
		if(!f.isDirectory())
			f.mkdirs();
		return new File(f, "core.cfg");
	}
	
	public static Configuration forModule(iModule mod)
	{
		String name = mod.getName();
		if(moduleCfgs.containsKey(name))
			return moduleCfgs.get(name);
		File f = new File("config", InfoDC.MOD_ID);
		if(!f.isDirectory())
			f.mkdirs();
		Configuration c = new Configuration(new File(f, mod.getName().toLowerCase() + ".cfg"));
		moduleCfgs.put(name, c);
		return c;
	}
	
	public static Configuration forClientModule(iModule mod)
	{
		String name = mod.getName() + "-client";
		if(moduleCfgs.containsKey(name))
			return moduleCfgs.get(name);
		File f = new File(InfoDC.MOD_ID, "client-configs");
		if(!f.isDirectory())
			f.mkdirs();
		Configuration c = new Configuration(new File(f, mod.getName().toLowerCase() + ".cfg"));
		moduleCfgs.put(name, c);
		return c;
	}
}