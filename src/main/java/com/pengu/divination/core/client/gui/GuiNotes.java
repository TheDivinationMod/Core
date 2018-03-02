package com.pengu.divination.core.client.gui;

import org.lwjgl.opengl.GL11;

import com.pengu.divination.core.constants.InfoDC;
import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.client.utils.UtilsFX;
import com.pengu.hammercore.core.gui.GuiCentered;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiNotes extends GuiCentered
{
	public final String text;
	
	public GuiNotes(String text)
	{
		this.text = text;
		xSize = 118;
		ySize = 150;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		float pd = 16 / 2.5F;
		int pix = (int) pd;
		
		GL11.glPushMatrix();
		GL11.glTranslated(guiLeft, guiTop, 0);
		
		GL11.glTranslated(xSize / 2, ySize / 2, 0);
		GL11.glScaled(1.5, 1.5, 1.5);
		GL11.glTranslated(-xSize / 2, -ySize / 2, 0);
		
		mouseX -= guiLeft;
		mouseY -= guiTop;
		
		UtilsFX.bindTexture(InfoDC.MOD_ID, "textures/totemic/gui/seal_note.png");
		RenderUtil.drawTexturedModalRect(0, 0, 0, 0, xSize, ySize);
		
		GL11.glPushMatrix();
		GL11.glScaled(1 / 1.5, 1 / 1.5, 1 / 1.5);
		fontRenderer.drawSplitString(I18n.format(text), 10, 12, (int) (xSize * 1.3), 0);
		GL11.glPopMatrix();
		
		GL11.glColor4f(1, 1, 1, 1);
		
		GL11.glPushMatrix();
		GL11.glTranslated(6.5, 8, 0);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.glLineWidth(1.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		
		GL11.glColor4f(1, 1, 1, 1);
		
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		
		GL11.glPopMatrix();
		
		GL11.glPopMatrix();
	}
}