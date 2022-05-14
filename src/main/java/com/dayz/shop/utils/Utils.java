package com.dayz.shop.utils;

public class Utils {
	public static String extractSteamId(String steamUrl) {
		return steamUrl.substring(steamUrl.lastIndexOf('/') + 1);
	}
}
