package com.drawer;

import org.bukkit.Bukkit;

public class DrawerScheduler {

    private static final boolean isFolia = Bukkit.getVersion().contains("Folia");


    public static void runTaskTimer(Runnable runnable,long delay, long interval, Main instance){
        if(isFolia){
            Bukkit.getGlobalRegionScheduler().runAtFixedRate(instance,t-> runnable.run(),delay < 1 ? 1 : delay,interval);
        }else{
            Bukkit.getScheduler().runTaskTimer(instance,runnable,delay,interval);
        }
    }
}
