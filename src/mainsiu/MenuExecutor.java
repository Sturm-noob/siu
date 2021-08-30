package mainsiu;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import SiuEvent.UpgradeItemEvent;
import SiuTypes.Req;


public class MenuExecutor implements CommandExecutor, Listener  {
	
	private ItemManager im;
	
	MenuExecutor(ItemManager i) {
		this.im = i;
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {	
		if (!(arg0 instanceof Player)) return false;
		if (!arg0.hasPermission("siu.up")) {
			arg0.sendMessage(SiuLang.NO_PERMS.getMessage());
			return true;
		}
		Player p = (Player) arg0;
		
		ItemStack item = p.getInventory().getItemInMainHand();
				
		UpableItem ui = im.getByMaterial(item.getType());
		if (ui == null) {
			p.sendMessage(SiuLang.UNUPABLE_ITEM.getMessage());
			return true;
		}
		String id = getUpdateID(item, ui);
		CustomUpgrade a = ui.getByID(id);
		if (a == null) a = ui.getUps().get(0);
		if (a == null) {
			Bukkit.getLogger().info("В плагине siu у предмета " + ui.getMaterial().name() + " нет улучшений. Плагин выключен.");
			Bukkit.getPluginManager().disablePlugin(siu.getInstance());
		}
		List<String> b = a.getRew().getOpens();
		p.openInventory(new SiuMenu().getMenu(ui, b));
		
		return true;
	}
	
	private String getUpdateID(ItemStack item, UpableItem ui) {
		return item.getItemMeta().getPersistentDataContainer().getOrDefault(new NamespacedKey(siu.getInstance(), "siu"), PersistentDataType.STRING, "default");
	}
	
	@EventHandler
	public void onInteract(InventoryClickEvent e) {
		if (e.getClickedInventory() == null) return;
		if (e.getClickedInventory().getHolder() instanceof SiuMenu) {
			e.setCancelled(true);
			int id = e.getSlot();
			if (id == -999) return;
			ItemStack i = e.getCurrentItem();
			if (i == null) return;
			PersistentDataContainer cont = i.getItemMeta().getPersistentDataContainer();
			if (!cont.has(new NamespacedKey(siu.getInstance(), "siu"), PersistentDataType.STRING)) return;
			String str = cont.get(new NamespacedKey(siu.getInstance(), "siu"), PersistentDataType.STRING);
			ItemStack item = e.getWhoClicked().getInventory().getItemInMainHand();
			UpableItem ui = siu.getInstance().getItemManager().getByMaterial(item.getType());
			if (ui == null) return;
			CustomUpgrade cu = ui.getByID(str);
			if (cu == null) return;
			UpgradeItemEvent event = new UpgradeItemEvent((Player)e.getWhoClicked(), item, cu);
			Bukkit.getPluginManager().callEvent(event);
			onUpgrade(event);
			e.getWhoClicked().closeInventory();
		}
		if (e.getInventory().getHolder() instanceof SiuMenu && e.isShiftClick()) e.setCancelled(true);
	}
	
	public void onUpgrade(UpgradeItemEvent e) {
		if (e.isCancelled()) return;
		CustomUpgrade cu = e.getCustomUpgrade();
		Player p = e.getPlayer();
		ItemStack item = e.getItem();
		Req cont = cu.checkReward(p, item);
		if (cont.isEmpty()) {
			Collection<ItemStack> dropped = cu.getReward(p, item);
			dropped.forEach(x -> e.addDropItem(p.getWorld().dropItem(p.getLocation(), x)));
			return;
		}
		else sendReqMessage(p, cont);
		return;
	}
	
	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		if (e.getInventory().getHolder() instanceof SiuMenu) e.setCancelled(true);
	}
	
	@EventHandler
	public void onMove(InventoryMoveItemEvent e) {
		if (e.getInitiator().getHolder() instanceof SiuMenu) e.setCancelled(true);
	}
	
	@EventHandler
	public void onCraft(CraftItemEvent e) {
		ItemStack item = e.getCurrentItem();
		UpableItem ui = siu.getInstance().getItemManager().getByMaterial(item.getType());
		CustomUpgrade cu = ui.getByID("default");
		if (cu == null) return;
		cu.getReward((Player) e.getWhoClicked(), item);
	}
	
	
	
	private void sendReqMessage(Player p, Req cont) {
		for (Entry<Material, Integer> ent : cont.getItems().entrySet()) {
			p.sendMessage(SiuLang.NO_ITEM.siuLangBuilder().replace("%item%", ent.getKey().name()).replace("%amount%", ent.getValue() + "").buildMessage());
		}
		if (cont.getMoney() > 0) p.sendMessage(SiuLang.NO_MONEY.siuLangBuilder().replace("%money%", cont.getMoney() + "").buildMessage());
		if (!cont.getCustom().isEmpty()) p.sendMessage(SiuLang.NO_REQS.getMessage());
	}
	 
}
