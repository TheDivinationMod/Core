package com.pengu.divination.proxy;

import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerX;
import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerY;
import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerZ;

import java.lang.reflect.Field;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.pengu.divination.Divination;
import com.pengu.divination.core.client.gui.GuiNotes;
import com.pengu.divination.core.constants.InfoDC;
import com.pengu.divination.totemic.items.ItemBrush;
import com.pengu.divination.totemic.tiles.TileTotemicSeal;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.musiclayer.api.GetMusicEvent;
import com.pengu.musiclayer.api.MusicLayer;
import com.pengu.musiclayer.api.UpdateAlternativeMusicEvent;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Client extends Common
{
	private String oneTimeMusic;
	
	private static final TextFormatting[] RAINBOW_COLORS = new TextFormatting[7];
	
	static
	{
		RAINBOW_COLORS[0] = TextFormatting.RED;
		RAINBOW_COLORS[1] = TextFormatting.GOLD;
		RAINBOW_COLORS[2] = TextFormatting.YELLOW;
		RAINBOW_COLORS[3] = TextFormatting.GREEN;
		RAINBOW_COLORS[4] = TextFormatting.LIGHT_PURPLE;
		RAINBOW_COLORS[5] = TextFormatting.DARK_PURPLE;
		RAINBOW_COLORS[6] = TextFormatting.LIGHT_PURPLE;
	}
	
	@Override
	public void preInit()
	{
		MusicLayer.MUSIC_BUS.register(this);
	}
	
	@Override
	public void init()
	{
	}
	
	@Override
	public void playOnce(String url)
	{
		oneTimeMusic = url;
	}
	
	@Override
	public void stopCurrentMusic()
	{
		oneTimeMusic = null;
	}
	
	@SubscribeEvent
	public void getMusic(GetMusicEvent e)
	{
		if(oneTimeMusic != null)
			e.setSound(oneTimeMusic);
	}
	
	@SubscribeEvent
	public void updAlts(UpdateAlternativeMusicEvent e)
	{
		oneTimeMusic = null;
	}
	
	@Override
	public void openNotes(String text)
	{
		Minecraft.getMinecraft().displayGuiScreen(new GuiNotes(text));
	}
	
	@Override
	protected EffectManager createFX()
	{
		return new ClientEffectManager();
	}
	
	public final String MODID_JEI = TextFormatting.BLUE + "" + TextFormatting.ITALIC + InfoDC.MOD_NAME;
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void addTooltip(ItemTooltipEvent e)
	{
		List<String> tooltip = e.getToolTip();
		
		for(int i = 0; i < tooltip.size(); ++i)
		{
			String s = tooltip.get(i);
			
			if(s.contains(InfoDC.MOD_NAME))
			{
				String mod = Divination.instance.getModuleName(e.getItemStack().getItem());
				mod = mod == null || mod.isEmpty() ? "?" : I18n.format("divination_module." + mod.toLowerCase());
				
				if(mod.length() > 1)
					tooltip.set(i, s + " (" + mod + ")");
				else
					break;
			}
		}
	}
	
	@SubscribeEvent
	public void renderWorldLast(RenderWorldLastEvent e)
	{
		ItemStack heldMain = Minecraft.getMinecraft().player.getHeldItemMainhand();
		ItemStack heldOff = Minecraft.getMinecraft().player.getHeldItemOffhand();
		RayTraceResult r = Minecraft.getMinecraft().objectMouseOver;
		
		boolean brush = false;
		
		if(!heldMain.isEmpty() && heldMain.getItem() instanceof ItemBrush)
			brush = true;
		if(!heldOff.isEmpty() && heldOff.getItem() instanceof ItemBrush)
			brush = true;
		
		if(brush && r != null && r.typeOfHit == Type.BLOCK && r.sideHit == EnumFacing.UP)
		{
			BlockPos p = r.getBlockPos();
			World w = Minecraft.getMinecraft().world;
			
			TileTotemicSeal se = WorldUtil.cast(w.getTileEntity(p), TileTotemicSeal.class);
			
			if(w.getBlockState(p).isSideSolid(w, p, EnumFacing.UP) || se != null)
			{
				GL11.glPushMatrix();
				
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				GlStateManager.glLineWidth(2.0F);
				GlStateManager.disableTexture2D();
				GlStateManager.depthMask(false);
				
				float off = se != null ? 0.001F : 1.001F;
				GL11.glTranslated(p.getX() - staticPlayerX, p.getY() - staticPlayerY + off, p.getZ() - staticPlayerZ);
				GL11.glColor4f(0, 0, 0, .4F);
				for(int x = 1; x < 16; ++x)
					drawLine(0, 0, x / 16F, 1, 0, x / 16F);
				for(int z = 1; z < 16; ++z)
					drawLine(z / 16F, 0, 0, z / 16F, 0, 1);
				
				int hx = (int) ((r.hitVec.x % 1) * 16);
				int hz = (int) ((r.hitVec.z % 1) * 16);
				
				{
					if(r.hitVec.x < 0)
						hx--;
					if(r.hitVec.z < 0)
						hz--;
					
					float d11 = hx / 16F + (r.hitVec.x < 0 ? 1 : 0);
					float d12 = (hx + 1) / 16F + (r.hitVec.x < 0 ? 1 : 0);
					float d14 = hz / 16F + (r.hitVec.z < 0 ? 1 : 0);
					float d15 = (hz + 1) / 16F + (r.hitVec.z < 0 ? 1 : 0);
					
					GL11.glBegin(GL11.GL_QUADS);
					GL11.glVertex3f(d12, 0, d15);
					GL11.glVertex3f(d12, 0, d14);
					GL11.glVertex3f(d11, 0, d14);
					GL11.glVertex3f(d11, 0, d15);
					GL11.glEnd();
				}
				
				GL11.glColor4f(1, 1, 1, 1);
				
				GlStateManager.depthMask(true);
				GlStateManager.enableTexture2D();
				GlStateManager.disableBlend();
				
				GL11.glPopMatrix();
			}
		}
	}
	
	private void drawLine(float x0, float y0, float z0, float x1, float y1, float z1)
	{
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3f(x0, y0, z0);
		GL11.glVertex3f(x1, y1, z1);
		GL11.glEnd();
	}
	
	@SubscribeEvent
	public void initGui(TextureStitchEvent.Post e)
	{
		Divination.modMeta.description = getFormattedDesc();
	}
	
	public static String getFormattedDesc()
	{
		String desc = Divination.modDescOrigin.replaceAll("<modname>", InfoDC.MOD_NAME);
		
		int in;
		while((in = desc.indexOf("<i18n:")) != -1)
		{
			int end = desc.indexOf('>', in);
			if(end == -1)
				break;
			String tag = desc.substring(in, end + 1);
			String[] sub = desc.substring(in + 1, end).split(":", 3);
			String fin = "";
			
			if(sub[1].equalsIgnoreCase("block") || sub[1].equalsIgnoreCase("tile"))
			{
				Block bl = GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(sub[2]));
				if(bl != null)
					fin = bl.getLocalizedName();
			} else if(sub[1].equalsIgnoreCase("item"))
			{
				Item it = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation(sub[2]));
				if(it != null)
					fin = it.getItemStackDisplayName(it.getDefaultInstance());
			} else if(sub[1].equalsIgnoreCase("custom"))
				fin = I18n.format(sub[2]);
			
			desc = desc.replaceAll(tag, fin);
		}
		
		while((in = desc.indexOf("<rainbow>")) != -1)
		{
			String en = "</rainbow>";
			int end = desc.indexOf(en, in);
			
			if(end == -1)
				break;
			
			String tag = desc.substring(in, end + en.length());
			String text = rainbow(desc.substring(in + 9, end));
			
			desc = desc.replaceAll(tag, text);
		}
		
		return desc;
	}
	
	public static String rainbow(String text)
	{
		StringBuilder b = new StringBuilder();
		char[] chars = text.toCharArray();
		for(int i = 0; i < chars.length; ++i)
			b.append(RAINBOW_COLORS[i % RAINBOW_COLORS.length].toString() + chars[i]);
		return b.toString();
	}
}