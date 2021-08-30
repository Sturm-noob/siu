package SiuTypes;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

public class Ench {
	
	private Enchantment ench;
	private int value;
	
	public Ench(String ench, int value) {
		this.ench = Enchantment.getByKey(NamespacedKey.fromString(ench));
		this.value = value;
	}
	
	public void setEnchantment(ItemMeta meta) {
		meta.addEnchant(ench, value, true);
	}
	
}
