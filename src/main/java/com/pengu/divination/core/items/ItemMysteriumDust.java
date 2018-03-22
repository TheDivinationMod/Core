package com.pengu.divination.core.items;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import com.endie.lib.tuple.TwoTuple;
import com.pengu.divination.api.items.ItemResearchable;
import com.pengu.divination.core.data.ResearchSystem;
import com.pengu.divination.core.init.BlocksDC;
import com.pengu.divination.core.init.ItemsDC;
import com.pengu.divination.core.init.SoundsDC;
import com.pengu.divination.core.proc.ProcessTransformBlock;
import com.pengu.divination.core.proc.ProcessTransformItem;
import com.pengu.divination.proxy.EffectManager;
import com.pengu.hammercore.common.items.iCustomEnchantColorItem;
import com.pengu.hammercore.common.utils.SoundUtil;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.net.pkt.thunder.Thunder;
import com.pengu.hammercore.raytracer.RayTracer;
import com.pengu.hammercore.utils.ColorHelper;
import com.pengu.hammercore.utils.WorldLocation;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemMysteriumDust extends ItemResearchable implements iCustomEnchantColorItem
{
	public static final Map<Block, TwoTuple<Function<IBlockState, IBlockState>, String>> WORLD_TRANSMUTATION = new HashMap<>();
	public static final Map<Item, TwoTuple<Item, String>> ITEM_TRANSMUTATION = new HashMap<>();
	
	static
	{
		transmute(null, Items.BOOK, ItemsDC.MYSTERIUM_BOOK);
		transmute(null, Blocks.CRAFTING_TABLE, table -> Blocks.AIR.getDefaultState());
		transmute(null, new Block[] { Blocks.FURNACE, Blocks.LIT_FURNACE }, furn -> Blocks.AIR.getDefaultState());
		transmute(null, Blocks.LAPIS_ORE, ore -> BlocksDC.MYSTERIUM_ORE.getDefaultState());
		transmute(null, Blocks.YELLOW_FLOWER, fl -> Blocks.RED_FLOWER.getStateFromMeta(0));
		transmute(null, Blocks.RED_FLOWER, fl -> fl.getBlock().getMetaFromState(fl) == 8 ? Blocks.YELLOW_FLOWER.getDefaultState() : Blocks.RED_FLOWER.getStateFromMeta(fl.getBlock().getMetaFromState(fl) + 1));
		transmute(null, Blocks.BROWN_MUSHROOM, ore -> Blocks.RED_MUSHROOM.getDefaultState());
		transmute(null, Blocks.RED_MUSHROOM, ore -> Blocks.BROWN_MUSHROOM.getDefaultState());
	}
	
	public static void transmute(String res, Item in, Item out)
	{
		ITEM_TRANSMUTATION.put(in, new TwoTuple<>(out, res));
	}
	
	public static void transmute(String res, Block in, Function<IBlockState, IBlockState> out)
	{
		WORLD_TRANSMUTATION.put(in, new TwoTuple<>(out, res));
	}
	
	public static void transmute(String res, Block[] ins, Function<IBlockState, IBlockState> out)
	{
		for(Block in : ins)
			transmute(res, in, out);
	}
	
	public ItemMysteriumDust()
	{
		setUnlocalizedName("mysterium_dust");
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		RayTraceResult res = RayTracer.retrace(playerIn);
		if(res != null && !worldIn.isRemote)
		{
			Vec3d v = res.hitVec;
			if(v != null)
				for(EntityItem ei : worldIn.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(v.x, v.y, v.z, v.x, v.y + .5, v.z).grow(.2)))
				{
					TwoTuple<Item, String> out = ITEM_TRANSMUTATION.get(ei.getItem().getItem());
					if(out.get2() == null || ResearchSystem.isResearchCompleted(playerIn, out.get2()))
						if(!ei.getItem().isEmpty() && ei.getItem().getCount() == 1 && ITEM_TRANSMUTATION.containsKey(ei.getItem().getItem()) && (!ei.getItem().hasTagCompound() || !ei.getItem().getTagCompound().hasKey("NonStackLeast")))
						{
							ei.getItem().setTagCompound(new NBTTagCompound());
							ei.getItem().getTagCompound().setUniqueId("NonStack", UUID.randomUUID());
							ei.setNoGravity(true);
							
							ResearchSystem.setResearchCompleted(playerIn, out.get1(), true);
							
							ProcessTransformItem p = new ProcessTransformItem();
							p.procEntity = ei.getEntityId();
							p.world = worldIn;
							p.start();
							
							HCNetwork.swingArm(playerIn, handIn);
							
							blow(playerIn, ei.getPositionVector(), .4);
							
							playerIn.getHeldItem(handIn).shrink(1);
							
							break;
						}
				}
			
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	public void blow(EntityPlayer playerIn, Vec3d end, double fuzz)
	{
		World worldIn = playerIn.world;
		
		Vec3d look = playerIn.getLook(1).scale(.25);
		Vec3d start = playerIn.getPositionVector().addVector(0, playerIn.getEyeHeight(), 0).add(look);
		
		int j = 32 + worldIn.rand.nextInt(32);
		
		for(int i = 0; i < j; ++i)
		{
			Vec3d ps = start.addVector((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * fuzz, (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * fuzz, (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * fuzz);
			end = end.add(look);
			
			EffectManager.fx().wisp(worldIn, ps, end, 1F, new Thunder.Layer(771, worldIn.rand.nextInt(9) == 0 ? 0xCC00FF : 0x2222FF, true));
		}
		
		// if(playerIn instanceof EntityPlayerMP)
		// HCNetwork.manager.sendTo(new
		// PacketStartMusic(MusicURLs.MAGICAL_PORTAL_OPEN), (EntityPlayerMP)
		// playerIn);
		
		SoundUtil.playSoundEffect(worldIn, SoundsDC.DUST.name.toString(), end.x, end.y, end.z, .5F, 1F, SoundCategory.BLOCKS);
	}
	
	@SubscribeEvent
	public void interact(PlayerInteractEvent e)
	{
		ItemStack it = e.getItemStack();
		
		if(!it.isEmpty() && it.getItem() == this && !e.getWorld().isRemote)
		{
			WorldLocation loc = new WorldLocation(e.getWorld(), e.getPos());
			
			TwoTuple<Function<IBlockState, IBlockState>, String> out = WORLD_TRANSMUTATION.get(loc.getBlock());
			if(!e.getWorld().isRemote && out != null && e.getEntityPlayer().isSneaking() && (out.get2() == null || ResearchSystem.isResearchCompleted(e.getEntityPlayer(), out.get2())))
			{
				int dim = loc.getWorld().provider.getDimension();
				Set<Long> active = ProcessTransformBlock.WORLD_ACTIVE.get(dim);
				if(active == null)
					ProcessTransformBlock.WORLD_ACTIVE.put(dim, active = new HashSet<>());
				
				long pl = loc.getPos().toLong();
				if(!active.contains(pl) && active.add(pl))
				{
					ProcessTransformBlock p = new ProcessTransformBlock();
					p.loc = loc;
					p.start();
					
					AxisAlignedBB aabb = loc.getState().getBoundingBox(e.getWorld(), loc.getPos());
					Vec3d center = aabb != null ? aabb.getCenter() : new Vec3d(.5, .5, .5);
					blow(e.getEntityPlayer(), new Vec3d(loc.getPos()).add(center), .5);
					if(!e.getEntityPlayer().capabilities.isCreativeMode)
						e.getItemStack().shrink(1);
					e.setCanceled(true);
					HCNetwork.swingArm(e.getEntityPlayer(), e.getHand());
				}
			}
		}
	}
	
	@Override
	public int getEnchantEffectColor(ItemStack stack)
	{
		float f9 = (float) (System.currentTimeMillis() % 5000L / 1000D) * 5F;
		float l = (float) ((Math.sin(f9 + 0.0F) + 1.0F) * 0.3F);
		float j1 = (float) ((Math.sin(f9 + 4.1887903F) + 1.0F) * 0.1F);
		return 255 << 24 | ColorHelper.packRGB(j1 * .75F + .2F, .1F, l * .75F + .2F);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}
}