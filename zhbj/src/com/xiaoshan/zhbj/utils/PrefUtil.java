package com.xiaoshan.zhbj.utils;

import android.content.Context;

public class PrefUtil {

	public static final String PREFER_CONFIG = "config";

	public static void setBoolean(Context context, String key, boolean flag) {
		context.getSharedPreferences(PREFER_CONFIG, Context.MODE_PRIVATE)
				.edit().putBoolean(key, flag).commit();
	}

	public static boolean getBoolean(Context context, String key) {

		boolean flag = context.getSharedPreferences(PREFER_CONFIG,
				Context.MODE_PRIVATE).getBoolean(key, false);
		return flag;
	}
}
