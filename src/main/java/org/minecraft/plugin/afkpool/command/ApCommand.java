package org.minecraft.plugin.afkpool.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.*;
import org.minecraft.plugin.afkpool.config.*;
import org.minecraft.plugin.afkpool.handler.*;

public class ApCommand implements CommandExecutor {

	private final ConfigHandler configHandler;

	public ApCommand(ConfigHandler configHandler) {
		this.configHandler = configHandler;
	}

	@Override
	public boolean onCommand(@NonNull CommandSender sender,
							 @NonNull Command command,
							 @NonNull String label, String[] args) {
		if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			if (sender.hasPermission(Permission.RELOAD.getKey())) {
				configHandler.reloadConfig();
				sender.sendMessage("AfkPool configuration reloaded.");
			} else {
				sender.sendMessage("You do not have permission to use this command.");
			}
			return true;
		}

		return true;
	}
}