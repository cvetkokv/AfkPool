package org.minecraft.plugin.afkpool;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.WorldGuard;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.minecraft.plugin.afkpool.config.*;
import org.minecraft.plugin.afkpool.handler.*;
import org.minecraft.plugin.afkpool.listener.AFKRegionListener;
import org.minecraft.plugin.afkpool.runnable.AFKRewardTask;

public class AfkPool extends JavaPlugin {

    private AFKRewardTask afkRewardTask;
    private WorldGuardPlugin worldGuard;
    private ConfigHandler configHandler;

    @Override
    public void onLoad() {
        FlagHandler.registerFlags(this);
    }

    @Override
    public void onEnable() {
        configHandler = new ConfigHandler(this);

        long rewardInterval = configHandler.getLong(ConfigKey.REWARD_INTERVAL);

        worldGuard = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");

        if (worldGuard != null) {
            getLogger().info("AFKRewards has been enabled with WorldGuard support!");

            afkRewardTask = new AFKRewardTask(rewardInterval);
            afkRewardTask.runTaskTimer(this, 0L, 20L);

            getServer().getPluginManager().registerEvents(new AFKRegionListener(worldGuard, afkRewardTask), this);
        } else {
            getLogger().severe("WorldGuard is not installed! Disabling AFKRewards.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("AFKRewards has been disabled!");
    }
}

