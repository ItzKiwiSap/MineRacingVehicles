package nl.itz_kiwisap_.dn.mineracing.vehicles.lib.armorstand;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_14_R1.EntityArmorStand;
import net.minecraft.server.v1_14_R1.EnumMoveType;
import net.minecraft.server.v1_14_R1.Vec3D;

public class ArmorStand_1_14 implements AArmorStand {

	public void setPosition(ArmorStand stand, Location location) {
		EntityArmorStand entityArmorStand = ((CraftArmorStand) stand).getHandle();
		entityArmorStand.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}
	
	public void move(ArmorStand stand, Vector vector) {
		EntityArmorStand entityArmorStand = ((CraftArmorStand) stand).getHandle();
		entityArmorStand.move(EnumMoveType.SELF, new Vec3D(vector.getX(), vector.getY(), vector.getZ()));
	}
}
