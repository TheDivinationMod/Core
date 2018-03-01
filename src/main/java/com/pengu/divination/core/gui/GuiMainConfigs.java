package com.pengu.divination.core.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;

public class GuiMainConfigs extends GuiScreen
{
	public final GuiScreen parent;
	
	public Side viewSide = null;
	
	public GuiMainConfigs(GuiScreen parent)
	{
		this.parent = parent;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
	}
}