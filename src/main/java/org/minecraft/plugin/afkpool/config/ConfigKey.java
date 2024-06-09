package org.minecraft.plugin.afkpool.config;

import java.util.*;

public enum ConfigKey {
	REWARD_INTERVAL("reward-interval", 1, ConfigDescription.REWARD_INTERVAL),
	COMMAND_ON_REWARD("commands-on-reward", Collections.emptyList(), ConfigDescription.COMMAND_ON_REWARD);
	private final String id;
	private final Object defaultValue;
	private final ConfigDescription comment;

	ConfigKey(String id, Object defaultValue, ConfigDescription comment) {
		this.id = id;
		this.defaultValue = defaultValue;
		this.comment = comment;
	}

	public String getId() {
		return id;
	}

	public Object getDefault() {
		return defaultValue;
	}

	public String getComment() {
		return comment.getDescription();
	}
}
