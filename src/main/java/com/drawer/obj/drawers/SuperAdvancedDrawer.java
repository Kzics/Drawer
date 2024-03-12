package com.drawer.obj.drawers;

import com.drawer.obj.impl.Drawer;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;


public class SuperAdvancedDrawer extends Drawer {
    public SuperAdvancedDrawer(Location location, int maximum, BlockFace face) {
        super(location, maximum, face);
        this.location = location;
        this.setMaximum(maximum);

        /*double verticalOffsetTop = 0.5;
        double verticalOffsetBottom = 0;
        textDisplayBottom = new DrawerTextDisplay(location, face, verticalOffsetBottom, 0.51, -0.25);
        textDisplayBottom.getDisplay().setGlowColorOverride(Color.ORANGE);
        itemDisplayBottom = new DrawerItemDisplay(location, face, verticalOffsetBottom + 0.3, 0.51, -0.25);
        textDisplayBottom.setText(String.valueOf(amountBotLeft + amountBotRight));

        textDisplayTop = new DrawerTextDisplay(location, face, verticalOffsetTop, 0.51, 0.25);
        textDisplayTop.getDisplay().setGlowColorOverride(Color.BLUE);
        itemDisplayTop = new DrawerItemDisplay(location, face, verticalOffsetTop + 0.3, 0.51, 0.25);
        textDisplayTop.setText(String.valueOf(amountTopLeft + amountTopRight));

        double verticalOffsetLeft = 0.5;
        double verticalOffsetRight = 0;

        // Left Displays
        textDisplayLeft = new DrawerTextDisplay(location, face, verticalOffsetLeft, 0.51, -0.25);
        textDisplayLeft.getDisplay().setGlowColorOverride(Color.GREEN);

        itemDisplayLeft = new DrawerItemDisplay(location, face, verticalOffsetLeft + 0.3, 0.51, -0.25);
        textDisplayLeft.setText(String.valueOf(amountBotLeft));

        // Right Displays
        textDisplayRight = new DrawerTextDisplay(location, face, verticalOffsetRight, 0.51, 0.25);
        itemDisplayRight = new DrawerItemDisplay(location, face, verticalOffsetRight + 0.3, 0.51, 0.25);
        textDisplayRight.setText(String.valueOf(amountBotRight));*/
    }

}