package com.drawer;

import com.drawer.display.DrawerPart;
import com.drawer.obj.impl.Drawer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DrawerItem extends ItemStack {

    public DrawerItem(int amount, Material material, String name, List<String> lores) {
        super(material, 1);

        ItemMeta meta = getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lores);
        setItemMeta(meta);
    }

    public DrawerItem(Material mat, String name, Drawer drawer, Main main, String type) {
        super(mat, 1);

        ItemMeta meta = getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        List<String> lore = new ArrayList<>();
        for (String line : main.getConfig().getStringList(String.format("drawers.%s.lore", type))) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        if (drawer != null) {
            for (DrawerPart part : drawer.getDrawerParts()) {
                Material partMaterial = part.getItemDisplay().getItemStack().getType();
                String itemName = partMaterial.equals(Material.AIR) ? "Vide" : partMaterial.name().replace("_", " ").toLowerCase();
                String amount = part.getText().asString();

                if (!itemName.equals("Vide")) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("line-format")).replace("{amount}", amount)
                            .replace("{item}", itemName));
                }
            }
        }

        meta.setLore(lore);
        setItemMeta(meta);
    }
}
