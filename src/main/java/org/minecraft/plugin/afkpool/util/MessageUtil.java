package org.minecraft.plugin.afkpool.util;

import org.bukkit.command.*;
import org.bukkit.entity.*;

public class MessageUtil {

	private static final String PREFIX = "§3[§b§lAfkPool§3] §6";

	public static void sendPrefixedMessage(CommandSender sender, String message) {
		sender.sendMessage(PREFIX + message);
	}

	public static void sendPrefixedMessage(Player player, String message) {
		player.sendMessage(PREFIX + message);
	}
}
