package com.pengu.divination.totemic.blocks;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.pengu.divination.totemic.init.BlocksDT;
import com.pengu.hammercore.HammerCore;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDungeonStair extends BlockStairs
{
	public static final List<String> authors = ImmutableList.copyOf(HammerCore.getHCAuthorsArray());
	
	public BlockDungeonStair()
	{
		this("", BlocksDT.DUNGEON_BRICK);
	}
	
	public BlockDungeonStair(String sub, Block bb)
	{
		super(bb.getDefaultState());
		setUnlocalizedName("totemic/dungeon_stairs" + sub);
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
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		fullBlock = false;
		return false;
	}
}