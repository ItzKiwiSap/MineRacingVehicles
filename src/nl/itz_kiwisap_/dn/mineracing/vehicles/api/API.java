package nl.itz_kiwisap_.dn.mineracing.vehicles.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import nl.itz_kiwisap_.dn.mineracing.vehicles.Main;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.Vehicle;

public class API {

	public void removeVehicle(Vehicle vehicle) {
		Main.getConfigManager().getConfigFile("storage.yml").set("stored.cars." + vehicle.getVehicleUUID(), null);
		Main.getConfigManager().saveConfigFile("storage.yml");
		vehicle.destroy(true);
		Main.getManager().getVehicles().remove(vehicle);
	}
	
	public List<Vehicle> getPlayerVehicles(Player player) {
		ArrayList<Vehicle> list = new ArrayList<>();
		for(Vehicle vehicle : Main.getManager().getVehicles()) {
			if(vehicle.getOwnerUUID().equalsIgnoreCase(player.getUniqueId().toString())) {
				list.add(vehicle);
			}
		}
		return list;
	}
}
