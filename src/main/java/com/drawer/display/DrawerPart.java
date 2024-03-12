package com.drawer.display;

import com.drawer.utils.TextFormat;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;

public class DrawerPart {

    DrawerItemDisplay itemDisplay;
    DrawerTextDisplay textDisplay;
    PartType type;
    private boolean hidden;
    private ItemStack cache;

    public DrawerPart(DrawerItemDisplay itemDisplay, DrawerTextDisplay display){
        this.itemDisplay = itemDisplay;
        this.textDisplay = display;
        this.type = PartType.NORMAL;
        this.hidden = false;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void hide(){
        this.hidden = true;
        this.cache = getItemDisplay().getItemStack();

        getItemDisplay().setItemStack(null);
    }

    public void show(){
        this.hidden = false;

        getItemDisplay().setItemStack(this.cache);
        this.cache = null;
    }

    public DrawerItemDisplay getItemDisplay() {
        return itemDisplay;
    }

    public BlockFace getFace(){
        return itemDisplay.display.getFacing();
    }

    public DrawerTextDisplay getTextDisplay() {
        return textDisplay;
    }

    public TextFormat getText(){
        return new TextFormat(((TextDisplay) textDisplay.getDisplay()).getText());
    }


}
