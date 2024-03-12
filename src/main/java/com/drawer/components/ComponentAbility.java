package com.drawer.components;

import com.drawer.Main;
import com.drawer.display.ComponentDisplay;
import com.drawer.obj.impl.Drawer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import java.awt.*;
import java.util.Locale;

public abstract class ComponentAbility {

    private final Components component;
    private ComponentDisplay display;
    ComponentAbility(final Components component){
        this.component = component;
    }


    public void setDisplay(ComponentDisplay display){
        this.display = display;
    }

    public ComponentDisplay getDisplay() {
        return display;
    }

    public Components getComponent() {
        return component;
    }

    public abstract void apply(Drawer drawer, Main main);
    public abstract void unApply(Drawer drawer, Main main);


    public String getName(){
        String formattedText = this.component.name().toLowerCase().replace("_"," ").toLowerCase(Locale.ROOT);
        TextComponent textComponent = new TextComponent(formattedText);
        textComponent.setColor(ChatColor.of(Color.CYAN));

        return formattedText;
    }
}
