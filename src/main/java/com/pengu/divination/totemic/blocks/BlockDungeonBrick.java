package com.pengu.divination.totemic.blocks;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.pengu.hammercore.HammerCore;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDungeonBrick extends Block
{
	public static final List<String> authors = ImmutableList.copyOf(HammerCore.getHCAuthorsArray());
	
	public BlockDungeonBrick()
	{
		super(Material.ROCK);
		setUnlocalizedName("totemic/dungeon_brick");
		setHardness(1F);
		setHarvestLevel("pickaxe", 1);
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos)
	{
		if(authors.contains(player.getGameProfile().getName()))
			return super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
		return 0;
	}
}