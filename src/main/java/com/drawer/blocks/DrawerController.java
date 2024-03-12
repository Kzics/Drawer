package com.drawer.blocks;

import com.drawer.Main;
import com.drawer.obj.drawers.NormalDrawer;
import com.drawer.obj.impl.Drawer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DrawerController extends Drawer{
    private final Location location;
    private final Set<Drawer> visitedDrawers;

    public DrawerController(final Location location,BlockFace face) {
        super(location,0,face);
        this.location = location;
        this.visitedDrawers = new HashSet<>();
        initAffectedDrawers(location.getBlock().getRelative(face).getLocation());
    }

    public Location getLocation() {
        return location;
    }

    public Set<Drawer> getAffectedDrawers() {
        return visitedDrawers;
    }

    private void initAffectedDrawers(Location currentLocation) {
        Drawer currentDrawer = Main.getDrawersManager().getDrawer(currentLocation);
        if (currentDrawer == null || visitedDrawers.contains(currentDrawer)) {
            return;
        }

        visitedDrawers.add(currentDrawer);

        List<Location> adjacentLocations = getAdjacentLocations(currentLocation);

        for (Location adjacentLocation : adjacentLocations) {
            initAffectedDrawers(adjacentLocation);
        }
    }

    public boolean canTransfer(){
        return location.getBlock().getRelative(BlockFace.WEST).getType().equals(Material.HOPPER);
    }

    private List<Location> getAdjacentLocations(Location location) {
        List<Location> adjacentLocations = new ArrayList<>();
        BlockFace[] faces = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST,BlockFace.UP, BlockFace.DOWN};
        for (BlockFace face : faces) {
            Location adjacentLocation = location.getBlock().getRelative(face).getLocation();
            adjacentLocations.add(adjacentLocation);
        }
        return adjacentLocations;
    }

    private void placeGlowingArmorStand(Location location) {
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setArms(false);
        armorStand.setBasePlate(false);
        armorStand.setGravity(false);
        armorStand.setVisible(true);
        armorStand.setHelmet(new ItemStack(Material.GLOWSTONE_DUST));
        armorStand.setGlowing(true);
    }
}
