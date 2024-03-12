package com.drawer.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DrawerPlaceEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Block placed;
    private final Player player;

    public DrawerPlaceEvent(Block placed, Player player) {
        this.placed = placed;
        this.player = player;
    }

    public Block getPlaced() {
        return placed;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
