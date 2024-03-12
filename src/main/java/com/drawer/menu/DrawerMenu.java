package com.drawer.menu;

import com.drawer.Main;
import com.drawer.components.ComponentAbility;
import com.drawer.obj.impl.Drawer;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class DrawerMenu {

    private final Drawer drawer;
    private final Inventory inventory;
    private final Main main;
    public DrawerMenu(final Drawer drawer, final Main main){
        this.drawer = drawer;
        this.main = main;
        this.inventory = Bukkit.createInventory(null, InventoryType.HOPPER,"Drawer");
    }


    public void open(Player player){
        for (ComponentAbility component : drawer.getComponents()){
            ItemStack item = new ItemStack(component.getComponent().getMaterial());
            ItemMeta meta = item.getItemMeta();
            String componentName = component.getComponent().name();
            String descriptionKey;

            if (componentName.startsWith("MAX")) {
                descriptionKey = componentName.substring(0, componentName.length() - 2).toLowerCase().replace("_","-");
            } else {
                descriptionKey = componentName.replace("_", "-").toLowerCase();
            }

            descriptionKey += "-description";
            String displayName = main.getConfig().getString("component-name").replace("{name}", component.getName());
            displayName = WordUtils.capitalizeFully(displayName);

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));        List<String> componentDescription = main.getConfig().getStringList(descriptionKey);

            List<String> coloredLores = new ArrayList<>();

            for (String lore : componentDescription) {
                coloredLores.add(ChatColor.translateAlternateColorCodes('&', lore));
            }

            meta.setLore(coloredLores);
            meta.getPersistentDataContainer().set(main.componentKey, PersistentDataType.STRING,component.getComponent().name());
            item.setItemMeta(meta);
            inventory.addItem(item);
        }

        player.openInventory(inventory);
    }
}
