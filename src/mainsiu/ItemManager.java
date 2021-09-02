package mainsiu;

import java.util.EnumMap;

import org.bukkit.Material;

public class ItemManager {
	
	private EnumMap<Material, UpableItem> uii = new EnumMap<>(Material.class);
	
	ItemManager() {}
	
	public UpableItem getByMaterial(Material m) {
		return uii.get(m);
	}
	
	public boolean addUpableItem(UpableItem ui) {
		return uii.put(ui.getMaterial(), ui) != null;
	}
	
	public EnumMap<Material, UpableItem> getItems() {
		return this.uii;
	}
	
}
