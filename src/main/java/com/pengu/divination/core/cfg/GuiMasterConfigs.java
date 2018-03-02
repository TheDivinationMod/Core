package com.pengu.divination.core.cfg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.pengu.divination.Divination;
import com.pengu.divination.api.iModule;
import com.pengu.divination.core.constants.InfoDC;
import com.pengu.hammercore.cfg.gui.HCConfigGui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;

public class GuiMasterConfigs extends GuiScreen
{
	public final GuiScreen parent;
	
	public Side viewSide = null;
	
	public GuiMasterConfigs(GuiScreen parent)
	{
		this.parent = parent;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		buttonList.add(new GuiButton(0, 10, height - 27, 118, 20, I18n.format("gui.back")));
		
		if(viewSide == null)
		{
			buttonList.add(new GuiButton(1, (width - 300) / 2 - 4, (height - 20) / 2, 100, 20, "Client Configs"));
			buttonList.add(new GuiButton(2, (width - 100) / 2, (height - 20) / 2, 100, 20, "Core Configs"));
			GuiButton cmn;
			buttonList.add(cmn = new GuiButton(3, (width + 100) / 2 + 4, (height - 20) / 2, 100, 20, "Common Configs"));
			cmn.enabled = ConfigsDC.globalGuiEdit;
		} else
		{
			ArrayList<iModule> mods = (ArrayList<iModule>) Divination.getModules();
			int co = mods.size();
			
			List<GuiButton> temp = new ArrayList<>();
			int x = 0, y = 0;
			for(int i = 0; i < co; ++i)
			{
				temp.add(new GuiButton(1 + i, x, y, 100, 20, mods.get(i).getName()));
				x += 104;
				if(x + 4 >= width)
				{
					x = 0;
					y += 24;
				}
			}
			
			int minX = 4, minY = 4;
			int maxX = x, maxY = y + 20;
			
			int cx = (width - (maxX - minX)) / 2;
			int cy = (height - (maxY - minY)) / 2;
			
			for(GuiButton c : temp)
			{
				c.x += cx;
				c.y += cy;
				
				buttonList.add(c);
			}
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		//
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(keyCode == 1)
		{
			if(viewSide == null)
				mc.displayGuiScreen(parent);
			else
			{
				viewSide = null;
				setWorldAndResolution(mc, width, height);
			}
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton b) throws IOException
	{
		if(b.id == 0)
		{
			if(viewSide == null)
				mc.displayGuiScreen(parent);
			else
			{
				viewSide = null;
				setWorldAndResolution(mc, width, height);
			}
		}
		
		if(viewSide != null && b.id > 0)
		{
			ArrayList<iModule> mods = (ArrayList<iModule>) Divination.getModules();
			iModule mod = mods.get(b.id - 1);
			HCConfigGui g;
			mc.displayGuiScreen(g = new HCConfigGui(this, viewSide == Side.SERVER ? ConfigsDC.forModule(mod) : ConfigsDC.forClientModule(mod), InfoDC.MOD_ID));
			g.title = (viewSide == Side.CLIENT ? "Client" : "Common") + " Configs for module " + mod.getName();
			g.titleLine2 = "";
		} else
		{
			if(b.id == 1)
			{
				viewSide = Side.CLIENT;
				setWorldAndResolution(mc, width, height);
			}
			if(b.id == 3)
			{
				viewSide = Side.SERVER;
				setWorldAndResolution(mc, width, height);
			}
			if(b.id == 2)
				mc.displayGuiScreen(new HCConfigGui(this, ConfigsDC.cfg, InfoDC.MOD_ID));
		}
	}
}