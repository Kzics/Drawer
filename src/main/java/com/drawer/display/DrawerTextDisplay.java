package com.drawer.display;

import com.drawer.utils.TextFormat;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;

public class DrawerTextDisplay extends DrawerDisplay {

    private final Location location;
    private final BlockFace face;
    private final double verticalOffset;
    private final double horizontalOffset;
    private final double lateralOffset;
    public DrawerTextDisplay(Location location, BlockFace face, double verticalOffset, double horizontalOffset) {
        super(location.getWorld().spawn(location.clone().add(
                0.5 + horizontalOffset * face.getModX(),
                verticalOffset,
                0.5 + horizontalOffset * face.getModZ()
        ), TextDisplay.class));
        this.location = location;
        this.face = face;
        this.verticalOffset = verticalOffset;
        this.horizontalOffset = horizontalOffset;
        this.lateralOffset = -1;


        rotate(0, getRotateYFromBlockFace(face), 0);
        ((TextDisplay)display).setAlignment(TextDisplay.TextAlignment.CENTER);
        display.getTransformation().getScale().add(-0.5f,-0.5f,-0.5f);
        display.setBrightness(new Display.Brightness(15, 15));
        display.setDisplayHeight(0);
        display.setDisplayWidth(0);
    }
    public DrawerTextDisplay(Location location, BlockFace face, double verticalOffset, double horizontalOffset, double lateralOffset) {
        super(location.getWorld().spawn(location.clone().add(
                0.5 + horizontalOffset * face.getModX() + lateralOffset * face.getModZ(),
                verticalOffset,
                0.5 + horizontalOffset * face.getModZ() + lateralOffset * face.getModX()
        ), TextDisplay.class));

        this.location = location;
        this.face = face;
        this.verticalOffset = verticalOffset;
        this.horizontalOffset = horizontalOffset;
        this.lateralOffset = lateralOffset;

        rotate(0, getRotateYFromBlockFace(face), 0);
        ((TextDisplay) display).setAlignment(TextDisplay.TextAlignment.CENTER);
        display.getTransformation().getScale().add(-0.5f, -0.5f, -0.5f);
        display.setBrightness(new Display.Brightness(15, 15));
        display.setDisplayHeight(0);
        display.setDisplayWidth(0);

        display.setPersistent(true);
    }

    public DrawerTextDisplay(Location location, BlockFace face, double verticalOffset, double horizontalOffset, double lateralOffset,boolean spawn) {
        super(location.getWorld().spawn(location.clone().add(
                0.5 + horizontalOffset * face.getModX() + lateralOffset * face.getModZ(),
                verticalOffset,
                0.5 + horizontalOffset * face.getModZ() + lateralOffset * face.getModX()
        ), TextDisplay.class));

        this.location = location;
        this.face = face;
        this.verticalOffset = verticalOffset;
        this.horizontalOffset = horizontalOffset;
        this.lateralOffset = lateralOffset;

        rotate(0, getRotateYFromBlockFace(face), 0);
        ((TextDisplay) display).setAlignment(TextDisplay.TextAlignment.CENTER);
        display.getTransformation().getScale().add(-0.5f, -0.5f, -0.5f);
        display.setBrightness(new Display.Brightness(15, 15));
        display.setDisplayHeight(0);
        display.setDisplayWidth(0);
    }

    public DrawerTextDisplay(Location location, BlockFace face, double verticalOffset, double horizontalOffset, double lateralOffset, double scale) {
        super(location.getWorld().spawn(location.clone().add(
                0.5 + horizontalOffset * face.getModX() + lateralOffset * face.getModZ(),
                verticalOffset,
                0.5 + horizontalOffset * face.getModZ() + lateralOffset * face.getModX()
        ), TextDisplay.class,(textDisplay -> {
            Transformation transformation = textDisplay.getTransformation();
            transformation.getScale().set(scale);
            textDisplay.setTransformation(transformation);
        })));

        this.location = location;
        this.face = face;
        this.verticalOffset = verticalOffset;
        this.horizontalOffset = horizontalOffset;
        this.lateralOffset = -1;


        rotate(0, getRotateYFromBlockFace(face), 0);
        ((TextDisplay) display).setAlignment(TextDisplay.TextAlignment.CENTER);
        display.getTransformation().getScale().add(-0.5f, -0.5f, -0.5f);
        display.setBrightness(new Display.Brightness(15, 15));
        display.setDisplayHeight(0);
        display.setDisplayWidth(0);
    }

    public int getEntityId(){
        return display.getEntityId();
    }

    public Location getLocation() {
        return location;
    }
    public void changeScale(double scale){
        display.getTransformation().getScale().set(scale);
    }

    public float getScale(){
        return display.getTransformation().getScale().x;
    }

    public BlockFace getFace() {
        return face;
    }

    public double getHorizontalOffset() {
        return horizontalOffset;
    }

    public double getLateralOffset() {
        return lateralOffset;
    }

    public double getVerticalOffset() {
        return verticalOffset;
    }

    public void setText(String text){
        if(display instanceof TextDisplay textDisplay){
            textDisplay.setText(text);
        }
    }
    
    public TextFormat getText(){
        if(display instanceof TextDisplay textDisplay){
            return new TextFormat(textDisplay.getText());
        }
        return null;
    }

}

