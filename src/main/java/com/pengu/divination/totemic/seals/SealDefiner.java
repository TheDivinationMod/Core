package com.pengu.divination.totemic.seals;

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

public class SealDefiner
{
	public static TotemicSeal loadSeal(String seal, Consumer<TileTotemicSeal> tick)
	{
		InputStream in = Divination.class.getResourceAsStream("/assets/" + InfoDC.MOD_ID + "/totem/seals/" + seal + ".png");
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
				
				return new TotemicSeal(Collections.unmodifiableList(points), tick);
			} catch(IOException ie)
			{
			}
		
		return null;
	}
}