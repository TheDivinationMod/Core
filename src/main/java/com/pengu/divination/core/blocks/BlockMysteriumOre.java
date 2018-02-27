package com.pengu.divination.core.blocks;

import java.util.Random;

import com.pengu.divination.core.init.ItemsDC;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class BlockMysteriumOre extends Block
{
	public BlockMysteriumOre()
	{
		super(Material.ROCK);
		setUnlocalizedName("mysterium_ore");
		setHardness(2F);
		setHarvestLevel("pickaxe", 2);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return ItemsDC.MYSTERIUM_CLUSTER;
	}
	
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		return 1 + random.nextInt(4 + Math.min(fortune, 10));
	}
}