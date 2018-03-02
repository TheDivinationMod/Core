package com.pengu.divination.totemic.client.isr;

import org.lwjgl.opengl.GL11;

import com.pengu.divination.core.constants.InfoDC;
import com.pengu.divination.totemic.items.ItemSealNote;
import com.pengu.divination.totemic.seals.core.TotemicSeal;
import com.pengu.hammercore.client.render.item.iItemRender;
import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.client.utils.UtilsFX;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;

public class ItemRenderSealNote implements iItemRender
{
	@Override
	public void renderItem(ItemStack item)
	{
		GL11.glPushMatrix();
		GL11.glTranslated(.5, .5, .5);
		GL11.glScaled(1.25, 1.25, 1.25);
		GL11.glTranslated(-.5, -.5, -.5);
		
		GL11.glRotated(180, 0, 1, 0);
		GL11.glTranslated(-2, 0, -1);
		
		renderSeal(ItemSealNote.getSealOnItem(item));
		
		GL11.glPopMatrix();
	}
	
	public static void renderSeal(TotemicSeal seal)
	{
		if(seal == null)
			return;
		
		int tex = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
		UtilsFX.bindTexture(InfoDC.MOD_ID, seal.tex);
		
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		RenderHelper.disableStandardItemLighting();
		
		float dim = .3F;
		
		GL11.glBlendFunc(770, 771);
		GlStateManager.color(1, 1, 1, 1);
		GL11.glPushMatrix();
		GL11.glTranslated(1 + 5 / 16D, 7 / 16D, .4745);
		GL11.glScaled(dim / 256D, dim / 256D, dim / 256D);
		RenderUtil.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
		GL11.glPopMatrix();
		GL11.glBlendFunc(770, 771);
		RenderHelper.enableStandardItemLighting();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
//		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}
}