package com.drawer.craft.drawers;

import com.drawer.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class AdvancedDrawerRecipe extends DrawerRecipe {
    public AdvancedDrawerRecipe(NamespacedKey key, ItemStack result, Main main) {
        super(key, result, main);

        loadRecipe("advanced");
    }
}
