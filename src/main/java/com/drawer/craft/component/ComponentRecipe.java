package com.drawer.craft.component;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class ComponentRecipe extends ShapedRecipe {

    public ComponentRecipe(NamespacedKey key, ItemStack result) {
        super(key, result);
    }
}
