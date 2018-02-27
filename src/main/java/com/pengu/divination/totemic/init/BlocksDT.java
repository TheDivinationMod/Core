package com.pengu.divination.totemic.init;

import com.pengu.divination.totemic.blocks.BlockDungeonBrick;
import com.pengu.divination.totemic.blocks.BlockDungeonSlab;
import com.pengu.divination.totemic.blocks.BlockDungeonStair;
import com.pengu.divination.totemic.blocks.BlockTotemicSeal;

public class BlocksDT
{
	public static final BlockTotemicSeal SEAL = new BlockTotemicSeal();
	public static final BlockDungeonBrick DUNGEON_BRICK = new BlockDungeonBrick();
	public static final BlockDungeonBrick DUNGEON_BRICK_CRACKED = new BlockDungeonBrick("_cracked");
	public static final BlockDungeonBrick DUNGEON_BRICK_MOSSY = new BlockDungeonBrick("_mossy");
	public static final BlockDungeonStair DUNGEON_STAIRS = new BlockDungeonStair();
	public static final BlockDungeonStair DUNGEON_STAIRS_CRACKED = new BlockDungeonStair("_cracked", DUNGEON_BRICK_CRACKED);
	public static final BlockDungeonStair DUNGEON_STAIRS_MOSSY = new BlockDungeonStair("_mossy", DUNGEON_BRICK_MOSSY);
	
	public static final BlockDungeonSlab DUNGEON_SLAB = new BlockDungeonSlab();
	public static final BlockDungeonSlab DUNGEON_SLAB_CRACKED = new BlockDungeonSlab("_cracked");
	public static final BlockDungeonSlab DUNGEON_SLAB_MOSSY = new BlockDungeonSlab("_mossy");
}