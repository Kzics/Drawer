package com.drawer.craft.drawers;

import com.drawer.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.recipe.CraftingBookCategory;

public class BlankDrawerRecipe extends DrawerRecipe{
    public BlankDrawerRecipe(NamespacedKey key, ItemStack result, Main main) {
        super(key, result, main);

        setCategory(CraftingBookCategory.MISC);
        loadRecipe("blank");
    }
}
