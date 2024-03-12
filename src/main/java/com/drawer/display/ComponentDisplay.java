package com.drawer.display;

import com.drawer.components.ComponentAbility;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;

public class ComponentDisplay extends DrawerDisplay {

    private ComponentAbility components;
    private ItemStack cache;

    public ComponentDisplay(ComponentAbility component, Location location, BlockFace face, double verticalOffset, double horizontalOffset, double lateralOffset) {
        super(location.getWorld().spawn(location.clone().add(
                0.5 + horizontalOffset * face.getModX() + lateralOffset * face.getModZ(),
                verticalOffset,
                0.5 + horizontalOffset * face.getModZ() + lateralOffset * face.getModX()
        ), ItemDisplay.class, (item)->{
            Transformation transformation = item.getTransformation();
            transformation.getScale().set(0.25, 0.25, 0);
            item.setInvulnerable(false);
            item.setTransformation(transformation);
        }));
        this.components = component;

        rotate(0, getRotateYFromBlockFace(face), 0);
        ((ItemDisplay)display).setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GUI);
        display.setBrightness(new Display.Brightness(15, 15));
        display.setDisplayHeight(0.25f);
        display.setDisplayWidth(0.25f);
        ((ItemDisplay)display).setItemStack(new ItemStack(component.getComponent().getMaterial()));
    }


    public void hide(){
        this.cache = ((ItemDisplay)display).getItemStack();
        ((ItemDisplay) display).setItemStack(null);
    }

    public void show(){
        ((ItemDisplay)display).setItemStack(cache);
        this.cache = null;
    }

    public void setComponent(ComponentAbility component){
        this.components = component;

        ((ItemDisplay)display).setItemStack(new ItemStack(component.getComponent().getMaterial()));
        display.setGlowColorOverride(component.getComponent().getColor());
    }

    public ComponentAbility getComponents() {
        return components;
    }
}

