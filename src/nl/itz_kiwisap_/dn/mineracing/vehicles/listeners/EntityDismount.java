package nl.itz_kiwisap_.dn.mineracing.vehicles.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

import nl.itz_kiwisap_.dn.mineracing.vehicles.Main;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.components.Seat;

public class EntityDismount implements Listener {

	public EntityDismount() { Bukkit.getPluginManager().registerEvents(this, Main.getInstance()); }
	
	@EventHandler
	public void onEntityDismount(EntityDismountEvent event) {
		Entity entity = event.getDismounted();
		if(entity instanceof ArmorStand && Main.getManager().getSeats().containsKey(entity)) {
			Seat seat = (Seat) Main.getManager().getSeats().get(entity);
			seat.setOccupied(false);
		}
	}
}
