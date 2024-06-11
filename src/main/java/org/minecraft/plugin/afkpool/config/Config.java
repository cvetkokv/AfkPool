package org.minecraft.plugin.afkpool.config;

import org.bukkit.configuration.file.*;
import org.bukkit.plugin.*;
import org.minecraft.plugin.afkpool.domain.*;
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
		appendMissingKeys();
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

				if (key == ConfigKey.ITEMS_ON_REWARD) {
					writer.write(key.getId() + ":");
					writer.newLine();

					@SuppressWarnings("unchecked")
					List<Map<String, Object>> items = (List<Map<String, Object>>) key.getDefault();
					for (Map<String, Object> item : items) {
						writer.write("  - item: " + item.get("item"));
						writer.newLine();
						writer.write("    amount: " + item.get("amount"));
						writer.newLine();
					}
				} else if (key == ConfigKey.COMMAND_ON_REWARD) {
					writer.write(key.getId() + ":");
					writer.newLine();

					@SuppressWarnings("unchecked")
					List<String> commands = (List<String>) key.getDefault();
					for (String command : commands) {
						writer.write("  - " + command);
						writer.newLine();
					}
				} else {
					writer.write(key.getId() + ": " + key.getDefault());
					writer.newLine();
				}

				writer.newLine();
			}
		} catch (IOException e) {
			plugin.getLogger().severe(Arrays.toString(e.getStackTrace()));
		}
	}

	private void appendMissingKeys() {
		Set<String> existingKeys = config.getKeys(false);

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile, true))) {
			for (ConfigKey key : ConfigKey.values()) {
				if (!existingKeys.contains(key.getId())) {
					writer.newLine();
					writer.write("# " + key.getComment());
					writer.newLine();

					if (key == ConfigKey.ITEMS_ON_REWARD) {
						writer.write(key.getId() + ":");
						writer.newLine();

						@SuppressWarnings("unchecked")
						List<Map<String, Object>> items = (List<Map<String, Object>>) key.getDefault();
						for (Map<String, Object> item : items) {
							writer.write("  - item: " + item.get("item"));
							writer.newLine();
							writer.write("    amount: " + item.get("amount"));
							writer.newLine();
						}
					} else if (key == ConfigKey.COMMAND_ON_REWARD) {
						writer.write(key.getId() + ":");
						writer.newLine();

						@SuppressWarnings("unchecked")
						List<String> commands = (List<String>) key.getDefault();
						for (String command : commands) {
							writer.write("  - " + command);
							writer.newLine();
						}
					} else {
						writer.write(key.getId() + ": " + key.getDefault());
						writer.newLine();
					}

					writer.newLine();
				}
			}
		} catch (IOException e) {
			plugin.getLogger().severe("Failed to append to config: " + e.getMessage());
		}
	}


	public long getRewardInterval() {
		return TimeUtil.minutesToMilliseconds(getLong(ConfigKey.REWARD_INTERVAL));
	}

	public List<String> getCommandsOnReward() {
		return getStringList(ConfigKey.COMMAND_ON_REWARD);
	}

	public List<ItemReward> getItemsOnReward() {
		List<ItemReward> items = new ArrayList<>();
		List<Map<?, ?>> mapList = config.getMapList(ConfigKey.ITEMS_ON_REWARD.getId());

		for (Map<?, ?> map : mapList) {
			try {
				String item = (String) map.get("item");
				int amount = (Integer) map.get("amount");
				ItemReward itemReward = new ItemReward(item, amount);
				items.add(itemReward);
			} catch (Exception e) {
				plugin.getLogger().severe("Failed to parse item reward: " + e.getMessage());
			}
		}
		return items;
	}

	public long getLong(ConfigKey key) {
		return config.getLong(key.getId());
	}
	public List<String> getStringList(ConfigKey key) {
		return config.getStringList(key.getId());
	}
}
