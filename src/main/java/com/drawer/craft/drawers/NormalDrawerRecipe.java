package com.drawer.craft.drawers;

import com.drawer.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.recipe.CraftingBookCategory;

public class NormalDrawerRecipe extends DrawerRecipe{

    private final Main main;
    public NormalDrawerRecipe(NamespacedKey key, ItemStack result, Main main) {
        super(key, result,main);
        this.main = main;

        setCategory(CraftingBookCategory.MISC);
        loadRecipe("normal");
    }

}
