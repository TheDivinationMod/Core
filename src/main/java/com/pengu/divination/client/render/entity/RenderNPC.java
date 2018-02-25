package com.pengu.divination.client.render.entity;

import com.pengu.divination.core.entity.npc.EntityNPC;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderNPC extends RenderLivingBase<EntityNPC>
{
	public static final Factory FACTORY = new Factory();
	
	public RenderNPC(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelPlayer(0F, false), .5F);
		addLayer(new LayerBipedArmor(this));
		addLayer(new LayerHeldItem(this));
		addLayer(new LayerArrow(this));
		addLayer(new LayerCustomHead(getMainModel().bipedHead));
		addLayer(new LayerElytra(this));
	}
	
	public ModelPlayer getMainModel()
	{
		return (ModelPlayer) super.getMainModel();
	}
	
	@Override
	public void doRender(EntityNPC entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		double d0 = y;
		if(entity.isSneaking())
			d0 = y - 0.125D;
		GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
		super.doRender(entity, x, d0, z, entityYaw, partialTicks);
		GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityNPC entity)
	{
		return entity.getSkin();
	}
	
	protected void preRenderCallback(EntityNPC entitylivingbaseIn, float partialTickTime)
	{
		float f = 0.9375F;
		GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
	}
	
	protected void renderEntityName(EntityNPC entityIn, double x, double y, double z, String name, double distanceSq)
	{
//		renderLivingLabel(entityIn, "Shaman", x, y, z, 64);
	}
	
	public static class Factory implements IRenderFactory<EntityNPC>
	{
		@Override
		public Render<? super EntityNPC> createRenderFor(RenderManager manager)
		{
			return new RenderNPC(manager);
		}
	}
}