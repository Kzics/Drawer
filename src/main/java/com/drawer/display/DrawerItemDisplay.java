package com.drawer.display;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;

public class DrawerItemDisplay extends DrawerDisplay {
    private final Location location;
    private final BlockFace face;
    private final double verticalOffset;
    private final double horizontalOffset;
    private final double lateralOffset;
    private PartType partType;
    public DrawerItemDisplay(Location location, BlockFace face, double verticalOffset, double horizontalOffset) {
        super(location.getWorld().spawn(location.clone().add(
                0.5 + horizontalOffset * face.getModX(),
                verticalOffset,
                0.5 + (horizontalOffset + 0.01) * face.getModZ()
        ), ItemDisplay.class, (item)->{
            Transformation transformation = item.getTransformation();
            transformation.getScale().set(0.25, 0.25, 0);
            item.setInvulnerable(false);
            item.setTransformation(transformation);
        }));

        this.location = location;
        this.face = face;
        this.verticalOffset = verticalOffset;
        this.horizontalOffset = horizontalOffset;
        this.lateralOffset = -1;

        rotate(0, getRotateYFromBlockFace(face) , 0);
        ((ItemDisplay)display).setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GUI);
        display.setBrightness(new Display.Brightness(15, 15));
        display.setDisplayHeight(0.25f);
        display.setDisplayWidth(0.25f);
    }

    public DrawerItemDisplay(Location location, BlockFace face, double verticalOffset, double horizontalOffset, double lateralOffset, PartType partType) {
        super(location.getWorld().spawn(location.clone().add(
                0.5 + horizontalOffset * face.getModX() + lateralOffset * face.getModZ(),
                verticalOffset,
                0.5 + (horizontalOffset + 0.01) * face.getModZ() + lateralOffset * face.getModX()
        ), ItemDisplay.class, (item) -> {
            Transformation transformation = item.getTransformation();
            transformation.getScale().set(0.25, 0.25, 0);
            item.setInvulnerable(false);
            item.setTransformation(transformation);
        }));
        this.location = location;
        this.face = face;
        this.verticalOffset = verticalOffset;
        this.horizontalOffset = horizontalOffset;
        this.lateralOffset = lateralOffset;

        rotate(0, getRotateYFromBlockFace(face.getOppositeFace()), 0);
        ((ItemDisplay) display).setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GUI);
        display.setBrightness(new Display.Brightness(15, 15));
        display.setDisplayHeight(0.25f);
        display.setDisplayWidth(0.25f);
        display.setPersistent(true);
        this.partType = partType;

    }

    public void setItemStack(ItemStack stack){
        if(display instanceof ItemDisplay itemDisplay){
            itemDisplay.setItemStack(stack);
        }
    }

    public PartType getPartType() {
        return partType;
    }

    public float getScale(){
        return display.getTransformation().getScale().x;
    }

    public void changeScale(double scale){
        display.getTransformation().getScale().set(scale);
    }

    public ItemStack getItemStack(){
        if(display instanceof ItemDisplay itemDisplay){
            return itemDisplay.getItemStack();
        }
        return null;
    }

    public int getEntityId(){
        return display.getEntityId();
    }

    public double getVerticalOffset() {
        return verticalOffset;
    }

    public double getLateralOffset() {
        return lateralOffset;
    }

    public double getHorizontalOffset() {
        return horizontalOffset;
    }

    public BlockFace getFace() {
        return face;
    }

    public Location getLocation() {
        return location;
    }
}
