package com.pengu.divination.totemic.client.gui;

import org.lwjgl.opengl.GL11;

import com.pengu.divination.InfoDC;
import com.pengu.divination.totemic.seals.core.TotemicSeal;
import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.client.utils.UtilsFX;
import com.pengu.hammercore.core.gui.GuiCentered;

import net.minecraft.client.renderer.GlStateManager;

public class GuiSealNote extends GuiCentered
{
	public final TotemicSeal seal;
	
	public GuiSealNote(TotemicSeal seal)
	{
		this.seal = seal;
		xSize = 118;
		ySize = 150;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GL11.glPushMatrix();
		GL11.glTranslated(guiLeft, guiTop, 0);
		
		GL11.glTranslated(xSize / 2, ySize / 2, 0);
		GL11.glScaled(1.5, 1.5, 1.5);
		GL11.glTranslated(-xSize / 2, -ySize / 2, 0);
		
		mouseX -= guiLeft;
		mouseY -= guiTop;
		
		UtilsFX.bindTexture(InfoDC.MOD_ID, "textures/totemic/gui/seal_note.png");
		RenderUtil.drawTexturedModalRect(0, 0, 0, 0, xSize, ySize);
		
		UtilsFX.bindTexture(InfoDC.MOD_ID, seal.tex);
		GL11.glPushMatrix();
		GL11.glTranslated(6.5, 8, 0);
		GL11.glScaled(1 / 2.5D, 1 / 2.5D, 1 / 2.5D);
		drawTexturedModalRect(0, 0, 0, 0, 256, 256);
		GL11.glPopMatrix();
		
		float pd = 16 / 2.5F;
		int pix = (int) pd;

		GL11.glPushMatrix();
		GL11.glTranslated(6.5, 8, 0);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.glLineWidth(1.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		GL11.glColor4f(0, 0, 0, .4F);
		
		for(int x = 0; x <= 16; ++x)
		{
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2f(x * pd, 0);
			GL11.glVertex2f(x * pd, pd * 16);
			GL11.glEnd();
			
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2f(0, x * pd);
			GL11.glVertex2f(pd * 16, x * pd);
			GL11.glEnd();
		}
		
		GL11.glColor4f(1, 1, 1, 1);
		
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		
		GL11.glPopMatrix();
		
		mouseX += (xSize / 2) / 1.5;
		mouseX -= 14;
		
		mouseY += (ySize / 2) / 1.5;
		mouseY -= 18;
		
		c: if(mouseX >= 6 && mouseY >= 8)
		{
			int x = mouseX - 6;
			int y = mouseY - 8;
			
			x /= pix * 1.59;
			y /= pix * 1.59;
			
			++x;
			++y;
			
			if(x > 16 || y > 16)
				break c;
			
			String s = "X: " + x + ", Z: " + y;
			fontRenderer.drawString(s, (int) ((xSize - fontRenderer.getStringWidth(s)) / 2), pix * 19, 0);
		}
		
		GL11.glPopMatrix();
	}
}