package org.minecraft.plugin.afkpool.config;

public enum ConfigDescription {
	REWARD_INTERVAL("Interval for AFK rewards in seconds"),
	COMMAND_ON_REWARD("Commands to execute on reward");

	private final String description;

	ConfigDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
