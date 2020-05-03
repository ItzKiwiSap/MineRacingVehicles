package nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import nl.itz_kiwisap_.dn.mineracing.vehicles.Main;
import nl.itz_kiwisap_.dn.mineracing.vehicles.Methods;
import nl.itz_kiwisap_.dn.mineracing.vehicles.managers.InputManager;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.Vehicle;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.components.Seat;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.components.Wheel;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.types.BaseType;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.serializables.Offset;

public class Car extends Vehicle {

	private transient List<Wheel> wheels = null;
	private boolean canDrift;
	private boolean hasWheels;
	private Offset spoiler;
	private String wheelType;
	private String spoilerType;
	private int rimRed = 100;
	private int rimGreen = 100;
	private int rimBlue = 100;
	private int spoilerRed = 100;
	private int spoilerGreen = 100;
	private int spoilerBlue = 100;
	transient int counter = 0;
	private transient ArmorStand spoilerStand;
	private transient float wheelRot;

	public Car(BaseType type, Player player) {
		super(type, player);

		this.wheelRot = 0.0F;

		if(type.getWheels() == null || type.getWheels().isEmpty()) {
			this.hasWheels = false;
		} else {
			this.wheels = new ArrayList<>();
			this.hasWheels = true;

			for(Offset offset : type.getWheels()) {
				Wheel wheel = new Wheel(offset);
				this.wheels.add(wheel);
			}
		}

		this.canDrift = true;
		this.wheelType = "Default";
	}

	public boolean spawn(Location location, Player player) {
		boolean bool = super.spawn(location, player);

		if(bool) {
			if(getBaseType().getSpoiler() == null) {
				this.spoiler = null;
			} else {
				this.spoiler = getBaseType().getSpoiler();
			}

			if(this.spoilerType == null) this.spoilerType = "None";

			if(getBaseType().getWheels() == null || getBaseType().getWheels().isEmpty()) {
				this.hasWheels = false;
			} else {
				this.wheels = new ArrayList<>();
				this.hasWheels = true;

				for(Offset offset : getBaseType().getWheels()) {
					Wheel wheel = new Wheel(offset);
					this.wheels.add(wheel);
				}
			}

			byte b = 0;

			if(this.wheels != null) {
				for(Wheel wheel : this.wheels) {
					if(b == 1 || b == 3) wheel.setRotation(Float.valueOf(100.0F));
					b++;
					wheel.spawn(this, location);
				}
			}

			if(this.spoiler != null && this.spoilerType != null && !this.spoilerType.equalsIgnoreCase("none")) {
				this.spoilerStand = Methods.spawnStand(getHost().getLocation().clone().add(this.spoiler.toVector()), "DNSPOILER");
				this.spoilerStand.setHelmet(Main.getManager().getSpoilerFromString(this.spoilerType).getItemWithColor(getSpoilerRed(), getSpoilerGreen(), getSpoilerBlue()));
				Main.getManager().getVehiclesLink().put(this.spoilerStand, this);
			}
		}
		return bool;
	}

	public void updateSkin() {
		super.updateSkin();
		if(isSpawned()) {
			if(this.spoiler != null) {
				if(this.spoilerStand != null) {
					if(!this.spoilerType.equalsIgnoreCase("none")) 
						this.spoilerStand.setHelmet(Main.getManager().getSpoilerFromString(this.spoilerType).getItemWithColor(getSpoilerRed(), getSpoilerGreen(), getSpoilerBlue()));
				} else if(!this.spoilerType.equalsIgnoreCase("none")) {
					this.spoilerStand = Methods.spawnStand(getHost().getLocation().clone().add(this.spoiler.toVector()), "DNSPOILER");
					this.spoilerStand.setHelmet(Main.getManager().getSpoilerFromString(this.spoilerType).getItemWithColor(getSpoilerRed(), getSpoilerGreen(), getSpoilerBlue()));
					Main.getManager().getVehiclesLink().put(this.spoilerStand, this);
				}

				if(this.hasWheels) {
					for(Wheel wheel : this.wheels) {
						wheel.getHolder().setHelmet(Main.getManager().getRimTypeFromString(this.wheelType).getItemWithColor(getRimRed(), getRimGreen(), getRimBlue()));
					}
				}
			}
		}
	}

	public void updateMovement(InputManager inputManager, Player player, Seat seat) {
		if(seat.isSteer()) {
			Car car = this;
			boolean bool1 = inputManager.isW();
			boolean bool2 = inputManager.isA();
			boolean bool3 = inputManager.isS();
			boolean bool4 = inputManager.isD();
			boolean bool5 = inputManager.isSpace();

			car.updatePositions();
			if(car.hasWheels) {
				byte b = 0;
				for(Wheel wheel : car.wheels) {
					if(b <= 1) {
						if(b == 1) {
							if(bool2) {
								wheel.setRotation(Float.valueOf(150.0F));
							} else if(bool4) {
								wheel.setRotation(Float.valueOf(210.0F));
							} else {
								wheel.setRotation(Float.valueOf(180.0F));
							}
						} else if(bool2) {
							wheel.setRotation(Float.valueOf(-30.0F));
						} else if(bool4) {
							wheel.setRotation(30.0F);
						} else {
							wheel.setRotation(0.0F);
						}
						b++;
					}
				}
			}

			if(car.getFuelLevel() > 0.0D || car.getBaseType().getFuelType().equalsIgnoreCase("none")) {
				if(bool5 && !car.canDrift) {
					car.getHost().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 20.0F, 10.0F);
				}
				if(bool1) {
					if(car.getCurSpeed() < car.getSpeed()) {
						if(car.getCurSpeed() < 0.0D) {
							car.setCurSpeed(car.getCurSpeed() + car.getTraction() * 5.0D);
						} else {
							car.setCurSpeed(car.getCurSpeed() + car.getTraction());
						}
					} else {
						car.setCurSpeed(car.getSpeed());
					}
				} else if(bool3) {
					if(car.getCurSpeed() > -(car.getSpeed() / 4.0D)) {
						if(car.getCurSpeed() > 0.0D) {
							car.setCurSpeed(car.getCurSpeed() - car.getTraction() * 5.0D);
						} else {
							car.setCurSpeed(car.getCurSpeed() - car.getTraction());
						}
					} else {
						car.setCurSpeed(-(car.getSpeed() / 4.0D));
					}
				} else if(car.getCurSpeed() > 0.0D && car.getCurSpeed() - car.getTraction() > 0.0D) {
					car.setCurSpeed(car.getCurSpeed() - car.getTraction());
				} else if(car.getCurSpeed() < 0.0D && car.getCurSpeed() + car.getTraction() < 0.0D) {
					car.setCurSpeed(car.getCurSpeed() + car.getTraction());
				} else {
					car.setCurSpeed(0.0D);
				}
				
				Vector vector1 = new Vector();
				if(bool2) {
					Location location = car.getHost().getLocation().clone();
					if(car.getCurSpeed() > 0.0D) {
						if(bool5 && car.canDrift) {
							location.setYaw((float) (location.getYaw() - car.getSteering() * 2.0D));
							vector1 = Methods.addRightVelocity(car.getHost(), 1, (float) car.getCurSpeed() / 5.0F);
							if(car.hasWheels) {
								((Wheel) car.wheels.get(2)).getHolder().getWorld().spawnParticle(Particle.SMOKE_NORMAL, ((Wheel) car.wheels.get(2)).getHolder().getLocation(), 20, 0.5D, 0.5D, 0.5D, 0.0D);
								((Wheel) car.wheels.get(3)).getHolder().getWorld().spawnParticle(Particle.SMOKE_NORMAL, ((Wheel) car.wheels.get(3)).getHolder().getLocation(), 20, 0.5D, 0.5D, 0.5D, 0.0D);
							}
						} else {
							location.setYaw((float) (location.getYaw() - car.getSteering()));
							vector1 = Methods.addRightVelocity(car.getHost(), -1, (float) car.getCurSpeed() / 20.0F);
						}
					} else if(car.getCurSpeed() < 0.0D) {
						location.setYaw((float) (location.getYaw() + car.getSteering()));
					}
					Methods.setPosition(car.getHost(), location);
				} 
				if(bool4) {
					Location location = car.getHost().getLocation().clone();
					if(car.getCurSpeed() > 0.0D) {
						if(bool5 && car.canDrift) {
							location.setYaw((float) (location.getYaw() + car.getSteering() * 2.0D));
							vector1 = Methods.addRightVelocity(car.getHost(), -1, (float) car.getCurSpeed() / 5.0F);
							if(car.hasWheels) {
								((Wheel) car.wheels.get(2)).getHolder().getWorld().spawnParticle(Particle.SMOKE_NORMAL, ((Wheel) car.wheels.get(2)).getHolder().getLocation(), 20, 0.5D, 0.5D, 0.5D, 0.0D);
								((Wheel) car.wheels.get(3)).getHolder().getWorld().spawnParticle(Particle.SMOKE_NORMAL, ((Wheel) car.wheels.get(3)).getHolder().getLocation(), 20, 0.5D, 0.5D, 0.5D, 0.0D);
							}
						} else {
							location.setYaw((float) (location.getYaw() + car.getSteering()));
							vector1 = Methods.addRightVelocity(car.getHost(), 1, (float) car.getCurSpeed() / 20.0F);
						}
					} else if(car.getCurSpeed() < 0.0D) {
						location.setYaw((float) (location.getYaw() - car.getSteering()));
					}
					Methods.setPosition(car.getHost(), location);
				}
				car.getHost().setGravity(true);
				Vector vector2 = (new Vector(car.getHost().getLocation().getDirection().multiply(0.5D).getX(), -1.0D * car.getCurSpeed(), car.getHost().getLocation().getDirection().multiply(0.5D).getZ())).multiply(car.getCurSpeed());
				boolean bool = false;
				try {
					NumberConversions.isFinite(vector2.getX());
					NumberConversions.isFinite(vector2.getY());
					NumberConversions.isFinite(vector2.getZ());
					bool = true;
				} catch(Exception exception) {}
				
				if(bool) {
					car.getHost().setVelocity(vector2.add(vector1));
					car.updatePositions();
					
					if(car.getCurSpeed() > 0.0D) {
						for(byte b = 1; b <= 1; b++) {
							Location location1 = getHost().getLocation().clone().add(0.0D, 0.2D, 0.0D);
							Location location2 = location1.add(location1.getDirection().setY(0).normalize().multiply(b));
							float f1 = (float) (location2.getZ() + 0.0D * Math.sin(Math.toRadians(location2.getYaw())));
							float f2 = (float) (location2.getX() + 0.0D * Math.cos(Math.toRadians(location2.getYaw())));
							Location location3 = new Location(getHost().getWorld(), f2, getHost().getLocation().getY(), f1, location2.getYaw(), location2.getPitch());
							
							if(!location3.clone().add(new Vector(0, 1, 0)).getBlock().getType().isSolid() && !location3.clone().add(new Vector(0, 2, 0)).getBlock().getType().isSolid()) {
								if(location3.getBlock().getType().name().contains("SLAB") || location3.getBlock().getType().name().contains("STEP")) {
									Location location = location3.clone();
									location.setY(location3.getBlockY() + 0.52D);
									Methods.setPosition(getHost(), location);
								} else if(location3.getBlock().getType().isSolid() && !location3.getBlock().getType().name().contains("FENCE") && !location3.getBlock().getType().name().contains("WALL")) {
									collideWithWall();
								}
							}
						}
					} else if(car.getCurSpeed() < 0.0D) {
						for(byte b = 1; b <= 1; b++) {
							Location location1 = getHost().getLocation().clone().add(0.0D, 0.2D, 0.0D);
							Location location2 = location1.add(location1.getDirection().setY(0).normalize().multiply(-b));
							float f1 = (float) (location2.getZ() + 0.0D * Math.sin(Math.toRadians(location2.getYaw())));
							float f2 = (float) (location2.getX() + 0.0D * Math.cos(Math.toRadians(location2.getYaw())));
							Location location3 = new Location(getHost().getWorld(), f2, getHost().getLocation().getY(), f1, location2.getYaw(), location2.getPitch());
							
							if(!location3.clone().add(new Vector(0, 1, 0)).getBlock().getType().isSolid() && !location3.clone().add(new Vector(0, 2, 0)).getBlock().getType().isSolid()) {
								if(location3.getBlock().getType().name().contains("SLAB") || location3.getBlock().getType().name().contains("STEP")) {
									Location location = location3.clone();
									location.setY(location3.getBlockY() + 0.52D);
									Methods.setPosition(getHost(), location);
								} else if(location3.getBlock().getType().isSolid() && !location3.getBlock().getType().name().contains("FENCE") && !location3.getBlock().getType().name().contains("WALL")) {
									collideWithWall();
								}
							}
						}
					}
					if(car.getBaseType().getFuelType().equalsIgnoreCase("none")) {
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("&fCurrent speed&7: &9" + car.getCurSpeed() * 20.0D + " KM/H"));
					} else if(car.getCurSpeed() > 0.0D) {
						if(car.getFuelLevel() > 0.0D) {
							car.setFuelLevel(car.getFuelLevel() - car.getBaseType().getFuelUsage() / 3000.0D * car.getCurSpeed());
						} else {
							car.setFuelLevel(0.0D);
						}
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("&fCurrent speed&7: &9" + car.getCurSpeed() * 20.0D + " KM/H"));
					} else {
						if(car.getFuelLevel() > 0.0D) {
							car.setFuelLevel(car.getFuelLevel() - car.getBaseType().getFuelUsage() / 3000.0D * car.getCurSpeed());
						} else {
							car.setFuelLevel(0.0D);
						}
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("&fCurrent speed&7: &9" + car.getCurSpeed() * 20.0D + " KM/H"));
					}
				}
			} else {
				car.setFuelLevel(0.0D);
				
				if(car.getCurSpeed() > 0.0D && car.getCurSpeed() - car.getTraction() > 0.0D) {
					car.setCurSpeed(car.getCurSpeed() - car.getTraction());
				} else if(car.getCurSpeed() < 0.0D && car.getCurSpeed() + car.getTraction() < 0.0D) {
					car.setCurSpeed(car.getCurSpeed() + car.getTraction());
				} else {
					car.setCurSpeed(0.0D);
				}
				car.getHost().setVelocity((new Vector(car.getHost().getLocation().getDirection().multiply(0.5D).getX(), -1.0D * car.getCurSpeed(), car.getHost().getLocation().getDirection().multiply(0.5D).getZ())).multiply(car.getCurSpeed()));
				
				if(bool2) {
					Location location = car.getHost().getLocation().clone();
					if(car.getCurSpeed() > 0.0D) {
						location.setYaw((float) (location.getYaw() - car.getSteering()));
					} else if(car.getCurSpeed() < 0.0D) {
						location.setYaw((float) (location.getYaw() + car.getSteering()));
					}
					Methods.setPosition(car.getHost(), location);
				}
				if(bool4) {
					Location location = car.getHost().getLocation().clone();
					if(car.getCurSpeed() > 0.0D) {
						location.setYaw((float) (location.getYaw() + car.getSteering()));
					} else if(car.getCurSpeed() < 0.0D) {
						location.setYaw((float) (location.getYaw() - car.getSteering()));
					}
					Methods.setPosition(car.getHost(), location);
				}
				car.updatePositions();
			}
		}
	}
	
	public void updatePositions() {
		super.updatePositions();
		
		if(this.hasWheels) {
			for(Wheel wheel : this.wheels) {
				Location location1 = getHost().getLocation().clone();
				Location location2 = location1.add(location1.getDirection().setY(0).normalize().multiply(wheel.getOffset().getX()));
				float f1 = (float) (location2.getZ() + wheel.getOffset().getZ() * Math.sin(Math.toRadians(location2.getYaw())));
				float f2 = (float) (location2.getX() + wheel.getOffset().getZ() * Math.cos(Math.toRadians(location2.getYaw())));
				Location location3 = new Location(wheel.getHolder().getWorld(), f2, getHost().getLocation().getY() + wheel.getOffset().getY(), f1, getHost().getLocation().getYaw() + wheel.getRotation().floatValue(), location2.getPitch());
				Methods.setPosition(wheel.getHolder(), location3);
			}
		}
		
		if(this.spoilerStand != null) {
			Location location1 = getHost().getLocation().clone();
			Location location2 = location1.add(location1.getDirection().setY(0).normalize().multiply(this.spoiler.getX()));
			float f1 = (float) (location2.getZ() + this.spoiler.getZ() * Math.sin(Math.toRadians(location2.getYaw())));
			float f2 = (float) (location2.getX() + this.spoiler.getZ() * Math.cos(Math.toRadians(location2.getYaw())));
			Location location3 = new Location(getHost().getWorld(), f2, getHost().getLocation().getY() + this.spoiler.getY(), f1, location2.getYaw(), location2.getPitch());
			Methods.setPosition(this.spoilerStand, location3);
		}
	}
	
	public boolean destroy(boolean destroy) {
		boolean bool = super.destroy(destroy);
		
		if(bool) {
			if(this.hasWheels && this.wheels != null) {
				for(Wheel wheel : this.wheels) {
					if(wheel.getHolder() != null) {
						Main.getManager().getVehiclesLink().remove(wheel.getHolder());
						wheel.getHolder().remove();
					}
				}
			}
			
			if(this.spoilerStand != null) {
				this.spoilerStand.remove();
			}
		}
		return bool;
	}

	public String getWheelType() { return this.wheelType; }	  
	public int getRimRed() { return this.rimRed; }	  
	public int getRimGreen() { return this.rimGreen; }	  
	public int getRimBlue() { return this.rimBlue; }	  
	public int getSpoilerRed() { return this.spoilerRed; }  
	public int getSpoilerGreen() { return this.spoilerGreen; }	  
	public int getSpoilerBlue() { return this.spoilerBlue; }	  
	public String getSpoilerType() { return this.spoilerType; }
	public boolean hasWheels() { return this.hasWheels; }	  
	public Offset getSpoiler() { return this.spoiler; }	  
	public List<Wheel> getWheels() { return this.wheels; }	  
	public float getWheelRot() { return this.wheelRot; }
	
	public void setSpoilerRed(int red) { this.spoilerRed = red; }	  
	public void setSpoilerGreen(int green) { this.spoilerGreen = green; }	  
	public void setSpoilerBlue(int blue) { this.spoilerBlue = blue; }	  
	public void setWheelType(String wheelType) { this.wheelType = wheelType; }	  
	public void setSpoilerType(String spoilerType) { this.spoilerType = spoilerType; }	  
	public void setRimRed(int red) { this.rimRed = red; }	  
	public void setRimGreen(int green) { this.rimGreen = green; }	  
	public void setRimBlue(int blue) { this.rimBlue = blue; }	  
	public void setWheelRot(float wheelRot) { this.wheelRot = wheelRot; }
}
