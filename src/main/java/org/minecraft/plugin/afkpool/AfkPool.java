package org.minecraft.plugin.afkpool;

import org.bukkit.plugin.java.JavaPlugin;
import org.minecraft.plugin.afkpool.command.*;
import org.minecraft.plugin.afkpool.config.*;
import org.minecraft.plugin.afkpool.handler.*;

import java.util.*;

public class AfkPool extends JavaPlugin {

    private Config config;
    private DatabaseConfig databaseConfig;
    private ConfigHandler configHandler;
    private EventHandlerManager eventHandlerManager;

    @Override
    public void onLoad() {
        FlagHandler.registerFlags(this);
    }

    @Override
    public void onEnable() {
        config = new Config(this);
        this.databaseConfig = new DatabaseConfig(this);
        eventHandlerManager = new EventHandlerManager(this);
        configHandler = new ConfigHandler(this, config, getServer(), eventHandlerManager, databaseConfig);

        configHandler.configStartup();
    }

    @Override
    public void onDisable() {
        getLogger().info("AFKRewards has been disabled!");
    }
}

