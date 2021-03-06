package nl.itz_kiwisap_.dn.mineracing.vehicles.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.Vehicle;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.components.Seat;

public class VehicleLeaveEvent extends Event {

	private Vehicle vehicle;
	private Player driver;
	private Seat seat;
	
	private static final HandlerList handlers = new HandlerList();
	
	public VehicleLeaveEvent(Vehicle vehicle, Player driver, Seat seat) {
		this.vehicle = vehicle;
		this.driver = driver;
		this.seat = seat;
	}
	
	public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	public Vehicle getVehicle() { return this.vehicle; }
	public Player getDriver() { return this.driver; }
	public Seat getSeat() { return this.seat; }
}
