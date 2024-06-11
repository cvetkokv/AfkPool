package org.minecraft.plugin.afkpool.handler;

import org.bukkit.command.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.*;
import org.minecraft.plugin.afkpool.command.*;

import java.util.*;

public class EventHandlerManager {
	private final JavaPlugin plugin;
	private final List<Listener> registeredListeners = new ArrayList<>();
	private final List<CommandExecutor> registeredCommands = new ArrayList<>();

	public EventHandlerManager(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public void registerCommand(String name, CommandExecutor commandExecutor) {
		Objects.requireNonNull(plugin.getCommand(name))
				.setExecutor(commandExecutor);
		registeredCommands.add(commandExecutor);
	}

	public void registerListener(Listener listener) {
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
		registeredListeners.add(listener);
	}

	public void unregisterListener(Class<?> clazz) {
		Iterator<Listener> iterator = registeredListeners.iterator();
		while (iterator.hasNext()) {
			Listener listener = iterator.next();
			if (clazz.isInstance(listener)) {
				HandlerList.unregisterAll(listener);
				iterator.remove();
			}
		}
	}
}
