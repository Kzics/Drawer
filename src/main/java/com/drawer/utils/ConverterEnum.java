package com.drawer.utils;

import org.bukkit.Material;

public enum ConverterEnum {
    DIAMOND_BLOCK(Material.DIAMOND,Material.AIR),
    GOLD_BLOCK(Material.GOLD_INGOT,Material.GOLD_NUGGET),
    IRON_BLOCK(Material.IRON_INGOT, Material.IRON_NUGGET),
    REDSTONE_BLOCK(Material.REDSTONE,Material.AIR),
    LAPIS_BLOCK(Material.LAPIS_LAZULI,Material.AIR),
    COAL_BLOCK(Material.COAL,Material.AIR);

    Material[] materials;
    ConverterEnum(Material ... materials){
        this.materials = materials;
    }

    public Material[] getMaterials() {
        return materials;
    }
}
