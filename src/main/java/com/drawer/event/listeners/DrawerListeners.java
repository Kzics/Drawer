package com.drawer.event.listeners;

import com.drawer.DrawerAction;
import com.drawer.Main;
import com.drawer.display.DrawerPart;
import com.drawer.event.DrawerAddEvent;
import com.drawer.event.DrawerPlaceEvent;
import com.drawer.event.DrawerRemoveEvent;
import com.drawer.menu.DrawerMenu;
import com.drawer.obj.impl.Drawer;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class DrawerListeners implements Listener {


    private final Main main;
    public DrawerListeners(final Main main){
        this.main = main;
    }

    @EventHandler
    public void onDrawerPlace(DrawerPlaceEvent event){
    }


    public void onDrawerAdd(DrawerAddEvent event){
        handleDrawerInteraction(event.getEvent(), DrawerAction.ADD, event.getEvent().getItem());
    }

    public void onDrawerRemove(DrawerRemoveEvent event){
        handleDrawerInteraction(event.getEvent(), DrawerAction.REMOVE, event.getEvent().getItem());

    }

    private void handleDrawerInteraction(PlayerInteractEvent event, DrawerAction action, ItemStack item) {
        if(Main.getDrawersManager().isFrozen() && !event.getPlayer().hasPermission("drawer.freeze.use")) return;

        Player player = event.getPlayer();
        Vector direction = player.getEyeLocation().getDirection();
        Location startLocation = player.getEyeLocation().add(direction.multiply(1.5));

        RayTraceResult rayTraceResult = player.getWorld().rayTrace(startLocation, direction, 10, FluidCollisionMode.ALWAYS, true, 0.2, null);
        Drawer advancedDrawer = Main.getDrawersManager().getDrawer(event.getClickedBlock().getLocation());
        if (rayTraceResult.getHitEntity() instanceof ItemDisplay itemDisplay) {
            DrawerPart part = advancedDrawer.getPart(itemDisplay.getLocation());

            if(part == null) return;
            if(event.getItem() != null && event.getItem().getItemMeta() != null && event.getItem().getItemMeta().getPersistentDataContainer().has(main.componentKey, PersistentDataType.STRING)){
                String component = event.getItem().getItemMeta().getPersistentDataContainer().get(main.componentKey,PersistentDataType.STRING);
                event.setCancelled(true);

                if(component.equals("HIDE_KEY")) {
                    if (part.isHidden()) {
                        part.show();
                    } else {
                        part.hide();
                    }
                    return;
                }

                if(component.equals("HIDE_UPGRADE")){
                    if(advancedDrawer.areComponentHidden()){
                        advancedDrawer.getComponents().forEach(c->c.getDisplay().hide());
                        advancedDrawer.setComponentHidden(true);
                    }else{
                        advancedDrawer.getComponents().forEach(c->c.getDisplay().show());
                        advancedDrawer.setComponentHidden(false);
                    }
                }
                return;
            }
            if (action == DrawerAction.ADD && item != null &&  !item.getType().equals(Material.AIR)) {
                event.setCancelled(true);

                if (advancedDrawer.getStoredItem(part) == null || advancedDrawer.getStoredItem(part).getType().equals(Material.AIR)) {
                    advancedDrawer.updateDrawer(item.getAmount(),DrawerAction.ADD,item,part);
                    player.getInventory().setItem(event.getHand(),null);
                } else {
                    if (player.isSneaking()) {
                        int max = advancedDrawer.getMaximum();
                        int current = advancedDrawer.getAmount(part);

                        if (current < max) {
                            for (int i = 0; i < player.getInventory().getSize(); i++) {
                                ItemStack inventoryItem = player.getInventory().getItem(i);
                                if (inventoryItem == null) continue;
                                if (inventoryItem.getType().equals(advancedDrawer.getStoredItem(part).getType())) {
                                    int stackSize = inventoryItem.getAmount();
                                    if (current + stackSize <= max) {
                                        current += stackSize;
                                        player.getInventory().setItem(i, new ItemStack(Material.AIR));
                                    } else {
                                        int diff = max - current;
                                        inventoryItem.setAmount(stackSize - diff);
                                        current = max;
                                    }
                                }
                                if (current == max) break;
                            }
                            advancedDrawer.updateDrawer(current - advancedDrawer.getAmount(part), DrawerAction.ADD, item, part);
                        }
                    } else {
                        event.setCancelled(true);
                        int amountToAdd = Math.min(item.getAmount(), advancedDrawer.getMaximum() - advancedDrawer.getAmount(part));
                        if (amountToAdd > 0) {
                           /* if (advancedDrawer instanceof OreDrawer) {
                                int blockAmount = advancedDrawer.getAmount(part) + item.getAmount();
                                int ingotAmount = blockAmount * 9;
                                int nuggets = ingotAmount * 9;

                                if (!item.getType().equals(Material.AIR)) {
                                    ItemStack firstState = new ItemStack(ConverterEnum.valueOf(item.getType().name()).getMaterials()[0]);
                                    ItemStack secondState = new ItemStack(ConverterEnum.valueOf(item.getType().name()).getMaterials()[1]);

                                    if (!firstState.getType().equals(Material.AIR))
                                        advancedDrawer.updateDrawer(ingotAmount, DrawerAction.ADD, firstState, advancedDrawer.drawerParts.get(1));
                                    if (!secondState.getType().equals(Material.AIR))
                                        advancedDrawer.updateDrawer(nuggets, DrawerAction.ADD, secondState, advancedDrawer.drawerParts.get(2));
                                }
                            }*/

                            advancedDrawer.updateDrawer(amountToAdd, DrawerAction.ADD, item, part);
                            item.setAmount(item.getAmount() - amountToAdd);
                            if (item.getAmount() == 0) {
                                player.getInventory().setItemInMainHand(null);
                            } else {
                                player.getInventory().setItemInMainHand(item);
                            }
                        }
                    }
                }

            } else if (action == DrawerAction.REMOVE) {
                event.setCancelled(true);

                int drawerContents = advancedDrawer.getAmount(part);
                if (drawerContents <= 0) {
                    return;
                }
                int removeAmount;

                if (player.isSneaking()) {
                    int slots = 0;

                    for (int i = 0; i < 36; i++) {
                        ItemStack stack = player.getInventory().getItem(i);
                        if (stack == null || stack.getType().equals(Material.AIR)) {
                            slots++;
                        }
                    }

                    removeAmount = Math.min(slots * 64, drawerContents);
                } else {
                    removeAmount = Math.min(drawerContents, 64);
                }

                /*if (advancedDrawer instanceof OreDrawer) {
                    switch (drawerPart.getType()) {
                        case NORMAL -> {
                            advancedDrawer.updateDrawer(removeAmount, DrawerAction.REMOVE, item, drawerPart);
                        }
                        case INGOT -> {
                            advancedDrawer.updateDrawer(removeAmount, DrawerAction.REMOVE, item, drawerPart);
                            for (DrawerPart parts : advancedDrawer.drawerParts){
                                if(parts.getType().equals(PartType.NUGGETS)){
                                    advancedDrawer.updateDrawer(removeAmount*9, DrawerAction.REMOVE, item, parts);
                                }
                            }
                        }
                        case NUGGETS -> {
                            //advancedDrawer.updateDrawer(removeAmount, DrawerAction.REMOVE, item, drawerPart);
                        }
                    }
                }*/
                ItemStack cloneItem = advancedDrawer.getStoredItem(part).clone();
                cloneItem.setAmount(removeAmount);

                player.getInventory().addItem(cloneItem);

                advancedDrawer.updateDrawer(removeAmount,DrawerAction.REMOVE,cloneItem,part);
            }
        }else{
            if(player.isSneaking()) new DrawerMenu(advancedDrawer,main).open(player);
        }
    }
}
