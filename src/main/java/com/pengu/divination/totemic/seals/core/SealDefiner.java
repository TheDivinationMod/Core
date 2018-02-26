package com.pengu.divination.totemic.seals.core;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import com.pengu.divination.Divination;
import com.pengu.divination.InfoDC;
import com.pengu.divination.totemic.tiles.TileTotemicSeal;

public class SealDefiner
{
	public static TotemicSeal loadSeal(String seal, Consumer<TileTotemicSeal> tick, String name)
	{
		InputStream in = Divination.class.getResourceAsStream("/assets/" + InfoDC.MOD_ID + "/textures/totemic/seals/" + seal + ".png");
		if(in != null)
			try
			{
				BufferedImage img = ImageIO.read(in);
				
				if(img.getWidth() != 16 || img.getHeight() != 16)
					return null;
				
				List<TSealPoint> points = new ArrayList<>();
				
				for(byte x = 0; x < 16; ++x)
					for(byte y = 0; y < 16; ++y)
					{
						EnumSealColor c = EnumSealColor.choose(img.getRGB(x, y));
						if(c != null)
							points.add(new TSealPoint(x, y, c));
					}
				
				TotemicSeal s = new TotemicSeal(Collections.unmodifiableList(points), tick);
				s.name = name;
				s.image = img;
				s.tex = "textures/totemic/seals/" + seal + ".png";
				return s;
			} catch(IOException ie)
			{
			}
		
		return null;
	}
}