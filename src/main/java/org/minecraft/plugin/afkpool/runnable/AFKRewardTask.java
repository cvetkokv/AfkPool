package org.minecraft.plugin.afkpool.runnable;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.scheduler.*;
import org.minecraft.plugin.afkpool.*;
import org.minecraft.plugin.afkpool.config.*;

import java.util.*;

public class AFKRewardTask extends BukkitRunnable {

	private final static Map<UUID, Long> afkStartTimes = new HashMap<>();
	private static long rewardInterval = 0;
	private final List<String> commandsOnReward;

	public AFKRewardTask(Config config) {
		rewardInterval = config.getRewardInterval();
		this.commandsOnReward = config.getCommandsOnReward();
	}

	public AFKRewardTask(Config config, Map<UUID, Long> cashedTimers) {
		rewardInterval = config.getRewardInterval();
		this.commandsOnReward = config.getCommandsOnReward();
		afkStartTimes.clear();
		afkStartTimes.putAll(cashedTimers);
	}

	public static Long nextReward(UUID playerId) {
		Long startTime = afkStartTimes.get(playerId);
		if (startTime == null) {
			return null;
		}
		long elapsedTime = System.currentTimeMillis() - startTime;
		return rewardInterval - elapsedTime;
	}

	public Map<UUID, Long> get() {
		return afkStartTimes;
	}

	public void addPlayer(UUID playerId) {
		afkStartTimes.put(playerId, System.currentTimeMillis());
	}

	public void removePlayer(UUID playerId) {
		afkStartTimes.remove(playerId);
	}

	public boolean isPlayerInList(UUID playerId) {
		return afkStartTimes.containsKey(playerId);
	}

	@Override
	public void run() {
		for (UUID playerId : afkStartTimes.keySet()) {
			Player player = Bukkit.getPlayer(playerId);
			if (player != null && player.isOnline()) {
				long afkTime = System.currentTimeMillis() - afkStartTimes.get(playerId);
				if (afkTime >= rewardInterval) {
					Bukkit.getScheduler().runTask(AfkPool.getPlugin(AfkPool.class), () -> {
						player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
						for (String command : commandsOnReward) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName()));
						}
					});
					afkStartTimes.put(playerId, System.currentTimeMillis());
				}
			} else if (player != null && !player.isOnline()) {
				afkStartTimes.entrySet().removeIf(entry -> Objects.equals(entry.getKey(), player.getUniqueId()));
			}
		}
	}
}
