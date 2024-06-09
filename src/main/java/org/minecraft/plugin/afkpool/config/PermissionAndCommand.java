package org.minecraft.plugin.afkpool.config;

public enum PermissionAndCommand {
	RELOAD("reload"),
	NEXT("next");

	private final String key;
	private static final String BASE = "afkpool.";

	PermissionAndCommand(String key) {
		this.key = key;
	}

	public String getPermission() {
		return BASE + key;
	}
	public String getCommand() {
		return key;
	}
}
