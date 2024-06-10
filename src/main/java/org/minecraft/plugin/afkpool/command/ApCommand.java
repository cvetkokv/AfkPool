package org.minecraft.plugin.afkpool.command;

import com.sk89q.worldedit.bukkit.*;
import com.sk89q.worldedit.internal.annotation.*;
import com.sk89q.worldedit.math.*;
import com.sk89q.worldedit.regions.*;
import com.sk89q.worldguard.*;
import com.sk89q.worldguard.bukkit.*;
import com.sk89q.worldguard.protection.flags.*;
import com.sk89q.worldguard.protection.managers.*;
import com.sk89q.worldguard.protection.regions.*;
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

import java.util.*;

import static org.minecraft.plugin.afkpool.CustomFlags.AFK_REWARD_FLAG;

public class ApCommand implements CommandExecutor {

	private final static String NO_PERMISSION = "You do not have permission to use this command.";

	private final ConfigHandler configHandler;
	private final WorldEditPlugin worldEdit;
	private final WorldGuardPlugin worldGuard;

	public ApCommand(ConfigHandler configHandler) {
		this.configHandler = configHandler;
		this.worldEdit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		this.worldGuard = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
	}

	@Override
	public boolean onCommand(@NonNull CommandSender sender,
							 @NonNull Command command,
							 @NonNull String label, String[] args) {
		if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
			sendHelpMessage(sender);
			return true;
		}

		if (args[0].equalsIgnoreCase("create")) {
			if (sender instanceof Player player) {
				if (player.hasPermission("afkpool.create")) {
					createAfkRegion(player);
				} else {
					sender.sendMessage(NO_PERMISSION);
				}
			} else {
				sender.sendMessage("This command can only be used by a player.");
			}
			return true;
		}

		if (args.length == 1 && args[0].equalsIgnoreCase(Commands.RELOAD.getCommand())) {
			if (sender.hasPermission(Commands.RELOAD.getPermission())) {
				configHandler.reloadConfig();
				MessageUtil.sendPrefixedMessage(sender, "Configuration reloaded.");
			} else {
				MessageUtil.sendPrefixedMessage(sender, NO_PERMISSION);
			}
			return true;
		}

		if (args.length == 1 && args[0].equalsIgnoreCase(Commands.NEXT.getCommand())) {
			if (sender.hasPermission(Commands.NEXT.getPermission())) {
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
				MessageUtil.sendPrefixedMessage(sender, NO_PERMISSION);
			}
		}

		return true;
	}

	private void createAfkRegion(Player player) {
		Region selection;
		try {
			selection = worldEdit.getSession(player).getSelection(BukkitAdapter.adapt(player.getWorld()));
			if (selection == null) {
				MessageUtil.sendPrefixedMessage(player, "You need to make a WorldEdit selection first.");
				return;
			}
		} catch (Exception e) {
			MessageUtil.sendPrefixedMessage(player, "Failed to get WorldEdit selection: " + e.getMessage());
			return;
		}

		BlockVector3 min = selection.getMinimumPoint();
		BlockVector3 max = selection.getMaximumPoint();

		String regionId = "afk_" + player.getName() + "_" + UUID.randomUUID().toString().substring(0, 8);
		ProtectedCuboidRegion region = new ProtectedCuboidRegion(
				regionId,
				BlockVector3.at(min.getBlockX(), min.getBlockY(), min.getBlockZ()),
				BlockVector3.at(max.getBlockX(), max.getBlockY(), max.getBlockZ())
		);

		RegionContainer container = WorldGuard.getInstance().getPlatform()
				.getRegionContainer();
		RegionManager regions = container.get(BukkitAdapter.adapt(player.getWorld()));
		if (regions != null) {
			regions.addRegion(region);
			region.setFlag(AFK_REWARD_FLAG, StateFlag.State.ALLOW);
			MessageUtil.sendPrefixedMessage(player, "AFK region created with ID: " + regionId);
		} else {
			MessageUtil.sendPrefixedMessage(player, "Could not access WorldGuard region manager.");
		}
	}

	private void sendHelpMessage(CommandSender sender) {
		sender.sendMessage("§3[§b§lAfkPool§3] §6Available Commands:");
		sender.sendMessage("§6/ap help - Shows this help message.");
		sender.sendMessage("§6/ap create - Creates an AFK region from WorldEdit selection.");
		sender.sendMessage("§6/ap reload - Reloads the plugin configuration.");
		sender.sendMessage("§6/ap next - Shows the time remaining for the next reward.");
	}
}