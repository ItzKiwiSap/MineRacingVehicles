package nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.types;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.serializables.Offset;

public class BaseType {

	private ItemStack skin;
	private String name;
	private double speed;
	private double acceleration;
	private double steering;
	private String fuelType;
	private double maxFuel;
	private double fuelUsage;
	private List<Offset> seats;
	private int health;
	private Offset spoiler;
	private List<Offset> wheels;
	private boolean playerCanExitWhileMoving = false;
	private boolean hasSmoke;
	
	public ItemStack getItemWithColor(int r, int g, int b) {
		ItemStack item = this.skin.clone();
		if(item.getType().name().contains("RABBIT_")) {
			ItemMeta meta = item.getItemMeta();
			item.setItemMeta((ItemMeta) meta);
		}
		return item;	
	}
	
	public BaseType(String name, ItemStack skin, double speed, double acceleration, double steering, List<Offset> seats, double maxFuel, double fuelUsage, String fuelType, int health, List<Offset> wheels, Offset spoiler) {
		this.name = name;
		this.skin = skin;
		this.speed = speed;
		this.acceleration = acceleration;
		this.steering = steering;
		this.seats = seats;
		this.maxFuel = maxFuel;
		this.fuelUsage = fuelUsage;
		this.fuelType = fuelType;
		this.health = health;
		this.wheels = wheels;
		this.spoiler = spoiler;
		Bukkit.broadcastMessage("basetype loaded");
		this.seats.forEach(seat -> {
			Bukkit.broadcastMessage("" + seat.toVector().toString());
		});
	}
	
	public ItemStack getSkin() { return this.skin; }
	public double getSpeed() { return this.speed; }
	public double getAcceleration() { return this.acceleration; }
	public double getSteering() { return this.steering; }
	public String getName() { return this.name; }
	public List<Offset> getSeats() { return this.seats; }
	public double getMaxFuel() { return this.maxFuel; }
	public double getFuelUsage() { return this.fuelUsage; }
	public String getFuelType() { return this.fuelType; }
	public int getHealth() { return this.health; }
	public boolean isPlayerCanExitWhileMoving() { return this.playerCanExitWhileMoving; }
	public Offset getSpoiler() { return this.spoiler; }
	public List<Offset> getWheels() { return this.wheels; }
	public boolean hasSmoke() { return this.hasSmoke; }
	
	public void setPlayerCanExitWhileMoving(boolean bool) { this.playerCanExitWhileMoving = bool; }
	public void setSmoke(boolean bool) { this.hasSmoke = bool; }
}
