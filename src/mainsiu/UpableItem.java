package mainsiu;

import java.util.LinkedHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class UpableItem {
	
	private Material m;
	private LinkedHashMap<String, CustomUpgrade> cus = new LinkedHashMap<>();

	public UpableItem(Material m) {
		if (m != null) this.m = m;
		else {
			Bukkit.getLogger().info("В конфиге siu значение одного из материалов указано неверно");
			Bukkit.getPluginManager().disablePlugin(siu.getInstance());
		}
	}
	
	public Material getMaterial() {
		return this.m;
	}
	
	public CustomUpgrade getByID(String id) {
		return cus.get(id);
	}
	
	public boolean addCustomUpgrade(CustomUpgrade cu) {
		return cus.put(cu.getID(), cu) != null;
	}
	
	public LinkedHashMap<String, CustomUpgrade> getUps() {
		return this.cus;
	}

}
