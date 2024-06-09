package org.minecraft.plugin.afkpool.runnable;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.scheduler.*;
import org.minecraft.plugin.afkpool.*;

import java.util.*;

public class AFKRewardTask extends BukkitRunnable {

	private final Map<UUID, Long> afkStartTimes;
	private final long rewardInterval;

	public AFKRewardTask(long rewardInterval) {
		this.rewardInterval = rewardInterval;
		this.afkStartTimes = new HashMap<>();
	}

	public AFKRewardTask(long rewardInterval, Map<UUID, Long> afkStartTimes) {
		this.rewardInterval = rewardInterval;
		this.afkStartTimes = afkStartTimes;
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
						player.sendMessage("You have received a diamond for being in the AFK region for 30 minutes!");
					});
					afkStartTimes.put(playerId, System.currentTimeMillis());
				}
			} else if (player != null && !player.isOnline()) {
				afkStartTimes.entrySet().removeIf(entry -> Objects.equals(entry.getKey(), player.getUniqueId()));
			}
		}
	}
}
