package cz.larkyy.leastereggs.inventory;

import cz.larkyy.leastereggs.Leastereggs;
import cz.larkyy.leastereggs.objects.Egg;
import cz.larkyy.leastereggs.utils.StorageUtils;
import cz.larkyy.leastereggs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListGUIHolder implements InventoryHolder {

    private GUIUtils guiUtils;
    private StorageUtils storageUtils;
    private int page;
    private Player p;
    private Leastereggs main;
    private Inventory gui;
    private Utils utils;

    public ListGUIHolder(Leastereggs main, Player p, int page) {
        this.main = main;
        this.guiUtils = main.guiUtils;
        this.p = p;
        this.page = page;
        this.utils = main.utils;
        this.storageUtils = main.storageUtils;

    }

    @Override
    public @NotNull Inventory getInventory() {

        gui = Bukkit.createInventory(this, main.getCfg().getInt("inventories.main.size", 45), main.utils.format(main.getCfg().getString("inventories.main.title", "&d&lEE &8| Eggs List (%page%)").replace("%page%", String.valueOf(page + 1))));

        solveItems();
        return gui;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    private void solveItems() {
        for (String itemType : main.getCfg().getConfiguration().getConfigurationSection("inventories.main.items").getKeys(false)) {

            switch (itemType) {
                case "eggs":
                    loadEggItems();
                    break;
                case "prevPage":
                    loadItem(itemType, "prevPage");
                    break;
                case "nextPage":
                    loadItem(itemType, "nextPage");
                    break;
                case "close":
                    loadItem(itemType, "close");
                    break;
                default:
                    loadItem(itemType, null);
                    break;
            }
        }
    }

    private void loadItem(String itemType, String localizedName) {
        if (!isMoreSlots(itemType)) {
            int slot = main.getCfg().getConfiguration().getInt("inventories.main.items." + itemType + ".slot");
            if (slot != -1) {
                gui.setItem(slot, mkItem(itemType, localizedName));
            }

        } else {
            ItemStack is = mkItem(itemType, localizedName);
            for (int i : main.getCfg().getConfiguration().getIntegerList("inventories.main.items." + itemType + ".slots")) {
                gui.setItem(i, is);
            }
        }
    }

    private ItemStack mkItem(String itemType, String localizedName) {
        return main.utils.mkItem(
                //MATERIAL
                Material.valueOf(main.getCfg().getConfiguration().getString("inventories.main.items." + itemType + ".material", "STONE")),
                //NAME
                utils.format(main.getCfg().getConfiguration().getString("inventories.main.items." + itemType + ".name", null)),
                //LOCALIZEDNAME
                localizedName,
                //LORE
                utils.formatLore(main.getCfg().getConfiguration().getStringList("inventories.main.items." + itemType + ".lore")),
                //TEXTURE
                main.getCfg().getConfiguration().getString("inventories.main.items." + itemType + ".texture", null));
    }

    private void loadEggItems() {
        List<ItemStack> eggItems = new ArrayList<>();
        for (Map.Entry<Integer, Egg> pair : storageUtils.getEggs().entrySet()) {
            eggItems.add(utils.mkItem(
                    //MATERIAL
                    Material.valueOf(main.getCfg().getConfiguration().getString("inventories.main.items.eggs.material", "STONE")),
                    //NAME
                    utils.format(main.getCfg().getConfiguration().getString("inventories.main.items.eggs.name", "Egg Item").replace("%id%", pair.getKey().toString())),
                    //LOCALIZEDNAME
                    "Egg " + pair.getKey(),
                    //LORE
                    utils.formatLore(main.getCfg().getConfiguration().getStringList("inventories.main.items.eggs.lore")),
                    //TEXTURE
                    main.getCfg().getConfiguration().getString("inventories.main.items.eggs.texture", null)
            ));
        }
        guiUtils.loadEggItems(gui, eggItems, page, "inventories.main.items.eggs.slots");
    }

    private boolean isMoreSlots(String itemType) {
        return guiUtils.isMoreSlots("main", itemType);
    }
}
