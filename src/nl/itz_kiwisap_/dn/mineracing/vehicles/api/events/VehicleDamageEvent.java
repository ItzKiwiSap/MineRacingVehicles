package nl.itz_kiwisap_.dn.mineracing.vehicles.api.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.Vehicle;

public class VehicleDamageEvent extends Event implements Cancellable {

	private Vehicle vehicle;
	private Boolean cancelled;
	private int damage;
	
	private static final HandlerList handlers = new HandlerList();
	
	public VehicleDamageEvent(Vehicle vehicle, int damage) {
		this.vehicle = vehicle;
		this.cancelled = Boolean.valueOf(false);
		this.damage = damage;
	}
	
	public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
	public void setDamage(int damage) { this.damage = damage; }
	
	public Vehicle getVehicle() { return this.vehicle; }
	public boolean isCancelled() { return this.cancelled.booleanValue(); }
	public int getDamage() { return this.damage; }
}
