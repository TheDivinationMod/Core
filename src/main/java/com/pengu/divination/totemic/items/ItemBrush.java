package com.pengu.divination.totemic.items;

import java.util.List;

import com.pengu.divination.api.items.ItemResearchable;
import com.pengu.divination.totemic.init.BlocksDT;
import com.pengu.divination.totemic.seals.core.EnumSealColor;
import com.pengu.divination.totemic.tiles.TileTotemicSeal;
import com.pengu.hammercore.common.utils.SoundUtil;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.raytracer.RayTracer;
import com.pengu.hammercore.utils.ColorNamePicker;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBrush extends ItemResearchable
{
	public ItemBrush()
	{
		setUnlocalizedName("totemic/brush");
		setMaxStackSize(1);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 30;
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		if(!worldIn.isRemote)
			SoundUtil.playSoundEffect(worldIn, "item.bucket.fill", entityLiving.getPosition(), .5F, 1.5F, SoundCategory.PLAYERS);
		
		return new ItemStack(this);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		EnumSealColor col = null;
		
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt != null && nbt.hasKey("Color", NBT.TAG_INT))
			col = EnumSealColor.values()[nbt.getInteger("Color") % EnumSealColor.values().length];
		
		if(col != null)
			tooltip.add(TextFormatting.GRAY + I18n.format("divination.color") + ": " + I18n.format(ColorNamePicker.getColorNameFromHex(col.getColor())));
	}
	
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase playerIn, int count)
	{
		if(playerIn instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) playerIn;
			
			Vec3d headVec = RayTracer.getCorrectedHeadVec(player);
			double reach = RayTracer.getBlockReachDistance(player) / 2;
			Vec3d lookVec = player.getLook(1);
			Vec3d endVec = headVec.addVector(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
			
			RayTraceResult res = player.world.rayTraceBlocks(headVec, endVec, true, false, true);
			if(res != null && res.typeOfHit == Type.BLOCK)
			{
				Block b = player.world.getBlockState(res.getBlockPos()).getBlock();
				if(b == Blocks.WATER || b == Blocks.FLOWING_WATER)
					return;
			}
		}
		
		playerIn.stopActiveHand();
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BOW;
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if(isInCreativeTab(tab))
		{
			items.add(withColor(null));
			for(EnumSealColor sc : EnumSealColor.values())
				items.add(withColor(sc));
		}
	}
	
	public ItemStack withColor(EnumSealColor col)
	{
		ItemStack stack = new ItemStack(this);
		if(col != null)
		{
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("Color", col.ordinal());
		}
		return stack;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		EnumSealColor col = null;
		
		NBTTagCompound nbt = player.getHeldItem(hand).getTagCompound();
		if(nbt != null && nbt.hasKey("Color", NBT.TAG_INT))
			col = EnumSealColor.values()[nbt.getInteger("Color") % EnumSealColor.values().length];
		
		if(col != null && facing == EnumFacing.UP && worldIn.getBlockState(pos).isSideSolid(worldIn, pos, facing))
		{
			BlockPos p = pos.up();
			TileTotemicSeal s = WorldUtil.cast(worldIn.getTileEntity(p), TileTotemicSeal.class);
			
			if(s == null && worldIn.isAirBlock(p))
			{
				worldIn.setBlockState(p, BlocksDT.SEAL.getDefaultState());
				s = WorldUtil.cast(worldIn.getTileEntity(p), TileTotemicSeal.class);
			}
			
			if(s != null)
			{
				float hx = hitX * 16;
				float hz = hitZ * 16;
				
				if(s.paint((int) hx, (int) hz, col))
					HCNetwork.swingArm(player, hand);
			}
		}
		
		return EnumActionResult.PASS;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		EnumSealColor col = null;
		
		NBTTagCompound nbt = playerIn.getHeldItem(handIn).getTagCompound();
		if(nbt != null && nbt.hasKey("Color", NBT.TAG_INT))
			col = EnumSealColor.values()[nbt.getInteger("Color") % EnumSealColor.values().length];
		
		if(col == null)
			return super.onItemRightClick(worldIn, playerIn, handIn);
		
		Vec3d headVec = RayTracer.getCorrectedHeadVec(playerIn);
		double reach = RayTracer.getBlockReachDistance(playerIn) / 2;
		Vec3d lookVec = playerIn.getLook(1);
		Vec3d endVec = headVec.addVector(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
		
		RayTraceResult res = playerIn.world.rayTraceBlocks(headVec, endVec, true, false, true);
		if(res != null && res.typeOfHit == Type.BLOCK)
		{
			Block b = worldIn.getBlockState(res.getBlockPos()).getBlock();
			if(b == Blocks.WATER || b == Blocks.FLOWING_WATER)
				playerIn.setActiveHand(handIn);
		}
		
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}