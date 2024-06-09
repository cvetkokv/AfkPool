package org.minecraft.plugin.afkpool.config;

public enum ConfigKey {
	REWARD_INTERVAL("reward-interval", 60000);

	private final String key;
	private final Object defaultValue;

	ConfigKey(String key, Object defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	public String getKey() {
		return key;
	}

	public Object getDefault() {
		return defaultValue;
	}
}
