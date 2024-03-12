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

        //double offsetX = 0.5 + face.getModX() * 0.53;
        double offsetY = 0.2 + face.getModY();
       /* double offsetZ = 0.5 + (face.getModZ() )* 0.53;

        float rotateX = 0;
        float rotateY = getRotateYFromBlockFace(face);
        float rotateZ = 0;*/

        //this.component = new ComponentDisplay(new MaxUpgradeAbility(Components.MAX_UPGRADE_1),location,face,0.9,0.53);
        //addComponent(new HideAbility());
        /*this.itemDisplay = new DrawerItemDisplay(location, face,0.7 ,0.53);
        this.textDisplay = new DrawerTextDisplay(location, face, offsetY, 0.53);
        this.textDisplay.setText(formatAmount(getAmount()));*/
    }


    /*public void setComponent(Components component){
        switch (component){
            case MAX_UPGRADE_1, MAX_UPGRADE_2, MAX_UPGRADE_3 -> this.component.setComponent(new MaxUpgradeAbility(component));
            case HIDE_KEY -> this.component.setComponent(new HideAbility());
        }
        if(!component.equals(Components.HIDE_KEY)) setHidden(false);

        this.component.getComponents().apply(this,);
    }*/

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
