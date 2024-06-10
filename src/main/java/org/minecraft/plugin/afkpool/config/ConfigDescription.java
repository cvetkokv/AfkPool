package org.minecraft.plugin.afkpool.config;

public enum ConfigDescription {
	REWARD_INTERVAL("Interval for AFK rewards in minutes"),
	COMMAND_ON_REWARD("Commands to execute on reward, if you want to give player directly use {player} for example eco give {player} 5000"),
	ITEMS_ON_REWARD("Create the list of items you want player to get on reward");

	private final String description;

	ConfigDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
