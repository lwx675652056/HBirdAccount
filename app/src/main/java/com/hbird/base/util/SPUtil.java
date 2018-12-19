package com.hbird.base.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.Set;
/**
 * Created by Liul on 2018/7/5.
 */

final public class SPUtil {

	private SPUtil() {

	}

	/**
	 * 根据key获取值(String类型)
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getPrefString(Context context, String key,
                                       final String defaultValue) {
	    if (context == null){
	        return "";
        }
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getString(key, defaultValue);
	}

	/**
	 * 根据key写入值(String类型)
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setPrefString(Context context, final String key,
                                     final String value) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putString(key, value).commit();
	}

	/**
	 * 根据key获取值(Boolean类型)
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue 默认值
	 * @return
	 */
	public static boolean getPrefBoolean(Context context, final String key,
                                         final boolean defaultValue) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getBoolean(key, defaultValue);
	}

	/**
	 * 根据key写入值(Boolean类型)
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setPrefBoolean(Context context, final String key,
                                      final boolean value) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putBoolean(key, value).commit();
	}

	public static int getPrefInt(Context context, final String key,
                                 final int defaultValue) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getInt(key, defaultValue);
	}

	public static void setPrefInt(Context context, final String key,
                                  final int value) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putInt(key, value).commit();
	}

	public static float getPrefFloat(Context context, final String key,
                                     final float defaultValue) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getFloat(key, defaultValue);
	}

	public static void setPrefFloat(Context context, final String key,
                                    final float value) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putFloat(key, value).commit();
	}

	public static long getPrefLong(Context context, final String key,
                                   final long defaultValue) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getLong(key, defaultValue);
	}

	public static void setPrefLong(Context context, final String key,
                                   final long value) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putLong(key, value).commit();
	}
	
	public static void setStringSet(Context context, final String key,
                                    final Set<String> set) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putStringSet(key, set).commit();
	}
	
	public static Set<String> getPrefSet(Context context, final String key,
                                         final Set<String> defValues) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getStringSet(key, defValues);
	}

	/**
	 * 检查是否包含key
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean hasKey(Context context, final String key) {
		return PreferenceManager.getDefaultSharedPreferences(context).contains(
				key);
	}

	/**
	 * 检查是否包含key
	 *
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean remove(Context context, final String key) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.edit().remove(key).commit();
	}

	/**
	 * 清空配置信息
	 * 
	 * @param context
	 */
	public static void clearPreference(Context context) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		final Editor editor = settings.edit();
		editor.clear();
		editor.commit();
	}

}
