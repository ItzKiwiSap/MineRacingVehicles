package nl.itz_kiwisap_.dn.mineracing.vehicles.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.Gson;

import nl.itz_kiwisap_.dn.mineracing.vehicles.Main;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.Car;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.Vehicle;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.types.BaseType;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.types.FuelType;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.types.RimType;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.types.SpoilerType;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.serializables.Offset;

class VehicleLoaderManager {

	static List<Vehicle> loadAllVehicles() {
		List<Vehicle> list = new ArrayList<>();
		FileConfiguration configuration = Main.getConfigManager().getConfigFile("storage.yml");

		if(configuration.contains("stored.cars")) {
			for(String str1 : configuration.getConfigurationSection("stored.cars").getKeys(false)) {
				String str2 = configuration.getString("stored.cars." + str1);
				Gson gson = Main.getGson();

				try {
					Car car = (Car) gson.fromJson(str2, Car.class);
					car.update();
					list.add(car);
				} catch(Exception exception) {
					exception.printStackTrace();
				}
			}
		}
		return list;
	}

	static LinkedHashMap<String, BaseType> loadAllTypes() {
		File file = new File("plugins/MineRacingVehicles/cars.yml");
		if(file.exists()) {
			FileConfiguration configuration = Main.getConfigManager().getConfigFile("cars.yml");

			if(configuration.contains("cars")) {
				for(String str1 : configuration.getConfigurationSection("cars").getKeys(false)) {
					File file1 = new File("plugins/MineRacingVehicles/Cars/" + str1 + ".yml");
					file1.getParentFile().mkdirs();

					try {
						file1.createNewFile();
					} catch(IOException exception) {
						exception.printStackTrace();
					}

					YamlConfiguration yamlConfiguration = new YamlConfiguration();
					try {
						yamlConfiguration.load(file1);
					} catch(IOException exception) {
						exception.printStackTrace();
					} catch(InvalidConfigurationException exception2) {
						exception2.printStackTrace();
					}

					yamlConfiguration.set("data", configuration.getConfigurationSection("cars." + str1));
					try {
						yamlConfiguration.save(file1);
					} catch(IOException exception) {
						exception.printStackTrace();
					}
				}
			}
		} else {
			File file1 = new File("plugins/MineRacingVehicles/Cars");
			if(!file1.exists()) {
				FileConfiguration fileConfiguration = Main.getConfigManager().getConfigFile("cars.yml");
				if(fileConfiguration.contains("cars")) {
					for(String str1 : fileConfiguration.getConfigurationSection("cars").getKeys(false)) {
						File file2 = new File("plugins/MineRacingVehicles/Cars/" + str1 + ".yml");
						file2.getParentFile().mkdirs();

						try {
							file2.createNewFile();
						} catch(IOException exception) {
							exception.printStackTrace();
						}

						YamlConfiguration yamlConfiguration = new YamlConfiguration();
						try {
							yamlConfiguration.load(file2);
						} catch(IOException exception) {
							exception.printStackTrace();
						} catch(InvalidConfigurationException exception2) {
							exception2.printStackTrace();
						}

						yamlConfiguration.set("data", fileConfiguration.getConfigurationSection("cars." + str1));
						try {
							yamlConfiguration.save(file2);
						} catch(IOException exception) {
							exception.printStackTrace();
						}
					}
				}
			}
		}

		LinkedHashMap<String, BaseType> linkedHashMap = new LinkedHashMap<>();
		loadCars(linkedHashMap);
		return linkedHashMap;
	}

	static void loadCars(LinkedHashMap<String, BaseType> linkedHashMap) {
		File file = new File("plugins/MineRacingVehicles/Cars");
		File[] cars = file.listFiles();

		if(cars != null) {
			for(File car : cars) {
				List<Offset> list;
				List<Offset> seats = new ArrayList<>();
				String str = car.getName().replace(".yml", "");
				FileConfiguration configuration = Main.getConfigManager().getConfigFile("Cars/" + car.getName());

				if(configuration.contains(str)) {
					configuration.set("data", configuration.getConfigurationSection(str));
					configuration.set(str, null);
				}

				byte b = 1;
				for(String str1 : configuration.getConfigurationSection("data.seats").getKeys(false)) {
					Offset offset = new Offset(configuration.getDouble("data.seats." + b + ".forward"), configuration.getDouble("data.seats." + b + ".up"), configuration.getDouble("data.seats." + b + ".left"));
					b++;
					str1.toLowerCase();
					seats.add(offset);
				}

				if(configuration.contains("data.wheels")) {
					list = new ArrayList<>();

					for(String str1 : configuration.getConfigurationSection("data.wheels").getKeys(false)) {
						Offset offset = new Offset(configuration.getDouble("data.wheels." + str1 + ".forward"), configuration.getDouble("data.wheels." + str1 + ".up"), configuration.getDouble("data.wheels." + str1 + ".left"));
						list.add(offset);
					}
				} else {
					list = null;
				}
				Offset offset = null;

				if(configuration.contains("data.spoiler")) {
					offset = new Offset(configuration.getDouble("data.spoiler.forward"), configuration.getDouble("data.spoiler.up"), configuration.getDouble("data.spoiler.left"));
				}

				boolean bool1 = true;
				if(configuration.contains("data.hasSmoke")) {
					bool1 = configuration.getBoolean("data.hasSmoke");
				}

				BaseType baseType = new BaseType(str, configuration.getItemStack("data.skin"), configuration.getDouble("data.speed"), 
						configuration.getDouble("data.acceleration"), configuration.getDouble("data.steering"), seats, configuration.getDouble("data.maxFuel"), 
						configuration.getDouble("data.fuelUsage"), configuration.getString("data.fuelType"), configuration.getInt("data.health"), list, offset);

				baseType.setSmoke(bool1);

				if(configuration.contains("data.canExitWhileMoving")) {
					baseType.setPlayerCanExitWhileMoving(configuration.getBoolean("data.canExitWhileMoving"));
				}

				try {
					configuration.save(car);
				} catch(IOException exception) {
					exception.printStackTrace();
				}
				linkedHashMap.put(str, baseType);
			}
		}
	}

	static LinkedHashMap<String, FuelType> loadAllFuelTypes() {
		LinkedHashMap<String, FuelType> linkedHashMap = new LinkedHashMap<>();
		FileConfiguration fileConfiguration = Main.getConfigManager().getConfigFile("fuels.yml");
		
		for (String str : fileConfiguration.getConfigurationSection("fuels").getKeys(false)) {
			FuelType fuelType = new FuelType(str, fileConfiguration.getItemStack("fuels." + str + ".item"), fileConfiguration.getDouble("fuels." + str + ".price"), fileConfiguration.getDouble("fuels." + str + ".fillAmount"));

			linkedHashMap.put(str, fuelType);
		} 
		return linkedHashMap;
	}

	static LinkedHashMap<String, RimType> loadAllRimTypes() {
		LinkedHashMap<String, RimType> linkedHashMap = new LinkedHashMap<>();
		FileConfiguration fileConfiguration = Main.getConfigManager().getConfigFile("rims.yml");

		for (String str : fileConfiguration.getConfigurationSection("rims").getKeys(false)) {
			RimType rimType = new RimType(str, fileConfiguration.getItemStack("rims." + str + ".item"), Double.valueOf(fileConfiguration.getDouble("rims." + str + ".price")));

			linkedHashMap.put(str, rimType);
		} 
		return linkedHashMap;
	}

	static LinkedHashMap<String, SpoilerType> loadAllSpoilers() {
		LinkedHashMap<String, SpoilerType> linkedHashMap = new LinkedHashMap<>();
		FileConfiguration fileConfiguration = Main.getConfigManager().getConfigFile("spoilers.yml");
		
		for (String str : fileConfiguration.getConfigurationSection("spoilers").getKeys(false)) {
			SpoilerType spoilerType = new SpoilerType(str, fileConfiguration.getItemStack("spoilers." + str + ".item"), Double.valueOf(fileConfiguration.getDouble("spoilers." + str + ".price")));

			linkedHashMap.put(str, spoilerType);
		} 
		return linkedHashMap;
	}
}
