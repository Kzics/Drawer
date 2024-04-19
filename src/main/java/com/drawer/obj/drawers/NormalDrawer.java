package com.drawer.obj.drawers;

import com.drawer.DrawerAction;
import com.drawer.components.Components;
import com.drawer.components.HideAbility;
import com.drawer.components.MaxUpgradeAbility;
import com.drawer.display.ComponentDisplay;
import com.drawer.display.DrawerItemDisplay;
import com.drawer.display.DrawerPart;
import com.drawer.display.DrawerTextDisplay;
import com.drawer.obj.impl.Drawer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class NormalDrawer extends Drawer {
    private final Location location;
    private ComponentDisplay component;
    private boolean hidden;
    private ItemStack itemCache;

    public NormalDrawer(Location location, int maximum, BlockFace face) {
        super(location, maximum, face);
        this.location = location;
        this.hidden = false;
    }
    public void setHidden(boolean bool){
        this.hidden = bool;
        if(bool){
            for (DrawerPart part : drawerParts){
                setItem(part,new ItemStack(Material.AIR));
            }
        }else{
            for (DrawerPart part : drawerParts){
                setItem(part,itemCache);
            }
        }
    }
}
