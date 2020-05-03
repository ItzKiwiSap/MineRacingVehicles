package nl.itz_kiwisap_.dn.mineracing.vehicles.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import nl.itz_kiwisap_.dn.mineracing.vehicles.managers.InputManager;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.Vehicle;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.components.Seat;

public class VehicleUpdateEvent extends Event implements Cancellable {

	private Vehicle vehicle;
	private Player driver;
	private Seat seat;
	private Boolean cancelled;
	private InputManager inputManager;
	
	private static final HandlerList  handlers = new HandlerList();
	
	public VehicleUpdateEvent(Vehicle vehicle, Player driver, Seat seat, InputManager inputManager) {
		this.vehicle = vehicle;
		this.driver = driver;
		this.seat = seat;
		this.cancelled = Boolean.valueOf(false);
		this.inputManager = inputManager;
	}
	
	public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	public void setCancelled(boolean cancelled) { this.cancelled = Boolean.valueOf(cancelled); }
	
	public Vehicle getVehicle() { return this.vehicle; }
	public Player getDriver() { return this.driver; }
	public Seat getSeat() { return this.seat; }
	public boolean isCancelled() { return this.cancelled; }
	public InputManager getInputManager() { return this.inputManager; }
}
