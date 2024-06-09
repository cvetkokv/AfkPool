package org.minecraft.plugin.afkpool.config;

import org.bukkit.configuration.file.*;
import org.bukkit.plugin.java.*;

import java.io.*;
import java.util.*;

public class ConfigHandler {
	private final FileConfiguration config;
	private final File configFile;
	private final JavaPlugin plugin;

	public ConfigHandler(JavaPlugin plugin) {
		this.plugin = plugin;
		this.configFile = new File(plugin.getDataFolder(), "config.yml");

		if (!configFile.exists()) {
			createDefaultConfig(plugin);
		}

		this.config = YamlConfiguration.loadConfiguration(configFile);
	}

	private void createDefaultConfig(JavaPlugin plugin) {
		try {
			if (plugin.getDataFolder().mkdirs()) {
				plugin.getLogger().info("Created plugin data folder.");
			}
			if (configFile.createNewFile()) {
				plugin.getLogger().info("Created default config.yml.");
				setDefaults();
			}
		} catch (IOException e) {
			plugin.getLogger().severe(Arrays.toString(e.getStackTrace()));
		}
	}

	private void setDefaults() {
		YamlConfiguration defaultConfig = new YamlConfiguration();
		for (ConfigKey key : ConfigKey.values()) {
			defaultConfig.set(key.getKey(), key.getDefault());
		}
		try {
			defaultConfig.save(configFile);
		} catch (IOException e) {
			plugin.getLogger().severe(Arrays.toString(e.getStackTrace()));
		}
	}

	public long getLong(ConfigKey key) {
		return config.getLong(key.getKey());
	}
}
