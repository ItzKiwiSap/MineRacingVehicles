package nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.types;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class RimType {

	private String name;
	private ItemStack item;
	private Double price;
	
	public RimType(String name, ItemStack item, Double price) {
		this.name = name;
		this.item = item;
		this.price = price;
	}
	
	public ItemStack getItemWithColor(int r, int g, int b) {
		ItemStack itemStack = item.clone();
		if(itemStack.getType().name().contains("LEATHER_")) {
			LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
			meta.setColor(Color.fromRGB(r, g, b));
		}
		return itemStack;
	}
	
	public String getName() { return this.name; }
	public ItemStack getItem() { return this.item; }
	public Double getPrice() { return this.price; }
}
