package org.minecraft.plugin.afkpool.handler;

import com.sk89q.worldguard.bukkit.*;
import org.bukkit.*;
import org.bukkit.plugin.java.*;
import org.minecraft.plugin.afkpool.command.*;
import org.minecraft.plugin.afkpool.config.*;
import org.minecraft.plugin.afkpool.listener.*;
import org.minecraft.plugin.afkpool.runnable.AFKRewardTask;

import java.util.*;

public class ConfigHandler {

	private final JavaPlugin plugin;
	private final Server server;
	private final EventHandlerManager eventHandlerManager;
	private final WorldGuardPlugin worldGuard;
	private Config config;
	private AFKRewardTask afkRewardTask;

	public ConfigHandler(JavaPlugin plugin, Config config, Server server,
						 EventHandlerManager eventHandlerManager) {
		this.plugin = plugin;
		this.config = config;
		this.server = server;
		this.eventHandlerManager = eventHandlerManager;
		this.worldGuard = (WorldGuardPlugin) server.getPluginManager().getPlugin("WorldGuard");
	}

	public void configStartup() {
		if (worldGuard != null) {
			afkRewardTask = new AFKRewardTask(config);
			afkRewardTask.runTaskTimerAsynchronously(plugin, 0L, 20L);
		} else {
			server.getPluginManager().disablePlugin(plugin);
			return;
		}

		eventHandlerManager.registerListener(new AFKRegionListener(worldGuard, afkRewardTask));

		ApCommand apCommand = new ApCommand(this);
		eventHandlerManager.registerCommand("afkpool", apCommand);
		eventHandlerManager.registerCommand("ap", apCommand);
		Bukkit.getLogger().info("AfkPool has been enabled!!");
	}

	public void reloadConfig() {
		plugin.getLogger().info("Reloading configuration...");
		plugin.reloadConfig();
		config = new Config(plugin);

		if (afkRewardTask != null) {
			afkRewardTask.cancel();
		}

		Map<UUID, Long> cashedTimers = afkRewardTask.get();

		afkRewardTask = new AFKRewardTask(config, cashedTimers);
		afkRewardTask.runTaskTimerAsynchronously(plugin, 0L, 20L);

		eventHandlerManager.unregisterListener(AFKRegionListener.class);
		eventHandlerManager.registerListener(new AFKRegionListener(worldGuard, afkRewardTask));
		plugin.getLogger().info("Reloading configuration finished");
	}
}