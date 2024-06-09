package org.minecraft.plugin.afkpool.listener;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.minecraft.plugin.afkpool.*;
import org.minecraft.plugin.afkpool.runnable.*;
import org.minecraft.plugin.afkpool.util.*;

import java.util.UUID;

public class AFKRegionListener implements Listener {

	private final WorldGuardPlugin worldGuard;
	private final AFKRewardTask afkRewardTask;

	public AFKRegionListener(WorldGuardPlugin worldGuard, AFKRewardTask afkRewardTask) {
		this.worldGuard = worldGuard;
		this.afkRewardTask = afkRewardTask;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		UUID playerId = player.getUniqueId();

		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionQuery query = container.createQuery();

		boolean inAfkRewardRegion = query.testState(BukkitAdapter.adapt(player.getLocation()), worldGuard.wrapPlayer(player), CustomFlags.AFK_REWARD_FLAG);

		if (inAfkRewardRegion) {
			if (!afkRewardTask.isPlayerInList(playerId)) {
				afkRewardTask.addPlayer(playerId);
				MessageUtil.sendPrefixedMessage(player, "You have entered the AFK reward region!");
			}
		} else {
			if (afkRewardTask.isPlayerInList(playerId)) {
				afkRewardTask.removePlayer(playerId);
				MessageUtil.sendPrefixedMessage(player, "You have left the AFK reward region!");
			}
		}
	}
}
