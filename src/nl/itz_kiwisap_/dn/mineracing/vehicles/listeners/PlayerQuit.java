package nl.itz_kiwisap_.dn.mineracing.vehicles.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import nl.itz_kiwisap_.dn.mineracing.vehicles.Main;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.Vehicle;

public class PlayerQuit implements Listener {

	public PlayerQuit() { Bukkit.getPluginManager().registerEvents(this, Main.getInstance()); }
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		for(Vehicle vehicle : Main.getAPI().getPlayerVehicles(event.getPlayer())) {
			vehicle.destroy(true);
		}
	}
}
