package com.pengu.divination.proxy;

import com.pengu.musiclayer.api.GetMusicEvent;
import com.pengu.musiclayer.api.MusicLayer;
import com.pengu.musiclayer.api.UpdateAlternativeMusicEvent;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Client extends Common
{
	private String oneTimeMusic;
	
	@Override
	public void preInit()
	{
		MusicLayer.MUSIC_BUS.register(this);
	}
	
	@Override
	public void playOnce(String url)
	{
		oneTimeMusic = url;
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
}