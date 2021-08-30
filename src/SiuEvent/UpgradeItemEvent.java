package SiuEvent;

import java.util.List;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import mainsiu.CustomUpgrade;

public class UpgradeItemEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private CustomUpgrade cu;
	private Player p;
	private ItemStack item;
	private boolean cancelled = false;
	private List<Item> dropitems;
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	public UpgradeItemEvent(Player p, ItemStack i, CustomUpgrade cu) {
		this.p = p;
		this.item = i;
		this.cu = cu;
	}
	
	public CustomUpgrade getCustomUpgrade() {
		return this.cu;
	}
	
	
	public Player getPlayer() {
		return this.p;
	}
	
	public ItemStack getItem() {
		return this.item;
	}
	
	public List<Item> getDropItems() {
		return this.dropitems;
	}
	
	public void addDropItem(Item i) {
		this.dropitems.add(i);
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

}