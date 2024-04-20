package com.drawer.obj.impl;

import com.drawer.DrawerAction;
import com.drawer.Main;
import com.drawer.components.ComponentAbility;
import com.drawer.display.ComponentDisplay;
import com.drawer.display.DrawerPart;
import com.drawer.display.PartType;
import com.drawer.utils.ConverterEnum;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.*;

public abstract class Drawer {

    protected Location location;
    private int maximum;
    public final List<DrawerPart> drawerParts;
    private final List<ComponentAbility> components;
    private BlockFace face;
    private boolean isVoid;
    private boolean componentHidden;

    public Drawer(Location location, int maximum, BlockFace face) {
        this.location = location;
        this.maximum = maximum;
        this.face = face;
        this.isVoid = false;

        this.drawerParts = new ArrayList<>();
        this.components = new ArrayList<>();
    }

    public Drawer(Location location) {
        this.location = location;

        this.drawerParts = null;
        this.components = null;
    }

    public BlockFace getFace() {
        return face;
    }

    public List<ComponentAbility> getComponents() {
        return components;
    }

    public void addComponent(ComponentAbility component, Main main) {
        if (this.getComponents().size() >= 5) return;

        double lateralOffset = -0.25 + this.getComponents().size() * 0.125;

        final ComponentDisplay componentDisplay = new ComponentDisplay(component, location, face, 0.9, 0.52, lateralOffset);
        component.setDisplay(componentDisplay);
        this.components.add(component);
        component.apply(this, main);
    }


    public void removeComponent(ComponentAbility componentAbility, Main main) {
        this.components.remove(componentAbility);
        componentAbility.getDisplay().remove();
        componentAbility.unApply(this, main);
    }

    public void enableVoid() {
        this.isVoid = true;
    }

    public void disableVoid() {
        this.isVoid = false;
    }

    public boolean areComponentHidden() {
        return componentHidden;
    }

    public void setComponentHidden(boolean bool) {
        this.componentHidden = bool;

    }

    public ComponentAbility getComponent(String componentText) {
        return this.components.stream().filter(c -> c.getComponent().name().equals(componentText))
                .findFirst()
                .get();
    }

    public int getMaximum() {
        return maximum;
    }

    public Location getLocation() {
        return location;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    private Optional<DrawerPart> findNearestCase(Location location) {
        return this.drawerParts.stream().min(Comparator.comparingDouble(drawerCase -> drawerCase.getItemDisplay().getDisplay().getLocation().distanceSquared(location)));
    }

    public List<DrawerPart> getDrawerParts() {
        return drawerParts;
    }

    public void updateDrawer(int amount, DrawerAction action, ItemStack itemStack, DrawerPart drawerPart) {
        if (action.equals(DrawerAction.ADD)) {
            if (!itemStack.getType().equals(getStoredItem(drawerPart).getType()) && !isEmpty(drawerPart)) return;
            drawerPart.getItemDisplay().setItemStack(itemStack);
            addAmount(amount, drawerPart);

        } else {
            System.out.println("removing");
            removeAmount(amount, drawerPart);
        }

        drawerPart.getTextDisplay().setText(formatAmount(getAmount(drawerPart)));
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) return;

        if (getAmount(drawerPart) <= 0) {
            setItem(drawerPart, new ItemStack(Material.AIR));
        }
    }

    public void removeDrawer(boolean real) {
        drawerParts.forEach(drawerPart -> {
            drawerPart.getTextDisplay().remove();
            drawerPart.getItemDisplay().remove();
        });

        if (real) location.getBlock().setType(Material.AIR);

        components.forEach(componentAbility -> componentAbility.getDisplay().remove());
    }

    public void removeAmount(final int amount, DrawerPart drawerPart) {
        int res = getAmount(drawerPart) - amount;
        if (res < 0) {
            res = 0;
        }
        setAmount(res, drawerPart);

        for (DrawerPart part : drawerParts) {
            if (part.equals(drawerPart)) continue;
            if (part.getItemDisplay().getItemStack() == null) continue;
            ItemStack itemStack = drawerPart.getItemDisplay().getItemStack();
            String itemName = itemStack.getType().name();

            if (itemName.contains("NUGGET")) {
                int equivalentIngotAmount = amount / 9;
                int equivalentBlockAmount = amount / 81;
                adjustAmount(part, equivalentIngotAmount, equivalentBlockAmount);
            } else if (itemName.contains("INGOT")) {
                int equivalentNuggetAmount = amount * 9; // Assuming 1 ingot = 9 nuggets
                int equivalentBlockAmount = amount; // 1 ingot = 1/9 block
                adjustAmount(part, equivalentNuggetAmount, equivalentBlockAmount);
            } else if (itemName.contains("BLOCK")) {
                int equivalentNuggetAmount = amount * 81; // Assuming 1 block = 81 nuggets
                int equivalentIngotAmount = amount * 9; // Assuming 1 block = 9 ingots
                adjustAmount(part, equivalentNuggetAmount, equivalentIngotAmount);
            }
        }
    }

    private void adjustAmount(DrawerPart part, int nuggetAmount, int ingotOrBlockAmount) {
        ItemStack itemStack = part.getItemDisplay().getItemStack();
        String itemName = itemStack.getType().name();
        int res;

        if(itemName.contains("NUGGET")){
            res = getAmount(part) - nuggetAmount;
        } else { // For both INGOT and BLOCK
            res = getAmount(part) - ingotOrBlockAmount;
        }

        if (res < 0) {
            res = 0;
        }
        setAmount(res, part);
    }




    public void setItem(DrawerPart drawerPart, ItemStack stack) {
        drawerPart.getItemDisplay().setItemStack(stack);
    }

    public ItemStack getStoredItem(DrawerPart drawerPart) {
        return drawerPart.getItemDisplay().getItemStack();
    }

    public boolean canReceive(DrawerPart drawerPart){
        return drawerPart.getText().asInt() < getMaximum() || this.isVoid;
    }

    public boolean isEmpty(DrawerPart drawerPart) {
        return getAmount(drawerPart) == 0;
    }

    protected void setAmount(final int amount, DrawerPart drawerPart) {
        drawerPart.getTextDisplay().setText(String.valueOf(amount));
        /*if (drawerPart == topDrawerPart) {
            this.amountTop = amount;
        } else if (drawerPart == bottomDrawerPart) {
            this.amountBot = amount;
        }*/
    }

    protected void addAmount(final int amount, DrawerPart drawerPart) {
        if(drawerPart.getItemDisplay().getPartType().equals(PartType.LINKED)){
            if (this.drawerParts != null) {
                ItemStack initialItem = drawerPart.getItemDisplay().getItemStack();
                ConverterEnum converterVal = ConverterEnum.valueOf(initialItem.getType().name());
                int index = 0;
                for (DrawerPart others : this.drawerParts){
                    if(others.equals(drawerPart)) continue;

                    others.getItemDisplay().setItemStack(new ItemStack(converterVal.getMaterials()[index]));
                    setAmount((int) (getAmount(others) + initialItem.getAmount() * Math.pow(9,index + 1)), others);
                    others.getTextDisplay().setText(formatAmount(getAmount(others)));
                    index ++;
                }
            }
        }
        setAmount(getAmount(drawerPart) + amount, drawerPart);

    }

    public DrawerPart getPart(Location location){
        Optional<DrawerPart> part = findNearestCase(location);

        return part.orElse(null);
    }
    public int getAmount(DrawerPart part) {
        return part.getText().asInt();
    }

    private String formatAmount(int amount) {
        if (amount >= 1000) {
            double formattedAmount = amount / 1000.0;
            return new DecimalFormat("#.#k").format(formattedAmount);
        } else {
            return String.valueOf(amount);
        }
    }
    private int parseAmount(String amount) {
        if (amount.endsWith("k")) {
            String numberPart = amount.substring(0, amount.length() - 1);
            double number = Double.parseDouble(numberPart);
            return (int) (number * 1000);
        } else {
            return Integer.parseInt(amount);
        }
    }


}