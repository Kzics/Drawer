package com.drawer.obj.impl;

import com.drawer.DrawerAction;
import com.drawer.Main;
import com.drawer.components.ComponentAbility;
import com.drawer.display.ComponentDisplay;
import com.drawer.display.DrawerPart;
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

    public Drawer(Location location, int maximum,BlockFace face){
        this.location = location;
        this.maximum = maximum;
        this.face = face;
        this.isVoid = false;

        this.drawerParts = new ArrayList<>();
        this.components = new ArrayList<>();
    }

    public Drawer(Location location){
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
        component.apply(this,main);
    }


    public void removeComponent(ComponentAbility componentAbility, Main main){
        this.components.remove(componentAbility);
        componentAbility.getDisplay().remove();
        componentAbility.unApply(this,main);
    }

    public void enableVoid(){
        this.isVoid = true;
    }
    public void disableVoid(){
        this.isVoid = false;
    }
    public boolean areComponentHidden(){
        return componentHidden;
    }

    public void setComponentHidden(boolean bool){
        this.componentHidden = bool;

    }

    public ComponentAbility getComponent(String componentText){
        return this.components.stream().filter(c->c.getComponent().name().equals(componentText))
                .findFirst()
                .get();
    }

    public int getMaximum() {
        return maximum;
    }

    public Location getLocation() {
        return location;
    }

    public void setMaximum(int maximum){
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
            addAmount(amount, drawerPart);
        } else {
            removeAmount(amount, drawerPart);
        }

        drawerPart.getTextDisplay().setText(formatAmount(getAmount(drawerPart)));
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) return;
        drawerPart.getItemDisplay().setItemStack(itemStack);

        if(getAmount(drawerPart) <= 0) {
            setItem(drawerPart,new ItemStack(Material.AIR));
        }
    }

    public void removeDrawer(boolean real) {
        drawerParts.forEach(drawerPart -> {
            drawerPart.getTextDisplay().remove();
            drawerPart.getItemDisplay().remove();
        });


        if(real) location.getBlock().setType(Material.AIR);

        components.forEach(componentAbility -> componentAbility.getDisplay().remove());
    }

    public void removeAmount(final int amount, DrawerPart drawerPart) {
        int res = getAmount(drawerPart) - amount;
        if (res < 0) {
            res = 0;
        }
        setAmount(res, drawerPart);
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