package nl.itz_kiwisap_.dn.mineracing.vehicles.lib.armorstand;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.Vector;

public interface AArmorStand {
	void setPosition(ArmorStand stand, Location location);
	void move(ArmorStand stand, Vector vector);
}
