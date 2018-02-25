package com.pengu.divination.core.entity.npc;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class EntityNPC extends EntityCreature implements INpc
{
	public EntityNPC(World worldIn)
	{
		super(worldIn);
	}
	
	@Override
	protected void initEntityAI()
	{
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAIMoveIndoors(this));
		tasks.addTask(4, new EntityAIOpenDoor(this, true));
		tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
		tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3F, 1F));
		tasks.addTask(1, new EntityAIWanderAvoidWater(this, 0.3D));
		tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8F));
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(250D);
	}
	
	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		if(!world.isRemote)
			MinecraftForge.EVENT_BUS.post(new PlayerTalkToNpcEvent(player, this));
		return true;
	}
	
	public static final ResourceLocation STEVE = new ResourceLocation("minecraft", "textures/entity/steve.png");
	
	public ResourceLocation getSkin()
	{
		return STEVE;
	}
	
	@Override
	public boolean canBeAttackedWithItem()
	{
		return false;
	}
}