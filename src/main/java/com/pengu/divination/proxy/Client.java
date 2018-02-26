package com.pengu.divination.proxy;

import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerX;
import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerY;
import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerZ;

import org.lwjgl.opengl.GL11;

import com.pengu.divination.InfoDC;
import com.pengu.divination.totemic.items.ItemBrush;
import com.pengu.divination.totemic.tiles.TileTotemicSeal;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.musiclayer.api.GetMusicEvent;
import com.pengu.musiclayer.api.MusicLayer;
import com.pengu.musiclayer.api.UpdateAlternativeMusicEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Client extends Common
{
	private String oneTimeMusic;
	
	@Override
	public void preInit()
	{
		MusicLayer.MUSIC_BUS.register(this);
	}
	
	@Override
	public void init()
	{
	}
	
	@Override
	public void playOnce(String url)
	{
		oneTimeMusic = url;
	}
	
	@SubscribeEvent
	public void getMusic(GetMusicEvent e)
	{
		if(oneTimeMusic != null)
			e.setSound(oneTimeMusic);
	}
	
	@SubscribeEvent
	public void updAlts(UpdateAlternativeMusicEvent e)
	{
		oneTimeMusic = null;
	}
	
	@Override
	protected EffectManager createFX()
	{
		return new ClientEffectManager();
	}
	
	@SubscribeEvent
	public void renderWorldLast(RenderWorldLastEvent e)
	{
		ItemStack heldMain = Minecraft.getMinecraft().player.getHeldItemMainhand();
		ItemStack heldOff = Minecraft.getMinecraft().player.getHeldItemOffhand();
		RayTraceResult r = Minecraft.getMinecraft().objectMouseOver;
		
		boolean brush = false;
		
		if(!heldMain.isEmpty() && heldMain.getItem() instanceof ItemBrush)
			brush = true;
		if(!heldOff.isEmpty() && heldOff.getItem() instanceof ItemBrush)
			brush = true;
		
		if(brush && r != null && r.typeOfHit == Type.BLOCK && r.sideHit == EnumFacing.UP)
		{
			BlockPos p = r.getBlockPos();
			World w = Minecraft.getMinecraft().world;
			
			TileTotemicSeal se = WorldUtil.cast(w.getTileEntity(p), TileTotemicSeal.class);
			
			if(w.getBlockState(p).isSideSolid(w, p, EnumFacing.UP) || se != null)
			{
				GL11.glPushMatrix();
				
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				GlStateManager.glLineWidth(2.0F);
				GlStateManager.disableTexture2D();
				GlStateManager.depthMask(false);
				
				float off = se != null ? 0.001F : 1.001F;
				GL11.glTranslated(p.getX() - staticPlayerX, p.getY() - staticPlayerY + off, p.getZ() - staticPlayerZ);
				GL11.glColor4f(0, 0, 0, .4F);
				for(int x = 0; x < 16; ++x)
					drawLine(0, 0, x / 16F, 1, 0, x / 16F);
				for(int z = 0; z < 16; ++z)
					drawLine(z / 16F, 0, 0, z / 16F, 0, 1);
				
				int hx = (int) ((r.hitVec.x % 1) * 16);
				int hz = (int) ((r.hitVec.z % 1) * 16);
				
				{
					if(r.hitVec.x < 0)
						hx--;
					if(r.hitVec.z < 0)
						hz--;
					
					float d11 = hx / 16F + (r.hitVec.x < 0 ? 1 : 0);
					float d12 = (hx + 1) / 16F + (r.hitVec.x < 0 ? 1 : 0);
					float d14 = hz / 16F + (r.hitVec.z < 0 ? 1 : 0);
					float d15 = (hz + 1) / 16F + (r.hitVec.z < 0 ? 1 : 0);
					
					GL11.glBegin(GL11.GL_QUADS);
					GL11.glVertex3f(d12, 0, d15);
					GL11.glVertex3f(d12, 0, d14);
					GL11.glVertex3f(d11, 0, d14);
					GL11.glVertex3f(d11, 0, d15);
					GL11.glEnd();
				}
				
				GL11.glColor4f(1, 1, 1, 1);
				
				GlStateManager.depthMask(true);
				GlStateManager.enableTexture2D();
				GlStateManager.disableBlend();
				
				GL11.glPopMatrix();
			}
		}
	}
	
	private void drawLine(float x0, float y0, float z0, float x1, float y1, float z1)
	{
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3f(x0, y0, z0);
		GL11.glVertex3f(x1, y1, z1);
		GL11.glEnd();
	}
}