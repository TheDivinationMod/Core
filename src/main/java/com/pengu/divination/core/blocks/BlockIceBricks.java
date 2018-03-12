package com.pengu.divination.core.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockIceBricks extends Block
{
	public BlockIceBricks()
	{
		super(Material.ROCK);
		setUnlocalizedName("ice_bricks");
		setSoundType(SoundType.GLASS);
		setHardness(.5F);
	}
}