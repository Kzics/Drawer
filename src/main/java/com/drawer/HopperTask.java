package com.drawer;

import com.drawer.blocks.DrawerController;
import com.drawer.display.DrawerPart;
import com.drawer.obj.drawers.NormalDrawer;
import com.drawer.obj.impl.Drawer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class HopperTask extends BukkitRunnable {

    private final DrawersManager drawersManager;

    public HopperTask() {
        this.drawersManager = Main.getDrawersManager();
    }

    @Override
    public void run() {
        for (DrawerController controller : drawersManager.getControllers().values()) {
            if (controller.canTransfer()) {
                transferFromHopper(controller);
            }
        }
    }

    private void transferFromHopper(DrawerController controller) {
        Location hopperLocation = controller.getLocation().getBlock().getRelative(BlockFace.WEST).getLocation();
        Block hopperBlock = hopperLocation.getBlock();

        if (hopperBlock.getType() == Material.HOPPER) {
            Inventory hopperInventory = ((Hopper)hopperBlock.getState()).getInventory();

            for (int i = 0; i < hopperInventory.getSize(); i++) {
                ItemStack hopperItem = hopperInventory.getItem(i);

                if (hopperItem != null && hopperItem.getAmount() >= 2) {
                    ItemStack itemToTransfer = new ItemStack(hopperItem.getType(), 2);

                    for (Drawer affectedDrawer : controller.getAffectedDrawers()) {
                        for (DrawerPart drawerPart : affectedDrawer.getDrawerParts()) {
                            if (affectedDrawer.canReceive(drawerPart) && affectedDrawer.getStoredItem(drawerPart) != null && affectedDrawer.getStoredItem(drawerPart).getType().equals(hopperItem.getType())) {
                                affectedDrawer.updateDrawer(2, DrawerAction.ADD, itemToTransfer, drawerPart);
                                hopperItem.setAmount(hopperItem.getAmount() - 2);
                                hopperInventory.setItem(i, hopperItem);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}
