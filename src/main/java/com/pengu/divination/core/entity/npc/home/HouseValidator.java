package com.pengu.divination.core.entity.npc.home;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pengu.divination.core.entity.npc.EntityNPC;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class HouseValidator
{
	public static final short MAX_HOME_SIZE = 10000; // 10K blocks inside home
													 // is max, not to make
													 // everything lag out!
	
	public static boolean isValidHouseFromInside(World world, BlockPos pos, List<String> problems)
	{
		if(world.isBlockLoaded(pos))
		{
			Set<Long> visited = new HashSet<>();
			visited.add(pos.toLong());
			Set<Long> toVisit = new HashSet<>();
			
			House h = new House();
			
			int lastSize = visited.size();
			while(visited.size() < MAX_HOME_SIZE)
			{
				for(long lnpos : visited)
				{
					BlockPos npos = BlockPos.fromLong(lnpos);
					for(EnumFacing f : EnumFacing.VALUES)
					{
						BlockPos mpos = npos.offset(f);
						if(!world.isBlockLoaded(mpos))
							continue; // skip unloaded areas
						IBlockState state = world.getBlockState(mpos);
						if(state.getBlock() == Blocks.AIR)
							toVisit.add(mpos.toLong());
						else if(state.getBlock().getLightValue(state, world, mpos) > 0)
						{
							h.lights.add(mpos.toLong());
							toVisit.add(mpos.toLong());
						} else if(state.getBlock() == Blocks.TRAPDOOR || state.getBlock() == Blocks.ACACIA_DOOR || state.getBlock() == Blocks.BIRCH_DOOR || state.getBlock() == Blocks.DARK_OAK_DOOR || state.getBlock() == Blocks.JUNGLE_DOOR || state.getBlock() == Blocks.OAK_DOOR || state.getBlock() == Blocks.SPRUCE_DOOR)
						{
							h.exits.add(mpos.toLong());
						} else if(state.getBlock() == Blocks.CRAFTING_TABLE)
						{
							h.tables.add(mpos.toLong());
							toVisit.add(mpos.toLong());
						}
					}
				}
				
				visited.addAll(toVisit);
				toVisit.clear();
				
				if(visited.size() == lastSize) // House did not change - it was
											   // defined
				{
					if(h.exits.size() == 0)
						problems.add(TextFormatting.RED + "No doors found!");
					if(h.lights.size() == 0)
						problems.add(TextFormatting.RED + "No lights found!");
					if(h.tables.size() == 0)
						problems.add(TextFormatting.RED + "No tables found!");
					if(lastSize < 24)
						problems.add(TextFormatting.RED + "House it too small!");
					
					boolean valid = h.exits.size() > 0 && h.lights.size() > 0 && h.tables.size() > 0;
					boolean occupied = false;
					
					if(valid)
					{
						int minx = 0, miny = 0, minz = 0, maxx = 0, maxy = 0, maxz = 0;
						
						for(Long lnpos : visited)
						{
							BlockPos npos = BlockPos.fromLong(lnpos);
							minx = Math.min(minx, npos.getX());
							miny = Math.min(miny, npos.getY());
							minz = Math.min(minz, npos.getZ());
							maxx = Math.max(maxx, npos.getX());
							maxy = Math.max(maxy, npos.getY());
							maxz = Math.max(maxz, npos.getZ());
						}
						
						AxisAlignedBB aabb = new AxisAlignedBB(minx + .5, miny + .5, minz + .5, maxx + .5, maxy + .5, maxz + .5);
						
						for(EntityNPC npc : world.getEntitiesWithinAABB(EntityNPC.class, aabb))
							if(npc.isEntityAlive())
								occupied = true;
						
						if(occupied)
							problems.add(TextFormatting.RED + "This house is already occupied!");
					}
					
					return valid && !occupied;
				}
				
				lastSize = visited.size();
			}
			
			if(visited.size() > MAX_HOME_SIZE)
				problems.add(TextFormatting.RED + "Home size limit exceeded: " + visited.size() + "/" + MAX_HOME_SIZE);
			return visited.size() < MAX_HOME_SIZE;
		}
		
		return true;
	}
}