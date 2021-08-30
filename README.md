# siu

[скачать](https://disk.yandex.ru/d/WMuv-bYFj6bgig)

# Permission:
siu.up (на команды /ui, /upgradeitem)

# API:
UpgradeItemEvent - событие улучшения предмета. 
Содержит getCustomUpgrade(), getPlayer(), getItem(), isCancelled(), setCancelled(), getDropItems() (это предметы из награды, которые выпадают когда инвентарь заполнен), addDropItem (для обработки вашего дропа другими плагинами)

ItemManager it_ma = siu.getInstance().getItemManager(); - получить ItemManager\
UpableItem up_it = new UpableItem(Material.DIAMOND_PICKAXE); - новый улучшаемый предмет\
CustomUpgrade cu_up = new CustomUpgrade("my_id"); - создаёт улучшение с айди my_id\
Req req = new Req(money, items, custom); - создаёт новый список условий (double money - кол-во денег, Map<Material, Integer> items - предметы, List\<String\> custom - ваше условие (в конфиге описано))\
req.setLore(lore); - устанавливает описание улучшения (lore - List<String>)\
Rew rew = new Rew(money, items, custom, attrs, enchs); - новый список наград (attrs - List\<Attr\>, Attr(String attr, double value), enchs - List\<Ench\>, Ench(String ench, int value))\
cu_up.setReqs(req); cu_up.setRews(rew); - установить требования/награды\
up_it.addCustomUpgrade(cu_up); - добавить улучшение к предмету\
it_ma.addUpableItem(up_it); - добавить предмет в ItemManager
