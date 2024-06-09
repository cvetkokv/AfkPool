package org.minecraft.plugin.afkpool.handler;

import org.bukkit.event.*;
import org.bukkit.plugin.*;

import java.util.*;

public class EventHandlerManager {
	private final Plugin plugin;
	private final List<Listener> registeredListeners = new ArrayList<>();

	public EventHandlerManager(Plugin plugin) {
		this.plugin = plugin;
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
