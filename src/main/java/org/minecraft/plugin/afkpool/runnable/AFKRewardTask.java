package org.minecraft.plugin.afkpool.runnable;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.scheduler.*;

import java.util.*;

public class AFKRewardTask extends BukkitRunnable {

	private final Map<UUID, Long> afkStartTimes = new HashMap<>();
	private final long rewardInterval;

	public AFKRewardTask(long rewardInterval) {
		this.rewardInterval = rewardInterval;
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
					player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
					player.sendMessage("You have received a diamond for being in the AFK region for 30 minutes!");
					afkStartTimes.put(playerId, System.currentTimeMillis()); // Reset timer after reward
				}
			}
		}
	}
}
