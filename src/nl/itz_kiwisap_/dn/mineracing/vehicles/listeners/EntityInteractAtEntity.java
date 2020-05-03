package nl.itz_kiwisap_.dn.mineracing.vehicles.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import nl.itz_kiwisap_.dn.mineracing.vehicles.Main;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.Vehicle;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.components.Seat;

public class EntityInteractAtEntity implements Listener {

	public EntityInteractAtEntity() { Bukkit.getPluginManager().registerEvents(this, Main.getInstance()); }
	
	@EventHandler
	public void onEntityInteractAtEntity(PlayerInteractAtEntityEvent event) {
		if(event.getRightClicked() instanceof ArmorStand) {
			ArmorStand stand = (ArmorStand) event.getRightClicked();
			
			event.getPlayer().sendMessage("true");
			
			if(Main.getManager().getSeats().containsKey(stand)) {
				event.getPlayer().sendMessage("true2");
				event.setCancelled(true);
				Vehicle vehicle = ((Seat) Main.getManager().getSeats().get(stand)).getOwner();
				if(vehicle.isLocked()) {
					event.getPlayer().sendMessage("Locked");
				} else {
					Seat seat = (Seat) Main.getManager().getSeats().get(stand);
					seat.enter(event.getPlayer());
				}
			}
		}
	}
}
