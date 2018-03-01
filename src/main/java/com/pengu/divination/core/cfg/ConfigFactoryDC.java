package com.pengu.divination.core.cfg;

import java.util.Set;

import com.pengu.divination.core.gui.GuiMainConfigs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class ConfigFactoryDC implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance)
	{
	}

	@Override
	public boolean hasConfigGui()
	{
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen)
	{
		return new GuiMainConfigs(parentScreen);
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}
}