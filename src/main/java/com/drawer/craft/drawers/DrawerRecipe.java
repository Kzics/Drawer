package com.drawer.craft.drawers;

import com.drawer.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class DrawerRecipe extends ShapedRecipe {
    private final Main main;
    public DrawerRecipe(NamespacedKey key, ItemStack result, Main main) {
        super(key, result);

        this.main = main;
    }

    void loadRecipe(String type){
        if(!main.getConfig().getBoolean("drawers."+type+".enabled")) return;

        if (main.getConfig().contains(String.format("drawers.%s.craft",type))) {
            String[] shapeArray = main.getConfig().getStringList(String.format("drawers.%s.craft.shape",type)).toArray(new String[0]);
            shape(shapeArray);

            for (String ingredientKey : main.getConfig().getConfigurationSection(String.format("drawers.%s.craft.ingredients",type)).getKeys(false)) {
                char symbol = ingredientKey.charAt(0);
                Material ingredientMaterial = Material.valueOf(main.getConfig().getString(String.format("drawers.%s.craft.ingredients." + ingredientKey,type)));
                setIngredient(symbol, ingredientMaterial);
            }
        }
    }

    public void register(){
        main.getServer().addRecipe(this);
    }

}
