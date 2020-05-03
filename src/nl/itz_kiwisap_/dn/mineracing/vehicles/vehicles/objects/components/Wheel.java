package nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.components;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

import nl.itz_kiwisap_.dn.mineracing.vehicles.Main;
import nl.itz_kiwisap_.dn.mineracing.vehicles.Methods;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.Car;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.Vehicle;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.serializables.Offset;

public class Wheel {

	private transient ArmorStand holder;
	private transient Vehicle owner;
	private Offset offset;
	private Float rotation = Float.valueOf(0.0F);
	
	public Wheel(Offset offset) { this.offset = offset; }
	
	public void spawn(Car car, Location location) {
		this.owner = (Vehicle) car;
		this.holder = Methods.spawnStand(location.clone().add(this.offset.toVector()), "DNWHEEL");
		this.holder.setHelmet(Main.getManager().getRimTypeFromString(car.getWheelType()).getItemWithColor(car.getRimRed(), car.getRimGreen(), car.getRimBlue()));
		Main.getManager().getVehiclesLink().put(this.holder, car);
		this.holder.setHeadPose(new EulerAngle(90F, 0F, 0F));
	}
	
	public ArmorStand getHolder() { return this.holder; }
	public Vehicle getOwner() { return this.owner; }
	public Offset getOffset() { return this.offset; }
	public Float getRotation() { return this.rotation; }
	
	public void setRotation(Float rotation) { this.rotation = rotation; }
}
