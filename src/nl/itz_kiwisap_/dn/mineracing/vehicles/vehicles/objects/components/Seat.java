package nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.components;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import nl.itz_kiwisap_.dn.mineracing.vehicles.Main;
import nl.itz_kiwisap_.dn.mineracing.vehicles.Methods;
import nl.itz_kiwisap_.dn.mineracing.vehicles.api.events.VehicleEnterEvent;
import nl.itz_kiwisap_.dn.mineracing.vehicles.api.events.VehicleLeaveEvent;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.Vehicle;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.serializables.Offset;

public class Seat {

	public transient ArmorStand holder;
	private transient Vehicle owner;
	private transient boolean occupied;
	private boolean steer;
	private Offset offset;
	
	public Seat(Offset offset) { this.offset = offset; }
	
	public void spawn(Vehicle vehicle, Location location) {
		this.owner = vehicle;
		this.occupied = false;
		this.holder = Methods.spawnStand(location.clone().add(this.getOffset().toVector()), "DNSEAT");
		Main.getManager().getSeats().put(this.holder, this);
	}
	
	public void setOccupied(boolean occupied) {
		if(this.occupied) {
			VehicleLeaveEvent event = new VehicleLeaveEvent(this.owner, (Player) this.holder.getPassengers().get(0), this);
			Bukkit.getPluginManager().callEvent(event);
		}
		this.occupied = occupied;
	}
	
	public void setSteer(boolean steer) { this.steer = steer; }
	
	public void enter(Player player) {
		VehicleEnterEvent event = new VehicleEnterEvent(this.owner, player, this);
		Bukkit.getPluginManager().callEvent(event);
		
		if(!event.isCancelled()) {
			if(!this.owner.isLocked()) {
				if(isOccupied()) {
					player.sendMessage("§1§lD§9§lN §8» §cThe seat you are tyring to enter is occupied!");
				} else {
					this.holder.addPassenger((Entity) player);
					setOccupied(true);
					if(isSteer()) {
						player.sendMessage("§1§lD§9§lN §8» §fYou entered the driver seat, use §9WASD §fto move the vehicle. And use §9SPACE §fto drift.");
					}
				}
			} else {
				player.sendMessage("§1§lD§9§lN §8» §cThis vehicle is locked!");
			}
		}
	}
	
	public ArmorStand getHolder() { return this.holder; }
	public Vehicle getOwner() { return this.owner; }
	public boolean isOccupied() { return this.occupied; }
	public boolean isSteer() { return this.steer; }
	public Offset getOffset() { return this.offset; }
}
