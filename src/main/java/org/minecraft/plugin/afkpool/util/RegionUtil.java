package org.minecraft.plugin.afkpool.util;

import com.sk89q.worldedit.math.*;
import com.sk89q.worldguard.protection.regions.*;

public class RegionUtil {
	public static BlockVector3 getCenter(ProtectedCuboidRegion region) {
		BlockVector3 min = region.getMinimumPoint();
		BlockVector3 max = region.getMaximumPoint();

		int centerX = (min.getX() + max.getX()) / 2;
		int centerY = min.getY(); // Use the minimum Y coordinate
		int centerZ = (min.getZ() + max.getZ()) / 2;

		return BlockVector3.at(centerX, centerY, centerZ);
	}
}
