package com.pengu.divination.totemic.client.tesr;

import com.pengu.divination.core.constants.InfoDC;
import com.pengu.divination.totemic.init.BlocksDT;
import com.pengu.divination.totemic.seals.core.EnumSealColor;
import com.pengu.divination.totemic.seals.core.TSealPoint;
import com.pengu.divination.totemic.tiles.TileTotemicSeal;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.vec.Cuboid6;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

public class TESRTotemicSeal extends TESR<TileTotemicSeal>
{
	@Override
	public void renderTileEntityAt(TileTotemicSeal te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		RenderBlocks rb = RenderBlocks.forMod(InfoDC.MOD_ID);
		int b = getBrightnessForRB(te, rb);
		
		TextureAtlasSprite sprite = mc.getTextureMapBlocks().getAtlasSprite(InfoDC.MOD_ID + ":items/face");
		rb.simpleRenderer.begin();
		
		for(int i = 0; i < te.points.size(); ++i)
		{
			TSealPoint p = te.points.get(i);
			EnumSealColor c = p.color;
			rb.setRenderBounds(p.x / 16D, 0, p.y / 16D, (p.x + 1) / 16D, .05 / 16, (p.y + 1) / 16D);
			rb.renderFaceYPos(x, y, z, sprite, c.getR(), c.getG(), c.getB(), b);
		}
		
		rb.simpleRenderer.end();
	}
}