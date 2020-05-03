package nl.itz_kiwisap_.dn.mineracing.vehicles.api.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import nl.itz_kiwisap_.dn.mineracing.vehicles.api.enums.CollisionMode;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.Vehicle;

public class VehicleCollisionEvent extends Event implements Cancellable {

	private Vehicle vehicle;
	private Boolean cancelled;
	private Vehicle collisionVehicle;
	private Entity collisionEntity;
	private CollisionMode collisionMode;
	private Boolean push;
	private Boolean damage;
	
	private static final HandlerList handlers = new HandlerList();
	
	public VehicleCollisionEvent(Vehicle vehicle, Vehicle collisionVehicle) {
		this.vehicle = vehicle;
		this.cancelled = Boolean.valueOf(false);
		this.collisionVehicle = collisionVehicle;
		this.collisionMode = CollisionMode.VEHICLE_VEHICLE;
		this.push = Boolean.valueOf(true);
		this.damage = Boolean.valueOf(true);
	}
	
	public VehicleCollisionEvent(Vehicle vehicle, Entity collisionEntity) {
		this.vehicle = vehicle;
		this.cancelled = Boolean.valueOf(false);
		this.collisionEntity = collisionEntity;
		this.collisionMode = CollisionMode.VEHICLE_ENTITY;
		this.push = Boolean.valueOf(true);
		this.damage = Boolean.valueOf(true);
	}
	
	public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerLis() { return handlers; }
	
	public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
	public void setDoPush(boolean push) { this.push = push; }
	public void setDoDamage(boolean damage) { this.damage = damage; } 
	
	public Vehicle getVehicle() { return this.vehicle; }
	public boolean isCancelled() { return this.cancelled.booleanValue(); }
	public Vehicle getCollisionVehicle() { return this.collisionVehicle; }
	public Entity getCollisionEntity() { return this.collisionEntity; }
	public CollisionMode getCollisionMode() { return this.collisionMode; }
	public boolean doPush() { return this.push.booleanValue(); }
	public boolean doDamage() { return this.damage.booleanValue(); }
}
