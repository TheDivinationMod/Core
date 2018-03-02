package com.pengu.divination.core.constants;

import com.pengu.hammercore.common.utils.IOUtils;
import com.pengu.hammercore.json.JSONArray;
import com.pengu.hammercore.json.JSONObject;
import com.pengu.hammercore.json.JSONTokener;

public class URLsDC
{
	public static final String //
	MAIN_GIT_DIR = "https://raw.githubusercontent.com/TheDivinationMod/", //
	        JSON_GIT_DIR = MAIN_GIT_DIR + "JSON/master/", //
	        GIT_JSON_UPDATE = JSON_GIT_DIR + "update.json", //
	        GIT_JSON_AUTHORS = JSON_GIT_DIR + "authors.json", //
	        GIT_TXT_DESCRIPTION = JSON_GIT_DIR + "description.txt";
	
	public static JSONObject objFromUrl(String url)
	{
		try
		{
			return (JSONObject) new JSONTokener(IOUtils.ioget(url)).nextValue();
		} catch(Throwable err)
		{
		}
		return null;
	}
	
	public static JSONArray arrFromUrl(String url)
	{
		try
		{
			return (JSONArray) new JSONTokener(IOUtils.ioget(url)).nextValue();
		} catch(Throwable err)
		{
		}
		return null;
	}
}