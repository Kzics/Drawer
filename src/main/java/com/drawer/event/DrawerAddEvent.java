package com.drawer.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class DrawerAddEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final PlayerInteractEvent event;

    public DrawerAddEvent(final PlayerInteractEvent event){
        this.event = event;
    }

    public PlayerInteractEvent getEvent() {
        return event;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
