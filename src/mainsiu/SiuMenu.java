package mainsiu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;

public class SiuMenu implements InventoryHolder {
	
	private Inventory inv;
	
	@Override
	public @NotNull Inventory getInventory() {
		return this.inv;
	}
	
	public Inventory getMenu(UpableItem ui, List<String> b) {
		int pos = -2;
		HashMap<Integer, ItemStack> items = new HashMap<>();
		for (String c : b) {
			pos += 2;
			ItemStack item = siu.getInstance().getIcon();
			ItemMeta meta = item.getItemMeta();
			meta.getPersistentDataContainer().set(new NamespacedKey(siu.getInstance(), "siu"), PersistentDataType.STRING, c);
			CustomUpgrade cu = ui.getByID(c);	
			meta.lore(getLore(cu));
			item.setItemMeta(meta);
			items.put(pos, item);
		}
		this.inv = Bukkit.createInventory(this, ((int) (pos / 9) + 1) * 9, SiuLang.MENU.getMessage());
		for (Entry<Integer, ItemStack> entry : items.entrySet()) {
			this.inv.setItem(entry.getKey(), entry.getValue());
		}
		return this.getInventory();
	}
	
	private List<Component> getLore(CustomUpgrade cu) {
		List<Component> lore = new ArrayList<>();
		lore.add(SiuLang.NEED_TEXT.getMessage());
		cu.getReq().getLore().forEach(x -> lore.add(Component.text(ChatColor.translateAlternateColorCodes('&', x))));
		lore.add(SiuLang.REWARD_TEXT.getMessage());
		cu.getRew().getLore().forEach(x -> lore.add(Component.text(ChatColor.translateAlternateColorCodes('&', x))));
		return lore;
	}

}
