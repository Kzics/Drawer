package com.drawer.obj.drawers;

import com.drawer.DrawerAction;
import com.drawer.display.DrawerItemDisplay;
import com.drawer.display.DrawerTextDisplay;
import com.drawer.display.DrawerPart;
import com.drawer.obj.impl.Drawer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class AdvancedDrawer extends Drawer {
    public AdvancedDrawer(Location location, int maximum, BlockFace face) {
        super(location, maximum, face);
        this.setMaximum(maximum);

       /* double verticalOffsetTop = 0.5;
        double verticalOffsetBottom = 0;

        DrawerTextDisplay textDisplayBottom = new DrawerTextDisplay(location, face, verticalOffsetBottom, 0.51);
        DrawerItemDisplay itemDisplayBottom = new DrawerItemDisplay(location, face, verticalOffsetBottom + 0.3, 0.51);
        itemDisplayBottom.setItemStack(new ItemStack(material));

        textDisplayBottom.setText(String.valueOf(0));

        DrawerTextDisplay textDisplayTop = new DrawerTextDisplay(location, face, verticalOffsetTop, 0.51);
        DrawerItemDisplay itemDisplayTop = new DrawerItemDisplay(location, face, verticalOffsetTop + 0.3, 0.51);
        textDisplayTop.setText(String.valueOf(0));

        this.bottomDrawerPart = new DrawerPart(itemDisplayBottom, textDisplayBottom);
        this.topDrawerPart = new DrawerPart(itemDisplayTop, textDisplayTop);

        drawerParts.add(bottomDrawerPart);
        drawerParts.add(topDrawerPart);*/
    }

    /*public DrawerPart getTopDrawerPart() {
        return topDrawerPart;
    }

    public DrawerPart getBottomDrawerPart() {
        return bottomDrawerPart;
    }*/

    public Location getLocation() {
        return location;
    }

    protected String formatAmount(int amount) {
        if (amount >= 1000) {
            double formattedAmount = amount / 1000.0;
            return new DecimalFormat("#.#k").format(formattedAmount);
        } else {
            return String.valueOf(amount);
        }
    }
}
