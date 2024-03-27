package com.drawer.craft.component;

import com.drawer.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class HideUpgradeRecipe extends ComponentRecipe {

    public HideUpgradeRecipe(NamespacedKey key, ItemStack result, Main main) {
        super(key, result, main);

        loadRecipe("hide-upgrade");
    }
}
