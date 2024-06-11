package org.minecraft.plugin.afkpool.domain;

public class AfkPoolModel {
	private String poolName;
	private String regionName;
	private int x;
	private int y;
	private int z;

	public AfkPoolModel(String poolName, String regionName, int x, int y, int z) {
		this.poolName = poolName;
		this.regionName = regionName;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}
}
