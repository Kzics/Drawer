package com.drawer.craft.component;

import com.drawer.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;

public class ComponentRecipe extends ShapedRecipe {

    private final Main main;
    public ComponentRecipe(NamespacedKey key, ItemStack result, Main main) {
        super(key, result);
        setCategory(CraftingBookCategory.MISC);

        this.main = main;

    }

    void loadRecipe(String type){
        if (main.getConfig().contains(String.format("components.%s.craft",type))) {
            String[] shapeArray = main.getConfig().getStringList(String.format("components.%s.craft.shape",type)).toArray(new String[0]);
            shape(shapeArray);

            for (String ingredientKey : main.getConfig().getConfigurationSection(String.format("components.%s.craft.ingredients",type)).getKeys(false)) {
                char symbol = ingredientKey.charAt(0);
                Material ingredientMaterial = Material.valueOf(main.getConfig().getString(String.format("components.%s.craft.ingredients." + ingredientKey,type)));
                setIngredient(symbol, ingredientMaterial);
            }
        }
    }

    public void register(){
        main.getServer().addRecipe(this);
    }
}
