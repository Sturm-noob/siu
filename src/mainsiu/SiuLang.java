package mainsiu;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import net.kyori.adventure.text.Component;


//��, ��������� ������� ����. ���, � �� ���� �� ������. 
public enum SiuLang {
	
	
	NEED_TEXT("&c����������:"),
	REWARD_TEXT("&a���:"),
	MENU("&2��������� ��������"),
	UNUPABLE_ITEM("&c���� ������� �� ����� ���� �������"),
	NO_MONEY("&9��� ���������� ������� ��� %money% $"),
	NO_ITEM("&9��� ����� ������� %item% � ���������� %amount%"),
	NO_REQS("&9�� �� ��������� ��� �������"),
	UP_SUC("&a�� ������� �������� �������"),
	ITEM_DROP("&9� ��� ������������ ����� � ���������. ������� %item% � ���������� %amount% ����� ����� � ����."),
	NO_PERMS("&b���, �������, � ��� ��� �� ��� ����������");
	
	
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
