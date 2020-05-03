package nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import nl.itz_kiwisap_.dn.mineracing.vehicles.Main;
import nl.itz_kiwisap_.dn.mineracing.vehicles.Methods;
import nl.itz_kiwisap_.dn.mineracing.vehicles.api.events.VehicleCollisionEvent;
import nl.itz_kiwisap_.dn.mineracing.vehicles.api.events.VehicleDamageEvent;
import nl.itz_kiwisap_.dn.mineracing.vehicles.api.events.VehicleDestroyEvent;
import nl.itz_kiwisap_.dn.mineracing.vehicles.api.events.VehicleSpawnEvent;
import nl.itz_kiwisap_.dn.mineracing.vehicles.managers.InputManager;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.components.Seat;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.types.BaseType;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.serializables.Offset;

public abstract class Vehicle {

	private transient ArmorStand host;
	private transient ArmorStand skin;
	private List<Seat> seats;
	private double speed;
	private double traction;
	private double steering;
	private double maxFuel;
	private double fuelLevel;
	private String vehicleUUID;
	private String ownerUUID;
	private String type;
	
	private boolean broken = false;
	private boolean locked = false;
	private boolean spawned = false;
	private int mainRed = 100;
	private int mainGreen = 100;
	private int mainBlue = 100;
	private int health = 100;
	private int rimFRHealth = 100;
	private int rimFLHealth = 100;
	private int rimRRHealth = 100;
	private int rimRLHealth = 100;
	private double curSpeed = 0.0D;
	
	public Vehicle(BaseType type, Player player) {
		boolean bool = false;
		this.seats = new ArrayList<>();
		for(Offset offset : type.getSeats()) {
			Seat seat = new Seat(offset);
			if(!bool) {
				bool = true;
				seat.setSteer(true);
			}
			this.seats.add(seat);
		}
		
		this.type = type.getName();
		this.speed = type.getSpeed();
		this.traction = type.getAcceleration();
		this.steering = type.getSteering();
		this.vehicleUUID = UUID.randomUUID().toString();
		this.ownerUUID = player.getUniqueId().toString();
		this.maxFuel = type.getMaxFuel();
		this.fuelLevel = type.getMaxFuel() / 2.0D;
	}
	
	public void update() {
		boolean bool = false;
		this.seats.clear();
		this.seats = new ArrayList<>();
		for(Offset offset : getBaseType().getSeats()) {
			Seat seat = new Seat(offset);
			if(!bool) {
				bool = true;
				seat.setSteer(true);
			}
			this.seats.add(seat);
		}
		this.speed = getBaseType().getSpeed();
		this.traction = getBaseType().getAcceleration();
		this.steering = getBaseType().getSteering();
	}
	
	public boolean spawn(Location location, Player player) {
		VehicleSpawnEvent event = new VehicleSpawnEvent(this);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()) {
			if(!this.spawned) {
				for(Seat seat : this.seats) {
					seat.spawn(this, location);
				}
				this.skin = Methods.spawnStand(location, false, getBaseType().getItemWithColor(this.getMainRed(), this.getMainGreen(), this.getMainBlue()), "DNSKIN#" + this.getVehicleUUID());
				this.host = Methods.spawnStand(location, true, "DNHOST#" + this.getVehicleUUID());
				this.spawned = true;
				this.health = this.getBaseType().getHealth();
				(new BukkitRunnable() {
					@Override
					public void run() {
						Vehicle.this.updatePositions();
						Main.getManager().getVehiclesLink().put(Vehicle.this.skin, Vehicle.this);
						Main.getManager().getVehiclesLink().put(Vehicle.this.host, Vehicle.this);
					}
				}).runTaskLater(Main.getInstance(), 10L);
				
				for(Seat seat : this.seats) {
					seat.getHolder().setHealth(getHealth() / getBaseType().getHealth() * 20.0D);
				}
				setLocked(true);
				return true;
			}
			return false;
		}
		return false;
	}
	
	public void updateSkin() {
		if(this.spawned) {
			getSkin().setHelmet(getBaseType().getItemWithColor(getMainRed(), getMainGreen(), getMainBlue()));
		}
	}
	
	public void updatePositions() {
		if(this.spawned) {
			for(Seat seat : this.seats) {
				Location loc3 = getHost().getLocation().clone();
				Location loc4 = loc3.add(loc3.getDirection().setY(0).normalize().multiply(seat.getOffset().getX()));
				float f1 = (float)(loc4.getZ() + seat.getOffset().getZ() * Math.sin(Math.toRadians(loc4.getYaw())));
				float f2 = (float)(loc4.getX() * seat.getOffset().getZ() * Math.cos(Math.toRadians(loc4.getYaw())));
				Location loc5 = new Location(seat.getHolder().getWorld(), f2, getHost().getLocation().getY() + seat.getOffset().getY(), f1, loc4.getYaw(), loc4.getPitch());
				Methods.setPosition(seat.getHolder(), loc5);
			}
			Methods.setPosition(this.skin, this.host.getLocation().clone());
			Location loc1 = getHost().getLocation().clone();
			Location loc2 = loc1.add(loc1.getDirection().setY(-0.2D).normalize().multiply(-3));
			if(this.getBaseType().hasSmoke()) {
				loc2.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc2, 10, 0.01D, 0.01D, 0.01D, 0.0D);
			}
		}
		handleCollision();
	}
	
	private void handleCollision() {
		if(getCurSpeed() > 0.0D) {
			Location loc1 = this.host.getLocation().clone().add(2.0D, 2.0D, 2.0D);
			Location loc2 = this.host.getLocation().clone().add(-2.0D, 0.0D, -2.0D);
			try {
				for(Entity entity : this.host.getWorld().getNearbyEntities(this.host.getLocation(), 10.0D, 10.0D, 10.0D)) {
					if(entity instanceof ArmorStand) {
						ArmorStand armorStand = (ArmorStand) entity;
						
						if(armorStand != getHost()
								&& armorStand.getCustomName() != null
								&& armorStand.getCustomName().equalsIgnoreCase("DNHOST")
								&& Main.getManager().getVehiclesLink().containsKey(armorStand)) {
							Vehicle vehicle = (Vehicle) Main.getManager().getVehiclesLink().get(armorStand);
							if(vehicle != this) {
								ArrayList<Location> arrayList = new ArrayList<>();
								arrayList.add(vehicle.getHost().getLocation().clone().add(2.0D, 2.0D, 2.0D));
				                arrayList.add(vehicle.getHost().getLocation().clone().add(2.0D, 2.0D, -2.0D));
				                arrayList.add(vehicle.getHost().getLocation().clone().add(-2.0D, 2.0D, 2.0D));
				                arrayList.add(vehicle.getHost().getLocation().clone().add(-2.0D, 2.0D, -2.0D));
				                arrayList.add(vehicle.getHost().getLocation().clone().add(2.0D, 0.0D, 2.0D));
				                arrayList.add(vehicle.getHost().getLocation().clone().add(2.0D, 0.0D, -2.0D));
				                arrayList.add(vehicle.getHost().getLocation().clone().add(-2.0D, 0.0D, 2.0D));
				                arrayList.add(vehicle.getHost().getLocation().clone().add(-2.0D, 0.0D, -2.0D));
				                
				                for(Location location : arrayList) {
				                	if(location.getX() <= loc1.getX() && location.getY() <= loc1.getY() && location.getZ() <= loc1.getZ()) {
				                		if(location.getX() >= loc2.getX() && location.getY() >= loc2.getY() && location.getZ() >= loc2.getZ()) {
				                			VehicleCollisionEvent event = new VehicleCollisionEvent(this, vehicle);
				                			Bukkit.getPluginManager().callEvent(event);
				                			if(!event.isCancelled()) {
				                				if(event.doPush()) {
				                					if(this.getCurSpeed() > 0.0D) {
				                						vehicle.getHost().setVelocity(vehicle.getHost().getLocation().toVector().subtract(this.getHost().getLocation().toVector()).normalize().multiply(2));
				                					} else if(this.getCurSpeed() < 0.0D) {
				                						vehicle.getHost().setVelocity(vehicle.getHost().getLocation().toVector().subtract(this.getHost().getLocation().toVector()).normalize().multiply(2));
				                					}
				                				}
				                				
				                				if(event.doDamage()) {
				                					damage((int) (this.getCurSpeed() * 10.0D));
				                					vehicle.damage((int) (this.getCurSpeed() * 5.0D));
				                				}
				                			}
				                		}
				                	}
				                }
							}
						}
						continue;
					}
					
					if(entity instanceof LivingEntity) {
						LivingEntity livingEntity = (LivingEntity) entity;
						Location location = livingEntity.getLocation();
						
						if(!livingEntity.isInsideVehicle()
								&& location.getX() <= loc1.getX() && location.getY() <= loc1.getY() && location.getZ() <= loc1.getZ()) {
							if(location.getX() >= loc2.getX() && location.getY() >= loc2.getY() && location.getZ() >= loc2.getZ()) {
								VehicleCollisionEvent event = new VehicleCollisionEvent(this, (Entity) livingEntity);
								Bukkit.getPluginManager().callEvent(event);
								
								if(!event.isCancelled()) {
									if(this.getCurSpeed() > 0.0D || this.getCurSpeed() < 0.0D) {
										if(event.doPush()) {
											setCurSpeed(-1.0D);
											Vector vector = livingEntity.getLocation().toVector().subtract(this.getHost().getLocation().toVector()).normalize().multiply(0.4D);
											vector.setY(0.4D);
											livingEntity.setVelocity(vector);
											this.getHost().setVelocity(new Vector(0, 0, 0));
										}
										
										if(event.doDamage()) {
											livingEntity.damage(10.0D);
											damage(10);
										}
										continue;
									}
									
									if(event.doPush()) {
										Vector vector = livingEntity.getLocation().toVector().subtract(this.getHost().getLocation().toVector()).normalize().multiply(0.1D);
										vector.setY(0);
										livingEntity.setVelocity(vector);
									}
								}
							}
						}
					}
				}
			} catch(Exception exception) { }
		}
	}
	
	private void damage(int damage) {
		VehicleDamageEvent event = new VehicleDamageEvent(this, damage);
		Bukkit.getPluginManager().callEvent(event);
		
		if(!event.isCancelled()) {
			this.setHealth(this.getHealth() - event.getDamage());
			if(this.getHealth() <= 0) {
				this.setCurSpeed(0.0D);
				this.getHost().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, this.getHost().getLocation(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
				this.broken = true;
				destroy(false);
			} else {
				for(Seat seat : this.getSeats()) {
					seat.getHolder().setHealth(this.getHealth() / this.getBaseType().getHealth() * 20.0D);
				}
			}
		}
	}
	
	public void collideWithWall() {
		if(getCurSpeed() > 0.0D) {
			damage((int) getCurSpeed() * 15);
		} else if(getCurSpeed() < 0.0D) {
			damage((int) getCurSpeed() * -15);
		}
		setCurSpeed(0.0D);
	}
	
	public boolean destroy(boolean destroy) {
		VehicleDestroyEvent event = new VehicleDestroyEvent(this);
		Bukkit.getPluginManager().callEvent(event);
		
		if(!event.isCancelled() || destroy) {
			if(this.spawned) {
				Main.getManager().getVehiclesLink().remove(this.host);
				Main.getManager().getVehiclesLink().remove(this.skin);
				
				if(this.host != null) this.host.remove();
				if(this.skin != null) this.skin.remove();
				
				for(Seat seat : this.getSeats()) {
					if(seat.getHolder() != null) {
						Main.getManager().getSeats().remove(seat.getHolder());
						seat.getHolder().remove();
					}
				}
				this.spawned = false;
			}
			return true;
		}
		return false;
	}
	
	public BaseType getBaseType() { return Main.getManager().getBaseTypeFromString(this.type); }
	public List<Seat> getSeats() { return this.seats; }
	public double getBaseSpeed() { return this.speed; }
	public double getSpeed() { return this.speed / 20.0D; }
	public double getBaseTraction() { return this.traction; }
	public double getTraction() { double d = this.traction * 20.0D; return 5.0D / d; }
	public double getSteering() { return this.steering; }
	public String getVehicleUUID() { return this.vehicleUUID; }
	public String getOwnerUUID() { return this.ownerUUID; }
	public double getMaxFuel() { return this.maxFuel; }
	public double getFuelLevel() { return this.fuelLevel; }
	public ArmorStand getHost() { return this.host; }
	public ArmorStand getSkin() { return this.skin; }
	public boolean isLocked() { return this.locked; }
	public boolean isSpawned() { return this.spawned; }
	public boolean isBroken() { return this.broken; }
	private int getHealth() { return this.health; }
	public double getCurSpeed() { return this.curSpeed; }
	public int getMainRed() { return this.mainRed; }
	public int getMainGreen() { return this.mainGreen; }
	public int getMainBlue() { return this.mainBlue; }
	public int getRimFRHealth() { return this.rimFRHealth; }
	public int getRimFLHealth() { return this.rimFLHealth; }
	public int getRimRRHealth() { return this.rimRRHealth; }
	public int getRimRLHealth() { return this.rimRLHealth; }
	
	private void setHealth(int health) { this.health = health; }
	public void setCurSpeed(double curspeed) { this.curSpeed = curspeed; }
	public void setFuelLevel(double fuellevel) { this.fuelLevel = fuellevel; }
	public void setLocked(boolean locked) { this.locked = locked; }
	public void setMainRed(int red) { this.mainRed = red; }
	public void setMainGreen(int green) { this.mainGreen = green; }
	public void setMainBlue(int blue) { this.mainBlue = blue; }
	public void setRimFRHealth(int fr) { this.rimFRHealth = fr; }
	public void setRimFLHealth(int fl) { this.rimFLHealth = fl; }
	public void setRimRRHealth(int rr) { this.rimRRHealth = rr; }
	public void setRimRLHealth(int rl) { this.rimRLHealth = rl; }
	public void setSpeed(double speed) { this.speed = speed; }
	public void setTraction(double traction) { this.traction = traction; }
	public void setSteering(double steering) { this.steering = steering; }
	public void setMaxFuel(double maxfuel) { this.maxFuel = maxfuel; }
	public void setOwnerUUID(String uuid) { this.ownerUUID = uuid; }
	public void setBroken(boolean broken) { this.broken = broken; }
	
	public abstract void updateMovement(InputManager inputManager, Player player, Seat seat);
}