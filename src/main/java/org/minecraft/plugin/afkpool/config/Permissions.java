package org.minecraft.plugin.afkpool.config;

public enum Permissions {
	NEXT("next"),
	RELOAD("reload");

	private final String permission;

	private static final String BASE = "afkpool.";

	Permissions(String permission) {
		this.permission = permission;
	}

	public String get() {
		return BASE + permission;
	}
}
