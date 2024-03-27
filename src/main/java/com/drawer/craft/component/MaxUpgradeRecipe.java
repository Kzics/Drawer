package com.drawer.craft.component;

import com.drawer.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class MaxUpgradeRecipe extends ComponentRecipe {
    public MaxUpgradeRecipe(int level, NamespacedKey key, ItemStack result, Main main) {
        super(key, result, main);

        loadRecipe(String.format("max-upgrade-%s", level));
    }
}
