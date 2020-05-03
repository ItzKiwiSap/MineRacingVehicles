package nl.itz_kiwisap_.dn.mineracing.vehicles.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.itz_kiwisap_.dn.mineracing.vehicles.Main;

public class NewConfigManager {

	private FileConfiguration configuration = null;
	Main plugin;
	private File configFile = null;
	
	public NewConfigManager(Main plugin) { this.plugin = plugin; }
	
	public void reloadConfigFile(String string) {
		if(this.configFile == null) {
			this.configFile = new File(this.plugin.getDataFolder(), string);
		}
		this.configuration = (FileConfiguration) YamlConfiguration.loadConfiguration(this.configFile);
		
		InputStreamReader is = null;
		try {
			try {
				is = new InputStreamReader(new FileInputStream("plugins/MineRacingVehicles/" + string), "UTF8");
			} catch(FileNotFoundException exception) {
				exception.printStackTrace();
			}
		} catch(UnsupportedEncodingException exception) {
			exception.printStackTrace();
		}
		
		if(is != null) {
			YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(this.configFile);
			this.configuration.setDefaults((Configuration) yamlConfiguration);
		}
	}
	
	public FileConfiguration getConfigFile(String string) {
		if(this.configuration == null) {
			reloadConfigFile(string);
		}
		return this.configuration;
	}
	
	public void saveConfigFile(String string) {
		if(this.configuration == null || this.configFile == null) {
			return;
		}
		
		try {
			getConfigFile(string).save(this.configFile);
		} catch(IOException exception) {
			this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, exception);
		}
	}
	
	public void saveDefaultConfigFile(String string) {
		if(this.configuration == null || this.configFile == null) {
			this.configFile = new File(this.plugin.getDataFolder(), string);
		}
		
		if(!this.configFile.exists()) {
			this.plugin.saveResource(string, false);
		}
	}
}
