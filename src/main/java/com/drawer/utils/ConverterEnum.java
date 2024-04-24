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

    public Material getIngot(){
        return materials[0];
    }

    public Material getNugget(){
        return materials[1];
    }

    public static ConverterEnum getConverter(Material material){
        for(ConverterEnum converterEnum : values()){
            if(converterEnum.getIngot() == material || converterEnum.getNugget() == material){
                return converterEnum;
            }
        }
        return null;
    }
}
