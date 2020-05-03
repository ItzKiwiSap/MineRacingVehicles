package nl.itz_kiwisap_.dn.mineracing.vehicles.api.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.Vehicle;

public class VehicleDestroyEvent extends Event implements Cancellable {

	private Vehicle vehicle;
	private Boolean cancelled;
	
	private static final HandlerList handlers = new HandlerList();
	
	public VehicleDestroyEvent(Vehicle vehicle) {
		this.vehicle = vehicle;
		this.cancelled = Boolean.valueOf(false);
	}
	
	public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
	
	public Vehicle getVehicle() { return this.vehicle; }
	public boolean isCancelled() { return this.cancelled.booleanValue(); }
}
