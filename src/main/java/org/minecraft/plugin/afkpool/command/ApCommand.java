package org.minecraft.plugin.afkpool.command;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.checkerframework.checker.nullness.qual.*;
import org.minecraft.plugin.afkpool.config.*;
import org.minecraft.plugin.afkpool.handler.*;
import org.minecraft.plugin.afkpool.runnable.*;
import org.minecraft.plugin.afkpool.util.*;

public class ApCommand implements CommandExecutor {

	private final static String NO_PERMISSION = "You do not have permission to use this command.";

	private final ConfigHandler configHandler;

	public ApCommand(ConfigHandler configHandler) {
		this.configHandler = configHandler;
	}

	@Override
	public boolean onCommand(@NonNull CommandSender sender,
							 @NonNull Command command,
							 @NonNull String label, String[] args) {
		if (args.length == 1 && args[0].equalsIgnoreCase(PermissionAndCommand.RELOAD.getCommand())) {
			if (sender.hasPermission(PermissionAndCommand.RELOAD.getPermission())) {
				configHandler.reloadConfig();
				MessageUtil.sendPrefixedMessage(sender, "Configuration reloaded.");
			} else {
				sender.sendMessage(NO_PERMISSION);
			}
			return true;
		}

		if (args.length == 1 && args[0].equalsIgnoreCase(PermissionAndCommand.NEXT.getCommand())) {
			if (sender.hasPermission(PermissionAndCommand.NEXT.getPermission())) {
				Player player = Bukkit.getPlayer(sender.getName());
				if (player == null) {
					MessageUtil.sendPrefixedMessage(sender, "Only player can use this message");
					return true;
				}
				Long nextRewardTimer = AFKRewardTask.nextReward(player.getUniqueId());
				if (nextRewardTimer == null) {
					MessageUtil.sendPrefixedMessage(sender, "You are not inside AFK reward region");
				} else {
					MessageUtil.sendPrefixedMessage(sender, "Next reward in: " + TimeUtil.formatMillisToMinutesSeconds(nextRewardTimer));
				}
			} else {
				sender.sendMessage(NO_PERMISSION);
			}
		}

		return true;
	}
}