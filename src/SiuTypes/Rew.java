package SiuTypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import mainsiu.siu;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;

public class Rew {
	
	private HashMap<Material, Integer> items = new HashMap<>();
	private double money;
	private List<String> open = new ArrayList<>();
	private List<String> custom = new ArrayList<>();
	private List<Attr> attributes = new ArrayList<>();
	private List<Ench> enchs = new ArrayList<>();
	private List<String> lore = new ArrayList<>();
	
	public Rew(double money, HashMap<Material, Integer> map, List<String> list, List<Attr> attrs, List<Ench> enchs) {
		this.custom = list;
		this.items = map;
		this.money = money;
		this.attributes = attrs;
		this.enchs = enchs;
	}
	
	public boolean addOpen(String id) {
		if (this.open.size() < 16) {
			this.open.add(id);
			return true;
		}
		return false;
	}
	
	public void addOpens(Collection<String> c) {
		c.forEach(x -> addOpen(x));
	}
	
	public List<String> getOpens() {
		return this.open;
	}
	
	public HashMap<Integer, ItemStack> takeItems(Player p) {
		HashMap<Integer, ItemStack> drop = new HashMap<>();
		Inventory inv = p.getInventory();
		for (Entry<Material, Integer> entry : this.getItems().entrySet()) {
			drop.putAll(inv.addItem(new ItemStack(entry.getKey(), entry.getValue())));
		}
		return drop;
	}
	
	public double getMoney() {
		return this.money;
	}
	
	public double takeMoney(Player p) {
		Economy ec = siu.getInstance().getEconomy();
		return ec == null ? 0 : ec.depositPlayer(p, this.money).balance;
	}
	
	public HashMap<Material, Integer> getItems() {
		return this.items;
	}

	public List<String> getCustom() {
		return this.custom;
	}
	
	public List<Attr> getAttrs() {
		return this.attributes;
	}
	
	public List<Ench> getEnchs() {
		return this.enchs;
	}
	
	public List<String> getLore() {
		return this.lore;
	}
	
	public void setLore(List<String> lore) {
		this.lore = lore;
	}
	
	public void dispatchCommands(Player p) {
		Location loc = p.getLocation();
		for (String cmd : this.getCustom()) {
			if (siu.getInstance().isPAPI()) cmd = PlaceholderAPI.setPlaceholders(p, cmd);
			cmd = cmd.replaceAll("%player%", p.getName()).replaceAll("%x%", loc.getBlockX() + "").replaceAll("%y%", loc.getBlockY() + "").replaceAll("%z%", loc.getBlockZ() + "");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
		}
	}
	
}
