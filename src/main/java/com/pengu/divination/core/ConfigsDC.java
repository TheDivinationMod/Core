package com.pengu.divination.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.pengu.divination.InfoDC;
import com.pengu.divination.api.iModule;
import com.pengu.hammercore.cfg.HCModConfigurations;
import com.pengu.hammercore.cfg.iConfigReloadListener;

import net.minecraftforge.common.config.Configuration;

@HCModConfigurations(modid = InfoDC.MOD_ID)
public class ConfigsDC implements iConfigReloadListener
{
	public static final Map<String, Configuration> moduleCfgs = new HashMap<>();
	public static Configuration cfg;
	
	@Override
	public void reloadCustom(Configuration cfgs)
	{
		cfg = cfgs;
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
}