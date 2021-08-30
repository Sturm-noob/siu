package mainsiu;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import SiuTypes.Attr;
import SiuTypes.Ench;
import SiuTypes.Req;
import SiuTypes.Rew;
import net.milkbowl.vault.economy.Economy;

public class CustomUpgrade {
	
	private String id;
	private Req reqs;
	private Rew rews;
	
	public CustomUpgrade(String id) {
		this.id = id;
	}
	
	public void setReqs(Req r) {
		this.reqs = r;
	}
	
	public void setRews(Rew w) {
		this.rews = w;
	}
	
	public String getID() {
		return this.id;
	}
	
	public Req getReq() {
		return this.reqs;
	}
	
	public Rew getRew() {
		return this.rews;
	}
	
	public Req checkReward(Player p, ItemStack item) {
		Map<Material, Integer> items = this.reqs.itemsLeft(p.getInventory());
		double money = this.reqs.moneyLeft(p);
		List<String> cc = this.reqs.getUncompletedCustom(p);
		return new Req(money, items, cc);
	}
	
	public Collection<ItemStack> getReward(Player p, ItemStack item) {
		completeReqs(p);
		return getRewardIgnoreReqs(p, item);
	}
	
	public boolean completeReqs(Player p) {
		if (this.reqs == null) return false;
		Economy economy  = siu.getInstance().getEconomy();
		if (economy != null) this.reqs.takeMoney(p);
		this.reqs.takeItems(p.getInventory());
		return true;
	}
	
	public Collection<ItemStack> getRewardIgnoreReqs(Player p, ItemStack item) {
		if (this.rews == null) return null;
		this.rews.takeMoney(p);
		ItemMeta meta = item.getItemMeta();
		meta.getPersistentDataContainer().set(new NamespacedKey(siu.getInstance(), "siu"), PersistentDataType.STRING, this.id);
		for (Attr a : this.rews.getAttrs()) a.setAttribute(meta);
		for (Ench e : this.rews.getEnchs()) e.setEnchantment(meta);
		item.setItemMeta(meta);
		this.rews.dispatchCommands(p);
		return this.rews.takeItems(p).values();
	}
	
}
