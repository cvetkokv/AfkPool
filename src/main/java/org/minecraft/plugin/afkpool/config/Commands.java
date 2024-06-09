package org.minecraft.plugin.afkpool.config;

public enum Commands {
	RELOAD("reload", Permissions.RELOAD),
	NEXT("next", Permissions.NEXT);

	private final String key;
	private final Permissions permission;

	Commands(String key, Permissions permission) {
		this.key = key;
		this.permission = permission;
	}

	public String getPermission() {
		return permission.get();
	}
	public String getCommand() {
		return key;
	}
}
