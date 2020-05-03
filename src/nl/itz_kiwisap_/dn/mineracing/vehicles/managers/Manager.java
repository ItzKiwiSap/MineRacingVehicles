package nl.itz_kiwisap_.dn.mineracing.vehicles.managers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

import nl.itz_kiwisap_.dn.mineracing.vehicles.Main;
import nl.itz_kiwisap_.dn.mineracing.vehicles.command.VehicleCommand;
import nl.itz_kiwisap_.dn.mineracing.vehicles.lib.inputhandler.InputHandler_1_14;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.Vehicle;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.components.Seat;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.types.BaseType;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.types.FuelType;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.types.RimType;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.types.SpoilerType;

public class Manager {

	private List<Vehicle> vehicles = new ArrayList<>();
	private LinkedHashMap<String, BaseType> baseTypes = new LinkedHashMap<>();
	private LinkedHashMap<String, FuelType> fuelTypes = new LinkedHashMap<>();
	private LinkedHashMap<String, RimType> rimTypes = new LinkedHashMap<>();
	private LinkedHashMap<String, SpoilerType> spoilerTypes = new LinkedHashMap<>();
	private LinkedHashMap<ArmorStand, Seat> seats = new LinkedHashMap<>();
	private LinkedHashMap<ArmorStand, Vehicle> vehiclesLink = new LinkedHashMap<>();
	private boolean isEnabled = false;
	
	public void enable() {
		this.fuelTypes = VehicleLoaderManager.loadAllFuelTypes();
		this.baseTypes = VehicleLoaderManager.loadAllTypes();
		this.rimTypes = VehicleLoaderManager.loadAllRimTypes();
		this.spoilerTypes = VehicleLoaderManager.loadAllSpoilers();
		
		VehicleCommand command = new VehicleCommand();
		Main.getInstance().getCommand("vehicle").setExecutor(command);
		
		this.vehicles.addAll(VehicleLoaderManager.loadAllVehicles());
		Main.getInstance().reloadConfig();
		
		if(!this.isEnabled) {
			InputHandler_1_14 inputHandler = new InputHandler_1_14();
			inputHandler.handleInput();
			this.isEnabled = true;
		}
		
		(new BukkitRunnable() {
			
			@Override
			public void run() {
				for(Vehicle vehicle : Manager.this.vehicles) {
					if(vehicle.isSpawned()) {
						vehicle.updatePositions();
						boolean bool = false;
						
						for(Seat seat : vehicle.getSeats()) {
							if(seat.isOccupied()) {
								bool = true;
								break;
							}
						}
						
						if(!bool) {
							vehicle.setCurSpeed(0.0D);
						}
					}
				}
			}
		}).runTaskTimer(Main.getInstance(), 0L, 1L);
	}
	
	public void disable() {
		for(Vehicle vehicle : this.vehicles) {
			vehicle.destroy(true);
		}
		this.vehicles.clear();
		this.fuelTypes.clear();
		this.baseTypes.clear();
		Main.getConfigManager().getConfigs().clear();
	}
	
	public BaseType getBaseTypeFromString(String string) { return this.baseTypes.get(string); }

	@SuppressWarnings("rawtypes")
	public FuelType getFuelTypeFromString(String string) {
		if (this.fuelTypes.containsKey(string)) {
			return this.fuelTypes.get(string);
		}
		return (FuelType)((Map.Entry)this.fuelTypes.entrySet().iterator().next()).getValue();
	}
	
	public RimType getRimTypeFromString(String string) { return this.rimTypes.get(string); }
	public SpoilerType getSpoilerFromString(String string) { return this.spoilerTypes.get(string); }
	public List<Vehicle> getVehicles() { return this.vehicles; }
	public LinkedHashMap<String, BaseType> getBaseTypes() { return this.baseTypes; }
	public LinkedHashMap<String, FuelType> getFuelTypes() { return this.fuelTypes; }
	public LinkedHashMap<String, RimType> getRimTypes() { return this.rimTypes; }
	public LinkedHashMap<String, SpoilerType> getSpoilerTypes() { return this.spoilerTypes; }
	public LinkedHashMap<ArmorStand, Seat> getSeats() { return this.seats; }
	public LinkedHashMap<ArmorStand, Vehicle> getVehiclesLink() { return this.vehiclesLink; }
}
