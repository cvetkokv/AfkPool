package org.minecraft.plugin.afkpool.config;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import org.bukkit.plugin.Plugin;
import org.minecraft.plugin.afkpool.domain.*;

public class DatabaseConfig {
	private final File databaseFile;
	private final Plugin plugin;

	public DatabaseConfig(Plugin plugin) {
		this.plugin = plugin;
		this.databaseFile = new File(plugin.getDataFolder(), "database.db");
		initializeDatabase();
	}

	private void initializeDatabase() {
		if (!databaseFile.exists()) {
			try {
				if (databaseFile.createNewFile()) {
					plugin.getLogger().info("Database file created.");
					createTables();
				}
			} catch (IOException e) {
				plugin.getLogger().severe("Failed to create database file: " + e.getMessage());
			}
		} else {
			createTables();
		}
	}

	private void createTables() {
		String sql = "CREATE TABLE IF NOT EXISTS afk_pools (" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"pool_name TEXT NOT NULL," +
				"region_name TEXT NOT NULL," +
				"x INTEGER NOT NULL," +
				"y INTEGER NOT NULL," +
				"z INTEGER NOT NULL" +
				");";

		try (Connection conn = connect();
			 Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
			plugin.getLogger().info("Database tables created.");
		} catch (SQLException e) {
			plugin.getLogger().severe("Failed to create database tables: " + e.getMessage());
		}
	}

	public Connection connect() {
		String url = "jdbc:sqlite:" + databaseFile.getPath();
		try {
			return DriverManager.getConnection(url);
		} catch (SQLException e) {
			plugin.getLogger().severe("Connection to SQLite has failed: " + e.getMessage());
			return null;
		}
	}

	public void insertAfkPool(String poolName, String regionName, int x, int y, int z) {
		String sql = "INSERT INTO afk_pools(pool_name, region_name, x, y, z) VALUES(?, ?, ?, ?, ?)";

		try (Connection conn = connect();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, poolName);
			pstmt.setString(2, regionName);
			pstmt.setInt(3, x);
			pstmt.setInt(4, y);
			pstmt.setInt(5, z);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			plugin.getLogger().severe("Failed to insert AFK pool: " + e.getMessage());
		}
	}

	public List<AfkPoolModel> getAfkPools() {
		String sql = "SELECT * FROM afk_pools";
		List<AfkPoolModel> afkPools = new ArrayList<>();

		try (Connection conn = connect();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				AfkPoolModel entry = new AfkPoolModel(
						rs.getString("pool_name"),
						rs.getString("region_name"),
						rs.getInt("x"),
						rs.getInt("y"),
						rs.getInt("z")
				);
				afkPools.add(entry);
			}
		} catch (SQLException e) {
			plugin.getLogger().severe("Failed to get AFK pools: " + e.getMessage());
		}

		return afkPools;
	}

	public AfkPoolModel getAfkPoolByName(String poolName) {
		String sql = "SELECT * FROM afk_pools WHERE pool_name = ?";
		AfkPoolModel afkPoolEntry = null;

		try (Connection conn = connect();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, poolName);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				afkPoolEntry = new AfkPoolModel(
						rs.getString("pool_name"),
						rs.getString("region_name"),
						rs.getInt("x"),
						rs.getInt("y"),
						rs.getInt("z")
				);
			}
		} catch (SQLException e) {
			plugin.getLogger().severe("Failed to get AFK pool by name: " + e.getMessage());
		}

		return afkPoolEntry;
	}

	public void removeAfkPoolByName(String poolName) {
		String sql = "DELETE FROM afk_pools WHERE pool_name = ?";

		try (Connection conn = connect();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, poolName);
			int rowsAffected = pstmt.executeUpdate();

			if (rowsAffected > 0) {
				plugin.getLogger().info("AFK pool " + poolName + " successfully removed.");
			} else {
				plugin.getLogger().info("No AFK pool found with the name " + poolName + ".");
			}
		} catch (SQLException e) {
			plugin.getLogger().severe("Failed to remove AFK pool: " + e.getMessage());
		}
	}
}