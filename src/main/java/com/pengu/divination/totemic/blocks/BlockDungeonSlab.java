package com.pengu.divination.totemic.blocks;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.api.mhb.BlockTraceable;
import com.pengu.hammercore.api.mhb.iCubeManager;
import com.pengu.hammercore.common.blocks.iItemBlock;
import com.pengu.hammercore.vec.Cuboid6;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDungeonSlab extends BlockTraceable implements iCubeManager, iItemBlock
{
	public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 2);
	public static final List<String> authors = ImmutableList.copyOf(HammerCore.getHCAuthorsArray());
	public boolean isDouble;
	
	public BlockDungeonSlab()
	{
		this("");
	}
	
	public BlockDungeonSlab(String sub)
	{
		super(Material.ROCK);
		setUnlocalizedName("totemic/dungeon_slab" + sub);
		setHardness(1F);
		setHarvestLevel("pickaxe", 1);
		fullBlock = false;
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos)
	{
		if(authors.contains(player.getGameProfile().getName()))
			return super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
		return 0;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(TYPE);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(TYPE, meta);
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return getMetaFromState(state) == 2;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return getMetaFromState(state) == 2;
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return getStateFromMeta(facing == EnumFacing.UP ? 0 : facing == EnumFacing.DOWN ? 1 : hitY >= 0.5 ? 1 : 0);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, TYPE);
	}
	
	@Override
	public Cuboid6[] getCuboids(World world, BlockPos pos, IBlockState state)
	{
		int var = getMetaFromState(state);
		if(var == 0)
			return new Cuboid6[] { new Cuboid6(0, 0, 0, 1, .5, 1) };
		if(var == 1)
			return new Cuboid6[] { new Cuboid6(0, .5, 0, 1, 1, 1) };
		return new Cuboid6[] { new Cuboid6(0, 0, 0, 1, .5, 1), new Cuboid6(0, .5, 0, 1, 1, 1) };
	}
	
	public final ItemBlockDungeonSlab itemBlock = new ItemBlockDungeonSlab(this);
	
	@Override
	public ItemBlock getItemBlock()
	{
		return itemBlock;
	}
	
	private static final class ItemBlockDungeonSlab extends ItemBlock
	{
		public ItemBlockDungeonSlab(Block block)
		{
			super(block);
		}
		
		@Override
		public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
		{
			ItemStack itemstack = player.getHeldItem(hand);
			IBlockState st = worldIn.getBlockState(pos);
			
			if(!player.isSneaking() && st.getBlock() == block)
			{
				int meta = st.getBlock().getMetaFromState(st);
				if((meta == 0 && facing == EnumFacing.UP) || (meta == 1 && facing == EnumFacing.DOWN))
				{
					worldIn.setBlockState(pos, st = block.getStateFromMeta(2));
					SoundType soundtype = block.getSoundType(st, worldIn, pos, player);
					worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
					itemstack.shrink(1);
					return EnumActionResult.SUCCESS;
				}
			}
			
			Block block = st.getBlock();
			
			if(!block.isReplaceable(worldIn, pos))
				pos = pos.offset(facing);
			
			st = worldIn.getBlockState(pos);
			if(st.getBlock() == this.block)
			{
				System.out.println("HI");
			} else if(!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack) && worldIn.mayPlace(this.block, pos, false, facing, (Entity) null))
			{
				int i = facing == EnumFacing.UP ? 0 : facing == EnumFacing.DOWN ? 1 : getMetadata(itemstack.getMetadata());
				IBlockState iblockstate1 = this.block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player, hand);
				
				if(placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1))
				{
					iblockstate1 = worldIn.getBlockState(pos);
					SoundType soundtype = iblockstate1.getBlock().getSoundType(iblockstate1, worldIn, pos, player);
					worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
					itemstack.shrink(1);
				}
				
				return EnumActionResult.SUCCESS;
			}
			return EnumActionResult.FAIL;
		}
	}
}