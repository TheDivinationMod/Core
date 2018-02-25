package com.pengu.divination.totemic.blocks;

import java.util.Random;

import com.pengu.divination.totemic.seals.core.TSealPoint;
import com.pengu.divination.totemic.tiles.TileTotemicSeal;
import com.pengu.hammercore.api.iTileBlock;
import com.pengu.hammercore.api.mhb.BlockTraceable;
import com.pengu.hammercore.api.mhb.iCubeManager;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.vec.Cuboid6;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTotemicSeal extends BlockTraceable implements iCubeManager, iTileBlock<TileTotemicSeal>, ITileEntityProvider
{
	public BlockTotemicSeal()
	{
		super(Material.PLANTS);
		setSoundType(SoundType.STONE);
		setUnlocalizedName("totemic/seal");
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
	}
	
	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public Class<TileTotemicSeal> getTileClass()
	{
		return TileTotemicSeal.class;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileTotemicSeal();
	}
	
	@Override
	public Cuboid6[] getCuboids(World world, BlockPos pos, IBlockState state)
	{
		TileTotemicSeal seal = WorldUtil.cast(world.getTileEntity(pos), TileTotemicSeal.class);
		if(seal != null)
		{
			Cuboid6[] cbs = new Cuboid6[seal.points.size()];
			for(int i = 0; i < cbs.length; ++i)
			{
				TSealPoint p = seal.points.get(i);
				cbs[i] = new Cuboid6(p.x / 16D, 0, p.y / 16D, (p.x + 1) / 16D, .05 / 16, (p.y + 1) / 16D);
			}
			return cbs;
		}
		return new Cuboid6[0];
	}
}