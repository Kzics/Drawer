package com.drawer.obj.drawers;


import com.drawer.display.DrawerPart;
import com.drawer.obj.impl.Drawer;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

public class OreDrawer extends Drawer {
    public OreDrawer(Location location, int maximum, BlockFace face) {
        super(location, maximum, face);
    }


    public void multiRemove(int amount, DrawerPart from) {
        ItemStack itemStack = from.getItemDisplay().getItemStack();
        int storedAmount = getAmount(from);

        for (DrawerPart part : drawerParts) {
            String partName = part.getItemDisplay().getItemStack().getType().name();
            if (part.equals(from)) continue;

            if (itemStack.getType().name().contains("INGOT")) {
                if (partName.contains("NUGGET")) {
                    setAmount(storedAmount * 9, part);
                } else {
                    setAmount(storedAmount / 9, part);
                }
            } else if (itemStack.getType().name().contains("NUGGET")) {
                if (partName.contains("INGOT")) {
                    setAmount(storedAmount / 9, part);
                } else {
                    setAmount((int) (storedAmount / Math.pow(9,2)), part);
                }
            }else if(itemStack.getType().name().contains("BLOCK")){
                if(partName.contains("INGOT")){
                    setAmount(storedAmount * 9, part);
                }else{
                    setAmount((int) (storedAmount * Math.pow(9,2)), part);
                }
            }
        }
    }
}