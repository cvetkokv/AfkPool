package org.minecraft.plugin.afkpool.config;

public enum Permission {
	RELOAD("reload");

	private final String key;
	private static final String BASE = "afkpool.";

	Permission(String key) {
		this.key = key;
	}

	public String getKey() {
		return BASE + key;
	}
}
