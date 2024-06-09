package org.minecraft.plugin.afkpool.config;

import org.bukkit.configuration.file.*;
import org.bukkit.plugin.*;
import org.minecraft.plugin.afkpool.util.*;

import java.io.*;
import java.util.*;

public class Config {
	private final FileConfiguration config;
	private final File configFile;
	private final Plugin plugin;

	public Config(Plugin plugin) {
		this.plugin = plugin;
		this.configFile = new File(plugin.getDataFolder(), "config.yml");

		if (!configFile.exists()) {
			createDefaultConfig(plugin);
		}

		this.config = YamlConfiguration.loadConfiguration(configFile);
	}

	private void createDefaultConfig(Plugin plugin) {
		try {
			if (plugin.getDataFolder().mkdirs()) {
				plugin.getLogger().info("Created plugin data folder.");
			}
			if (configFile.createNewFile()) {
				plugin.getLogger().info("Created default config.yml.");
				writeDefaultConfigWithComments();
			}
		} catch (IOException e) {
			plugin.getLogger().severe(Arrays.toString(e.getStackTrace()));
		}
	}

	private void writeDefaultConfigWithComments() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
			for (ConfigKey key : ConfigKey.values()) {
				writer.write("# " + key.getComment());
				writer.newLine();
				writer.write(key.getId() + ": " + key.getDefault());
				writer.newLine();
				writer.newLine();
			}
		} catch (IOException e) {
			plugin.getLogger().severe(Arrays.toString(e.getStackTrace()));
		}
	}

	public long getRewardInterval() {
		return TimeUtil.minutesToMilliseconds(getLong(ConfigKey.REWARD_INTERVAL));
	}

	public long getLong(ConfigKey key) {
		return config.getLong(key.getId());
	}
	public List<String> getStringList(ConfigKey key) {
		return config.getStringList(key.getId());
	}
}
