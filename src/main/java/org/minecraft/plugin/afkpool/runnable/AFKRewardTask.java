package org.minecraft.plugin.afkpool.runnable;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.scheduler.*;
import org.minecraft.plugin.afkpool.*;
import org.minecraft.plugin.afkpool.config.*;
import org.minecraft.plugin.afkpool.domain.*;
import org.minecraft.plugin.afkpool.util.*;

import java.util.*;
import java.util.stream.*;

public class AFKRewardTask extends BukkitRunnable {

	private final static Map<UUID, Long> afkStartTimes = new HashMap<>();
	private static long rewardInterval = 0;
	private final List<String> commandsOnReward;

	private final List<ItemReward> itemRewards;

	public AFKRewardTask(Config config) {
		rewardInterval = config.getRewardInterval();
		this.commandsOnReward = config.getCommandsOnReward();
		this.itemRewards = config.getItemsOnReward();
	}

	public AFKRewardTask(Config config, Map<UUID, Long> cashedTimers) {
		rewardInterval = config.getRewardInterval();
		this.commandsOnReward = config.getCommandsOnReward();
		this.itemRewards = config.getItemsOnReward();
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
						if (!itemRewards.isEmpty()) {
							for (ItemReward itemReward : itemRewards) {
								player.getInventory().addItem(itemReward.createItemStack());
							}
							String addedItems = itemRewards.stream()
									.map(item -> item.getAmount() + " " + item.getItemName())
									.collect(Collectors.joining(", "));

							MessageUtil.sendPrefixedMessage(player, "You received: " + addedItems);
						}
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
