package mainsiu;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import net.kyori.adventure.text.Component;


//Да, сообщения сделаны тупо. Нет, я не хочу их менять. 
public enum SiuLang {
	
	
	NEED_TEXT("&cНеобходимо:"),
	REWARD_TEXT("&aДаёт:"),
	MENU("&2Улучшение предмета"),
	UNUPABLE_ITEM("&cЭтот предмет не может быть улучшен"),
	NO_MONEY("&9Вам необходимо собрать ещё %money% $"),
	NO_ITEM("&9Вам нужно собрать %item% в количестве %amount%"),
	NO_REQS("&9Вы не выполнили все условия"),
	UP_SUC("&aВы успешно улучшили предмет"),
	ITEM_DROP("&9У вас недостаточно места в инвентаре. Предмет %item% в количестве %amount% лежит рядом с вами."),
	NO_PERMS("&bУпс, кажется, у вас нет на это разрешения");
	
	
	private String text;
	private static boolean isinit = false;
	
	SiuLang(String text) {
		this.text = text;
	}

	public static YamlConfiguration setMessages(YamlConfiguration c) {
		if (isinit) return c;
		isinit = true;
		for (SiuLang sl : SiuLang.values()) {
			String path = sl.toString().replaceAll("_", "-").toLowerCase();
			String s = c.getString(path);
			if (s != null) sl.text = s;
			else c.set(path, sl.text);
		}
		return c;
	}
	
	public Component getMessage() {
		return Component.text(ChatColor.translateAlternateColorCodes('&', this.text));
	}
	
	public static class SiuLangBuilder {
		
		private String text;
		
		public SiuLangBuilder(SiuLang l) {
			this.text = l.text;
		}
		
		public SiuLangBuilder replace(String old, String replace) {
			this.text = this.text.replaceAll(old, replace);
			return this;
		}
		
		public Component buildMessage() {
			return Component.text(ChatColor.translateAlternateColorCodes('&', this.text));
		}
		
	}
	
	public SiuLangBuilder siuLangBuilder() {
		return new SiuLangBuilder(this);
	}

}
