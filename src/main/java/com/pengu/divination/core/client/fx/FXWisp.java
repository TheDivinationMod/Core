package com.pengu.divination.core.client.fx;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.GLRenderState;
import com.pengu.hammercore.client.particle.api.SimpleParticle;
import com.pengu.hammercore.net.pkt.thunder.Thunder;
import com.pengu.hammercore.utils.ColorHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FXWisp extends SimpleParticle
{
	static final ResourceLocation p_large = new ResourceLocation("hammercore", "textures/misc/p_large.png");
	public boolean shrink = false;
	float moteParticleScale;
	int moteHalfLife;
	public boolean tinkle = false;
	
	public int blendmode = 1;
	
	public FXWisp(World world, double x, double y, double z, double tx, double ty, double tz, float partialTicks, Thunder.Layer type)
	{
		this(world, x, y, z, partialTicks, type);
		double dx = tx - posX;
		double dy2 = ty - posY;
		double dz = tz - posZ;
		motionX = dx / particleMaxAge;
		motionY = dy2 / particleMaxAge;
		motionZ = dz / particleMaxAge;
	}
	
	public FXWisp(World world, double d, double d1, double d2, float partialTicks, float rotationX, float rotationZ)
	{
		this(world, d, d1, d2, 1.0f, partialTicks, rotationX, rotationZ);
	}
	
	public FXWisp(World world, double d, double d1, double d2, float partialTicks, float rotationX, float rotationZ, float rotationYZ)
	{
		super(world, d, d1, d2, 0.0, 0.0, 0.0);
		if(rotationX == 0F)
			rotationX = 1F;
		particleRed = rotationX;
		particleGreen = rotationZ;
		particleBlue = rotationYZ;
		particleGravity = 0.0f;
		motionX = 0.0;
		motionY = 0.0;
		motionZ = 0.0;
		particleScale *= partialTicks;
		moteParticleScale = particleScale;
		particleMaxAge = (int) (36.0 / (Math.random() * 0.3 + 0.7));
		moteHalfLife = particleMaxAge / 2;
	}
	
	public FXWisp(World world, double d, double d1, double d2, float partialTicks, Thunder.Layer type)
	{
		this(world, d, d1, d2, partialTicks, 0.0f, 0.0f, 0.0f);
		blendmode = type.blendFunc;
		particleRed = ColorHelper.getRed(type.color);
		particleGreen = ColorHelper.getGreen(type.color);
		particleBlue = ColorHelper.getBlue(type.color);
	}
	
	@Override
	public void doRenderParticle(double x, double y, double z, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		EntityPlayer entityIn = Minecraft.getMinecraft().player;
		ActiveRenderInfo.updateRenderInfo(entityIn, Minecraft.getMinecraft().gameSettings.thirdPersonView == 2);
		
		Particle.interpPosX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
		Particle.interpPosY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
		Particle.interpPosZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
		Particle.cameraViewDir = entityIn.getLook(partialTicks);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.alphaFunc(516, 0.003921569F);
		
		float agescale = 0.0f;
		if(shrink)
			agescale = ((float) particleMaxAge - (float) particleAge) / particleMaxAge;
		else
		{
			agescale = (float) particleAge / (float) moteHalfLife;
			if(agescale > 1.0f)
				agescale = 2.0f - agescale;
		}
		particleScale = moteParticleScale * agescale;
		
		GL11.glPushMatrix();
		GL11.glDepthMask(false);
		GLRenderState.BLEND.captureState();
		GLRenderState.BLEND.on();
		GL11.glBlendFunc(770, blendmode);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(p_large);
		
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		
		buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		particleAlpha = 0.5F;
		
		float f = particleTextureIndexX / 16.0F;
		float f1 = f + 0.0624375F;
		float f2 = particleTextureIndexY / 16.0F;
		float f3 = f2 + 0.0624375F;
		float f4 = 0.1F * particleScale;
		
		f = 0;
		f1 = 1;
		f2 = 0;
		f3 = 1;
		
		float f5 = (float) (prevPosX + (posX - prevPosX) * partialTicks - interpPosX);
		float f6 = (float) (prevPosY + (posY - prevPosY) * partialTicks - interpPosY);
		float f7 = (float) (prevPosZ + (posZ - prevPosZ) * partialTicks - interpPosZ);
		int i = 255;
		int j = i >> 16 & 65535;
		int k = i & 65535;
		Vec3d[] avec3d = new Vec3d[] { new Vec3d(-rotationX * f4 - rotationXY * f4, -rotationZ * f4, -rotationYZ * f4 - rotationXZ * f4), new Vec3d(-rotationX * f4 + rotationXY * f4, rotationZ * f4, -rotationYZ * f4 + rotationXZ * f4), new Vec3d(rotationX * f4 + rotationXY * f4, rotationZ * f4, rotationYZ * f4 + rotationXZ * f4), new Vec3d(rotationX * f4 - rotationXY * f4, -rotationZ * f4, rotationYZ * f4 - rotationXZ * f4) };
		
		if(particleAngle != 0.0F)
		{
			float f8 = particleAngle + (particleAngle - prevParticleAngle) * partialTicks;
			float f9 = MathHelper.cos(f8 * 0.5F);
			float f10 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.x;
			float f11 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.y;
			float f12 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.z;
			Vec3d vec3d = new Vec3d(f10, f11, f12);
			
			for(int l = 0; l < 4; ++l)
				avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale(f9 * f9 - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale(2.0F * f9));
		}
		
		buffer.pos(f5 + avec3d[0].x, f6 + avec3d[0].y, f7 + avec3d[0].z).tex(f1, f3).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
		buffer.pos(f5 + avec3d[1].x, f6 + avec3d[1].y, f7 + avec3d[1].z).tex(f1, f2).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
		buffer.pos(f5 + avec3d[2].x, f6 + avec3d[2].y, f7 + avec3d[2].z).tex(f, f2).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
		buffer.pos(f5 + avec3d[3].x, f6 + avec3d[3].y, f7 + avec3d[3].z).tex(f, f3).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
		
		Tessellator.getInstance().draw();
		
		GLRenderState.BLEND.reset();
		GL11.glDepthMask(true);
		GL11.glPopMatrix();
		GL11.glBlendFunc(770, 771);
	}
	
	@Override
	public void onUpdate()
	{
		Entity renderentity = Minecraft.getMinecraft().getRenderViewEntity();
		double visibleDistance = 50;
		
		if(Minecraft.getMinecraft().gameSettings.particleSetting > 0)
			visibleDistance = 25;
		if(Minecraft.getMinecraft().gameSettings.particleSetting > 1)
			visibleDistance = 5;
		
		if(renderentity.getDistance(posX, posY, posZ) > visibleDistance)
		{
			setExpired();
			return;
		}
		
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		
		if(particleAge == 0 && tinkle && rand.nextInt(3) == 0)
			world.playSound(posX, posY, posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, .003F, .7F * ((rand.nextFloat() - rand.nextFloat()) * 0.6f + 2.0f), false);
		
		if(particleAge++ >= particleMaxAge)
			setExpired();
		
		motionY -= 0.04 * particleGravity;
		
		move(motionX, motionY, motionZ);
		
		motionX *= 0.9800000190734863;
		motionY *= 0.9800000190734863;
		motionZ *= 0.9800000190734863;
		
		if(onGround)
		{
			motionX *= 0.699999988079071;
			motionZ *= 0.699999988079071;
		}
	}
	
	public FXWisp setColor(int color)
	{
		particleRed = (color >> 16 & 255) / 255F;
		particleGreen = (color >> 8 & 255) / 255F;
		particleBlue = (color >> 0 & 255) / 255F;
		
		return this;
	}
	
	public void setGravity(float val)
	{
		particleGravity = val;
	}
}