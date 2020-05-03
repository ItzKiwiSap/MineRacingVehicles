package nl.itz_kiwisap_.dn.mineracing.vehicles;

import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.gson.Gson;

import nl.itz_kiwisap_.dn.mineracing.vehicles.api.API;
import nl.itz_kiwisap_.dn.mineracing.vehicles.listeners.EntityDismount;
import nl.itz_kiwisap_.dn.mineracing.vehicles.listeners.EntityInteractAtEntity;
import nl.itz_kiwisap_.dn.mineracing.vehicles.listeners.PlayerQuit;
import nl.itz_kiwisap_.dn.mineracing.vehicles.managers.ConfigManager;
import nl.itz_kiwisap_.dn.mineracing.vehicles.managers.Manager;

public class Main extends JavaPlugin {

	private static Main instance;
	public static ConfigManager config = new ConfigManager();
	private static Manager manager = new Manager();
	public static API api = new API();
	private static Gson gson = new Gson();
	private ProtocolManager protocol;
	
	public Main() {
		this.protocol = null;
		instance = this;
	}
	
	public void onEnable() {
		this.protocol = ProtocolLibrary.getProtocolManager();
		manager.enable();
		new PlayerQuit();
		new EntityDismount();
		new EntityInteractAtEntity();
	}
	
	public void onDisable() {
		instance = null;
		manager.disable();
	}
	
	public static Main getInstance() { return instance; }
	public static Manager getManager() { return manager; }   
	public ProtocolManager getProtocol() { return protocol; }
	public static ConfigManager getConfigManager() { return config; }
	public static API getAPI() { return api; }
	public static Gson getGson() { return gson; }
}
