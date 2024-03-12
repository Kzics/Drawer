package com.drawer.craft.drawers;

import com.drawer.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.recipe.CraftingBookCategory;

public class SuperAdvancedDrawerRecipe extends DrawerRecipe{

    public SuperAdvancedDrawerRecipe(NamespacedKey key, ItemStack result, Main main) {
        super(key, result, main);

        setCategory(CraftingBookCategory.MISC);

        loadRecipe("superadvanced");
    }
}
