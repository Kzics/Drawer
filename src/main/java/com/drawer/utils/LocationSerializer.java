package com.drawer.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.stream.Collectors;

public class LocationSerializer {


    public static List<String> serializeLocations(final List<Location> locationList){
        return locationList.stream()
                .map(loc -> loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ())
                .collect(Collectors.toList());

    }

    public static String serializeLocation(final Location loc){
        return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ();
    }

    public static Location deserializeLocation(final String loc){
        return getLocation(loc);
    }

    public static List<Location> deserializeLocations(final List<String> locationList){
        return  locationList
                .stream()
                .map(LocationSerializer::getLocation)
                .collect(Collectors.toList());
    }

    private static Location getLocation(String loc) {
        final String[] splitList = loc.split(";");
        final World world = Bukkit.getWorld(splitList[0]);
        final double x = Double.parseDouble(splitList[1]);
        final double y = Double.parseDouble(splitList[2]);
        final double z = Double.parseDouble(splitList[3]);

        return new Location(world,x,y,z);
    }
}