package nl.itz_kiwisap_.dn.mineracing.vehicles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import nl.itz_kiwisap_.dn.mineracing.vehicles.api.events.VehicleUpdateEvent;
import nl.itz_kiwisap_.dn.mineracing.vehicles.lib.armorstand.ArmorStand_1_14;
import nl.itz_kiwisap_.dn.mineracing.vehicles.managers.InputManager;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.Vehicle;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.components.Seat;

public class Methods {
	
	public static int round(int i) { return (i / 9 + ((i % 9 == 0) ? 0 : 1)) * 9; }
	
	public static ArmorStand spawnStand(Location location, String string) {
		ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
		stand.setCustomName(string);
		stand.setCustomNameVisible(true);
		stand.setGravity(false);
		stand.setSilent(true);
		stand.setInvulnerable(true);
		stand.setAI(false);
		stand.setVisible(true);
		if(string.equals("DNSEAT"))
			Bukkit.broadcastMessage("Seat spawned at " + location.toString());
		return stand;
	}
	
	public static ArmorStand spawnStand(Location location, boolean paramBoolean, String string) {
	    ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
	    stand.setCustomName(string);
	    stand.setCustomNameVisible(false);
	    stand.setGravity(true);
	    stand.setSilent(true);
	    stand.setInvulnerable(true);
	    stand.setAI(false);
	    stand.setVisible(false);
	    stand.setSmall(paramBoolean);
	    return stand;
	}
	
	public static ArmorStand spawnStand(Location location, boolean paramBoolean, ItemStack item, String string) {
		ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
		stand.setCustomName(string);
		stand.setCustomNameVisible(false);
		stand.setGravity(false);
		stand.setSilent(true);
		stand.setInvulnerable(true);
		stand.setAI(false);
		stand.setVisible(false);
		stand.setSmall(paramBoolean);
		stand.setHelmet(item);
		return stand;
	}
	
	public static void setPosition(ArmorStand stand, Location location) {
		ArmorStand_1_14 armorstand_1_14 = new ArmorStand_1_14();
		armorstand_1_14.setPosition(stand, location);
	}
	
	public static void move(ArmorStand stand, Vector vector) {
		ArmorStand_1_14 armorstand_1_14 = new ArmorStand_1_14();
		armorstand_1_14.move(stand, vector);
	}
	
	public static void handleInput(final boolean w, final boolean a, final boolean s, final boolean d, final boolean space, final Player p) {
		(new BukkitRunnable() {
			@Override
			public void run() {
				if(p.getVehicle() instanceof ArmorStand) {
					ArmorStand stand = (ArmorStand) p.getVehicle();
					if(Main.getManager().getSeats().containsKey(stand)) {
						Seat seat = (Seat) Main.getManager().getSeats().get(stand);
						Vehicle vehicle = seat.getOwner();
						
						InputManager inputManager = new InputManager(w, a, s, d, space);
						VehicleUpdateEvent event = new VehicleUpdateEvent(vehicle, p, seat, inputManager);
						Bukkit.getPluginManager().callEvent(event);
						if(!event.isCancelled()) {
							vehicle.updateMovement(inputManager, p, seat);
						}
					}
				}
			}
		}).runTask((Plugin) Main.getInstance());
	}
	
	public static void handleInput(final boolean w, final boolean a, final boolean s, final boolean d, final boolean space, final ArmorStand v) {
		(new BukkitRunnable() {
			@Override
			public void run() {
				ArmorStand stand = v;
				if(Main.getManager().getSeats().containsKey(stand)) {
					Seat seat = (Seat) Main.getManager().getSeats().get(stand);
					Vehicle vehicle = seat.getOwner();
					
					InputManager inputManager = new InputManager(w, a, s, d, space);
					VehicleUpdateEvent event = new VehicleUpdateEvent(vehicle, null, seat, inputManager);
					Bukkit.getPluginManager().callEvent(event);
					
					if(!event.isCancelled()) {
						vehicle.updateMovement(inputManager, null, seat);
					}
				}
			}
		}).runTask((Plugin) Main.getInstance());
	}
	
	public static Vector addRightVelocity(ArmorStand stand, int paramInt, float paramFloat) {
		Vector vector1 = stand.getVelocity().clone();
		Vector vector2 = vector1.clone();
		
		if(paramInt == 1) {
			vector2.setX(-vector1.getZ());
			vector2.setZ(vector1.getX());
		} else {
			vector2.setX(vector1.getZ());
			vector2.setZ(-vector1.getX());
		}
		
		vector2 = vector2.normalize().multiply(paramFloat);
		return vector2;
	}
}
