package com.drawer.display;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;

public class DrawerDisplay {
    protected Display display;

    public DrawerDisplay(Display display) {
        this.display = display;
    }

    protected float getRotateYFromBlockFace(BlockFace face) {
        switch (face) {
            case NORTH:
                return (float) Math.PI;
            case EAST:
                return (float) (Math.PI / 2);
            case SOUTH:
                return 0;
            case WEST:
                return (float) (Math.PI * 3 / 2);
            default:
                return 0;
        }
    }

    protected void rotate(float x, float y, float z) {
        Transformation transformation = display.getTransformation();
        Quaternionf quaternion = new Quaternionf().rotationXYZ(x, y, z);
        transformation.getLeftRotation().set(quaternion);
        display.setTransformation(transformation);
    }

    public Display getDisplay() {
        return display;
    }

    public void remove(){
        display.remove();
    }
}
