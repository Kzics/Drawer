package com.drawer;

import com.drawer.blocks.DrawerController;
import com.drawer.obj.impl.Drawer;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DrawersManager {
    private final Main main;
    private final HashMap<Location, Drawer> drawers;
    private final HashMap<Location, DrawerController> controllers;
    private boolean frozen;
    public DrawersManager(final Main main){
        this.main = main;
        this.drawers = new HashMap<>();
        this.controllers = new HashMap<>();
        this.frozen = false;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen){
        this.frozen = frozen;
    }

    public void removeDrawers(World world){
        List<Location> toRemove = drawers.keySet().stream()
                .filter(k -> k.getWorld().getName().equals(world.getName())).toList();
        toRemove.forEach(this::removeDrawer);
    }

    public void addDrawer(Drawer drawer){
        this.drawers.put(drawer.getLocation(), drawer);
    }

    public HashMap<Location, Drawer> getDrawers() {
        return drawers;
    }

    public void removeDrawer(Drawer drawer){
        this.getDrawers().remove(drawer.getLocation());
    }
    public void removeDrawer(Location drawer){
        this.getDrawers().remove(drawer);
    }

    public void addController(DrawerController controller){
        this.controllers.put(controller.getLocation(),controller);
    }

    public void removeController(Location location){
        this.controllers.remove(location);
    }

    public HashMap<Location, DrawerController> getControllers() {
        return controllers;
    }

    public Drawer getDrawer(Location location){
        return this.drawers.get(location);
    }


}
