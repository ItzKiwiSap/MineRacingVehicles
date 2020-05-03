package nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.serializables;

import org.bukkit.util.Vector;

public class Offset {

	private double x;
	private double y;
	private double z;
	
	public Offset(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getX() { return x; }
	public double getY() { return y; }
	public double getZ() { return z; }
	public Vector toVector() { return new Vector(this.x, this.y, this.z); }	
}
