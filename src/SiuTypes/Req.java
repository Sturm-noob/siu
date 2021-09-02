package SiuTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import mainsiu.siu;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;

public class Req {
	
	private Map<Material, Integer> items = new HashMap<>();
	private double money;
	private List<String> custom = new ArrayList<>();
	private List<String> lore = new ArrayList<>();
	
	public Req(double money, Map<Material, Integer> map, List<String> list) {
		this.custom = list;
		this.items = map;
		this.money = money;
	}
	
	public Map<Material, Integer> itemsLeft(Inventory inv) {
		Map<Material, Integer> ret = copy(this.items);
		
		if (ret == null || ret.isEmpty()) return new HashMap<Material, Integer>();
		for (ItemStack i : inv.getContents()) {
			if (i == null) continue;
			Material t = i.getType();
			Integer co = ret.get(t);
			if (co != null) ret.put(t, co - i.getAmount());
			if (ret.getOrDefault(t, 0) <= 0) ret.remove(t);
		}
		return ret;
	}
	
	public boolean takeItems(Inventory inv) {
		if (!itemsLeft(inv).isEmpty()) return false;
		Map<Material, Integer> temp = copy(this.items);
		
		for (int i = 0; i < 36; i++) {
			ItemStack item = inv.getItem(i);
			if (item == null) continue;
			Material type = item.getType();
			Integer am = temp.get(type);
			if (am == null) continue;
			if (am >= item.getAmount()) {
				inv.setItem(i, null);
				temp.put(type, am - item.getAmount());
			}
			else {
				temp.remove(type);
				inv.setItem(i, new ItemStack(type, item.getAmount() - am));
			}
		}
		return true;
	}
	
	public double getMoney() {
		return this.money;
	}
	
	public double moneyLeft(OfflinePlayer p) {
		Economy ec = siu.getInstance().getEconomy();
		double ret = (ec == null) ? 0 : this.money - siu.getInstance().getEconomy().getBalance(p);
		return ret > 0 ? ret : 0;
	}
	
	public boolean takeMoney(OfflinePlayer p) {
		return siu.getInstance().getEconomy().withdrawPlayer(p, this.money).transactionSuccess();
	}
	
	public List<String> getLore() {
		return this.lore;
	}
	
	public void setLore(List<String> lore) {
		this.lore = lore;
	}
	
	public List<String> getUncompletedCustom(Player p) {
		List<String> ret = new ArrayList<>();
		for (String command : this.custom) {
			String cmd = command.substring(0);
			Location loc = p.getLocation();
			if (siu.getInstance().isPAPI()) cmd = PlaceholderAPI.setPlaceholders(p, cmd);
			cmd = cmd.replaceAll("%player%", p.getName()).replaceAll("%exp_level%", p.getLevel() + "").replaceAll("%world%", loc.getWorld().getName())
					.replaceAll("%x%", loc.getBlockX() + "").replaceAll("%y%", loc.getBlockY() + "").replaceAll("%z%", loc.getBlockZ() + "");
			
			if (cmd.startsWith("hasPermission")) {
				cmd = cmd.replace("hasPermission", "");
				cmd = cmd.substring(1, cmd.length() - 1);
				if (!p.hasPermission(cmd)) ret.add(command);
				continue;
			}
			if (cmd.startsWith("equals")) {
				cmd = cmd.substring(7, cmd.length() - 1);
				String[] y = cmd.split(",");
				if (y.length != 2) {
					Bukkit.getLogger().info("Ошибка в условии: " + cmd);
					Bukkit.getLogger().info("Количество аргументов должно быть равно двум.");
					continue;
				}
				if (!y[0].trim().equalsIgnoreCase(y[1].trim())) ret.add(command);
				continue;
			}
			
			if (cmd.contains("==")) {
				String[] y = cmd.split("==");
				if (y.length != 2) {
					Bukkit.getLogger().info("Ошибка в условии: " + cmd);
					Bukkit.getLogger().info("Количество аргументов должно быть равно двум.");
					continue;
				}
				if (!isDouble(y[0].trim()) || !isDouble(y[1].trim())) {
					Bukkit.getLogger().info("Ошибка в условии: " + cmd);
					Bukkit.getLogger().info("Оба агрумента должны быть числами");
				}

				if (cmd.contains("==") || cmd.contains(">") || cmd.contains("<")) {
					if (!math(cmd)) ret.add(command);
					continue;
				}
			}
				
			
		}
		return ret;
	}
	
	private boolean math(String cmd) {
		String[] y = null;
		char ch = '=';
		if (cmd.contains("==")) y = cmd.split("==");
		else if (cmd.contains(">")) {
			y = cmd.split(">");
			ch = '>';
		}
		else if (cmd.contains("<")) {
			y = cmd.split("<");
			ch = '<';
		}
		if (y == null) return true;
		if (y.length != 2) {
			Bukkit.getLogger().info("Ошибка в условии: " + cmd);
			Bukkit.getLogger().info("Количество аргументов должно быть равно двум.");
			return true;
		}
		if (!isDouble(y[0].trim()) || !isDouble(y[1].trim())) {
			Bukkit.getLogger().info("Ошибка в условии: " + cmd);
			Bukkit.getLogger().info("Оба агрумента должны быть числами");
			return true;
		}
		double[] z = new double[]{Double.parseDouble(y[0]), Double.parseDouble(y[1])};
		if (ch == '=') return z[0] == z[1];
		if (ch == '>') return z[0] > z[1];
		if (ch == '<') return z[0] < z[1];
		return true;
	}
	
	private boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
		}
		catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	public boolean isEmpty() {
		return (money <= 0 && items.isEmpty() && custom.isEmpty());
	}
	
	//Map.copyOf выдаёт NoSuchMethodError, поэтому так
	private Map<Material, Integer> copy(Map<Material, Integer> map)  {
		Map<Material, Integer> ret = new HashMap<Material, Integer>();
		map.entrySet().forEach(x -> ret.put(x.getKey(), x.getValue()));
		return ret;
	}
	
	public Map<Material, Integer> getItems() {
		return this.items;
	}
	
	public List<String> getCustom() {
		return this.custom;
	}
}
