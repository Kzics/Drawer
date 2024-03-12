package com.drawer.components;

import org.bukkit.Color;
import org.bukkit.Material;

public enum Components {

    MAX_UPGRADE_1(Material.TRIPWIRE_HOOK,Color.GREEN),
    MAX_UPGRADE_2(Material.TRIPWIRE_HOOK, Color.RED),
    MAX_UPGRADE_3(Material.TRIPWIRE_HOOK,Color.BLUE),
    HIDE_KEY(Material.TRIPWIRE_HOOK,Color.ORANGE),
    HIDE_UPGRADE(Material.TRIPWIRE_HOOK,Color.BLUE),
    VOID_UPGRADE(Material.MEDIUM_AMETHYST_BUD,Color.PURPLE);

    private final Material material;
    private final Color color;
    Components(final Material material, Color color){
        this.material = material;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public Material getMaterial() {
        return material;
    }
}
