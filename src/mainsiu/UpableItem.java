package mainsiu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

public class UpableItem {
	
	private Material m;
	private List<CustomUpgrade> cus = new ArrayList<>();

	public UpableItem(Material m) {
		if (m != null) this.m = m;
		else throw new IllegalArgumentException("В конфиге siu значение одного из материалов указано неверно");
	}
	
	public Material getMaterial() {
		return this.m;
	}
	
	public CustomUpgrade getByID(String id) {
		for (CustomUpgrade cu : cus) {
			if (cu.getID().equalsIgnoreCase(id)) return cu;
		}
		return null;
	}
	
	public boolean addCustomUpgrade(CustomUpgrade cu) {
		if (getByID(cu.getID()) != null) return false;
		cus.add(cu);
		return true;
	}
	
	public List<CustomUpgrade> getUps() {
		return this.cus;
	}

}
