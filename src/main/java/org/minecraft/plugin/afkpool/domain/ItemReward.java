package org.minecraft.plugin.afkpool.domain;

import org.bukkit.*;
import org.bukkit.inventory.*;

public class ItemReward {
	private final String item;
	private final int amount;

	public ItemReward(String item, int amount) {
		this.item = item;
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}

	public String getItemName() {
		return item;
	}

	public ItemStack createItemStack() {
		return new ItemStack(getItem(), amount);
	}

	private Material getItem() {
		Material material = Material.matchMaterial(item);
		if (material == null) {
			throw new IllegalArgumentException("Invalid item name: " + item);
		}
		return material;
	}
}
