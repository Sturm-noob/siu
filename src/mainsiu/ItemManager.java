package mainsiu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

public class ItemManager {
	
	private List<UpableItem> uii = new ArrayList<>();
	
	ItemManager() {}
	
	public UpableItem getByMaterial(Material m) {
		for (UpableItem c : uii) {
			if (c.getMaterial() == m) return c;
		}
		return null;
	}
	
	public boolean addUpableItem(UpableItem ui) {
		if (getByMaterial(ui.getMaterial()) != null) {
			return false;
		}
		uii.add(ui);
		return true;
	}
	
	public List<UpableItem> getItems() {
		return this.uii;
	}
	
}
