package SiuTypes;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;

public class Attr {
	
	private Attribute attr;
    private AttributeModifier modifier;
    
    public Attr(String attr, double value) {
    	this.attr = getAttr(attr);
    	this.modifier = new AttributeModifier(UUID.randomUUID(), attr, value, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
    }
    
    public static Attribute getAttr(String at) {
        switch (at) {
            case "generic.attackknockback": return Attribute.GENERIC_ATTACK_KNOCKBACK;
            case "generic.attackspeed": return Attribute.GENERIC_ATTACK_SPEED;
            case "generic.attackdamage": return Attribute.GENERIC_ATTACK_DAMAGE;
            case "generic.movementspeed": return Attribute.GENERIC_MOVEMENT_SPEED;
            case "generic.horsejumpstrength": return Attribute.HORSE_JUMP_STRENGTH;
            case "generic.knockbackresistance": return Attribute.GENERIC_KNOCKBACK_RESISTANCE;
            case "generic.flyingspeed": return Attribute.GENERIC_FLYING_SPEED;
            case "generic.maxhealth": return Attribute.GENERIC_MAX_HEALTH;
            default:
                return null;
        }
    }
    
    public void setAttribute(ItemMeta meta) {
    	meta.removeAttributeModifier(attr);
    	meta.addAttributeModifier(attr, modifier);
    }
	
}
