package com.drawer.craft.component;

import com.drawer.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class VoidRecipe extends ComponentRecipe{
    public VoidRecipe(NamespacedKey key, ItemStack result, Main main) {
        super(key, result, main);

        loadRecipe("void-upgrade");
    }
}
