package com.drawer.obj.drawers;

import com.drawer.obj.impl.Drawer;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class BlankDrawer extends Drawer {
    public BlankDrawer(Location location, int maximum, BlockFace face) {
        super(location, maximum, face);
    }
}
