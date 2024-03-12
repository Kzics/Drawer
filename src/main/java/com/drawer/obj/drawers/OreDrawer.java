package com.drawer.obj.drawers;


import com.drawer.obj.impl.Drawer;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class OreDrawer extends Drawer {


    public OreDrawer(Location location, int maximum, BlockFace face) {
        super(location,maximum, face);
    }
}
