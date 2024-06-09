package org.minecraft.plugin.afkpool.handler;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.registry.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.minecraft.plugin.afkpool.*;

public class FlagHandler {

	public static void registerFlags(JavaPlugin plugin) {
		FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

		try {
			registry.register(CustomFlags.AFK_REWARD_FLAG);
			plugin.getLogger().info("Custom flags registered successfully!");
		} catch (Exception e) {
			plugin.getLogger().severe("Failed to register custom flags: " + e.getMessage());
		}
	}
}

