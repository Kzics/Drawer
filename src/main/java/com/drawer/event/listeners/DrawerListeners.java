package com.drawer.event.listeners;

import com.drawer.Main;
import com.drawer.event.DrawerPlaceEvent;
import org.bukkit.event.Listener;

public class DrawerListeners implements Listener {


    private final Main main;
    public DrawerListeners(final Main main){
        this.main = main;
    }

    public void onDrawerPlace(DrawerPlaceEvent event){

    }

}
