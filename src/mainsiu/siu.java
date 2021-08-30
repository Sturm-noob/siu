package mainsiu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import SiuTypes.Attr;
import SiuTypes.Ench;
import SiuTypes.Req;
import SiuTypes.Rew;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;

public class siu extends JavaPlugin  {
	
	private static siu pl;
	private ItemManager im;
	private Economy econ = null;
	private ItemStack icon = new ItemStack(Material.BLUE_STAINED_GLASS);
	
	public void onEnable() {
		
		saveDefaultConfig();
		FileConfiguration config = getConfig();
		loadIcon(config.getConfigurationSection("icon"));
		ItemManager im = loadMaterials(config.getConfigurationSection("items"));
		if (im == null) {
			Bukkit.getLogger().info("Ошибка при загрузке предметов");
			getServer().getPluginManager().disablePlugin(this);
		}
		this.im = im;
		
		File lang = new File(getDataFolder() + File.separator + "lang.yml");
		if (!lang.exists()) {
			try {
				lang.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		YamlConfiguration l = YamlConfiguration.loadConfiguration(lang);
		l = SiuLang.setMessages(l);
		try {
			l.save(lang);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Logger log = Bukkit.getLogger();
		
		if (!setupEconomy() ) {
			log.warning("Вы знали, что у вас не установлен Vault?");
            log.warning("Я, конечно, не заставляю его ставить,");
            log.warning("Но если в конфиге siu у вас где-то есть строчка money - ");
            log.warning("поздравляю вас, она не работает.");
            log.warning("А ещё могут выползти ошибки. Не должны, но могут.");
            log.warning("И вообще, Vault - хорошая вещь. Лучше поставить.");
        }
		
		if (!isPAPI() ) {
			log.warning("Вы знали, что у вас не установлено PlaceholderAPI?");
            log.warning("Я, конечно, не заставляю его ставить,");
            log.warning("Но если в конфиге siu у вас где-то есть команды");
            log.warning("с вашими заполнителями - поздравляю вас, они не работают.");
            log.warning("А ещё могут выползти ошибки. Не должны, но могут.");
            log.warning("И вообще, PlaceholderAPI - хорошая вещь. Лучше поставить.");
		}
		
		MenuExecutor ex = new MenuExecutor(im);
		getCommand("ui").setExecutor(ex);
		getCommand("upgradeitem").setExecutor(ex);
		Bukkit.getPluginManager().registerEvents(ex, this);
		
		pl = this;
	}
	
	
	
	private ItemManager loadMaterials(ConfigurationSection c) {
		ItemManager im = new ItemManager();
		
		for (String key : c.getKeys(false)) {
			UpableItem ui = new UpableItem(Material.getMaterial(key));
			
			for (String id : c.getConfigurationSection(key).getKeys(false)) {
				CustomUpgrade cu = new CustomUpgrade(id);
				
				ConfigurationSection need = c.getConfigurationSection(key + "." + id + ".need");
				if (need != null) {
					int money = need.getInt("money");
					HashMap<Material, Integer> items = getItems(getStringList(need, "items"));
					List<String> custom = need.getStringList("custom");
					Req r = new Req(money, items, custom);
					r.setLore(getStringList(need, "lore"));
					cu.setReqs(r);
				}
				
				ConfigurationSection reward = c.getConfigurationSection(key + "." + id + ".reward");
				
				if (reward != null) {
					int money = reward.getInt("money");
					HashMap<Material, Integer> items = getItems(getStringList(reward, "items"));
					List<String> custom = getStringList(reward, "custom");
					List<Attr> attrs = getAttrs(getStringList(reward, "attributes"));
					List<Ench> enchs = getEnchs(getStringList(reward, "enchantments"));
					Rew r = new Rew(money, items, custom, attrs, enchs);			
					r.addOpens(getStringList(reward, "open"));	
					r.setLore(getStringList(reward, "lore"));
					cu.setRews(r);
				}
				ui.addCustomUpgrade(cu);
			}
			if (!im.addUpableItem(ui)) Bukkit.getLogger().info("В конфиге siu дублируется предмет " + ui.getMaterial());
		}
		return im;
	}
	
	public static siu getInstance() {
		return pl;
	}
	
	public ItemManager getItemManager() {
		return this.im;
	}
	
	private boolean setupEconomy() {    //стырил с гитхаба vault'а, мб что-то не нужно/не учтено, я не в курсе
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	public Economy getEconomy() {
		return this.econ;
	}
	
	private List<String> getStringList(ConfigurationSection c, String path) {
		List<String> ret = new ArrayList<>();
		if (!c.contains(path)) return ret;
		if (c.isList(path)) return c.getStringList(path);
		String s = c.getString(path);
		if (s != null) ret.add(s);
		return ret;
	}
	
	private HashMap<Material, Integer> getItems(List<String> a) {
		HashMap<Material, Integer> items = new HashMap<>();
		getEntry(a).forEach(x -> items.put(Material.matchMaterial(x.getKey()), x.getValue().intValue()));
		return items;
	}
	
	private List<Attr> getAttrs(List<String> a) {
		List<Attr> ret = new ArrayList<>();
		getEntry(a).forEach(x -> ret.add(new Attr(x.getKey(), x.getValue())));
		return ret;
	}
	
	private List<Ench> getEnchs(List<String> a) {
		List<Ench> ret = new ArrayList<>();
		getEntry(a).forEach(x -> ret.add(new Ench(x.getKey(), x.getValue().intValue())));
		return ret;
	}
	
	private Set<Entry<String, Double>> getEntry(List<String> a) {
		HashMap<String, Double> ret = new HashMap<>();
		for (String s : a) {
			if (s.isEmpty()) continue;
			String[] y = s.split(",");
			ret.put(y[0].trim(), Double.parseDouble(y[1].trim()));
		}
		return ret.entrySet();
	}
	
	public boolean isPAPI() {
		return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
	}
	
	public void loadIcon(ConfigurationSection c) {
		int amount = c.getInt("amount");
		if (amount < 1) amount = 1;
		if (amount > 64) amount = 64;
		ItemStack item = new ItemStack(Material.matchMaterial(c.getString("type")), amount);
		ItemMeta meta = item.getItemMeta();
		meta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', c.getString("name"))));
		item.setItemMeta(meta);
		this.icon = item;
	}
	
	public boolean setIcon(ItemStack item) {
		if (!item.getItemMeta().lore().isEmpty()) return false;
		this.icon = item;
		return true;
	}
	
	//Если сделать втупую return this.icon, то вернётся объект icon (логично)
	//Тогда изменения появятся тут, а этого нам не надо
	//Поэтому тут костыль для копирования предмета
	public ItemStack getIcon() {
		ItemStack ret = new ItemStack(icon.getType(), icon.getAmount());
		ret.setItemMeta(icon.getItemMeta());
		return ret;
	}
	
}
