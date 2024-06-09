package org.minecraft.plugin.afkpool.util;

public class TimeUtil {
	public static long minutesToMilliseconds(long minutes) {
		return minutes * 60 * 1000L;
	}

	public static String formatMillisToMinutesSeconds(long millis) {
		long seconds = millis / 1000;
		long minutes = seconds / 60;
		seconds = seconds % 60;

		return String.format("%02d:%02d", minutes, seconds);
	}
}
