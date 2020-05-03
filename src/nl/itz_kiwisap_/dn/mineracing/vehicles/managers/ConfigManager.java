package nl.itz_kiwisap_.dn.mineracing.vehicles.managers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.itz_kiwisap_.dn.mineracing.vehicles.Main;

public class ConfigManager {

	private HashMap<String, FileConfiguration> configs = new HashMap<>();
	
	public FileConfiguration getConfigFile(String string) {
		if(this.configs.containsKey(string)) {
			return this.configs.get(string);
		}
		createConfigFile(string);
		return this.configs.get(string);
	}
	
	public void saveConfigFile(String string) {
		if(this.configs.containsKey(string)) {
			try {
				((FileConfiguration) this.configs.get(string)).save(new File(Main.getInstance().getDataFolder(), string));
			} catch(IOException exception) {
				exception.printStackTrace();
			}
			this.configs.remove(string);
		}
	}
	
	private void createConfigFile(String string) {
		File file = new File(Main.getInstance().getDataFolder(), string);
		if(!file.exists()) {
			file.getParentFile().mkdirs();
			Main.getInstance().saveResource(string, false);
		}
		
		YamlConfiguration configuration = new YamlConfiguration();
		try {
			configuration.load(file);
			this.configs.put(string, configuration);
		} catch(IOException | InvalidConfigurationException exception) {
			exception.printStackTrace();
		}
	}
	
	public HashMap<String, FileConfiguration> getConfigs() { return this.configs; }
}
