package nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.types;

import org.bukkit.inventory.ItemStack;

public class FuelType {

	private String name;
	private ItemStack item;
	private double price;
	private double fillAmount;
	
	public FuelType(String name, ItemStack item, double price, double fillAmount) {
		this.name = name;
		this.item = item;
		this.price = price;
		this.fillAmount = fillAmount;
	}
	
	public String getName() { return this.name; }
	public ItemStack getItem() { return this.item; }
	public double getPrice() { return this.price; }
	public double getFillAmount() { return this.fillAmount; }
}
