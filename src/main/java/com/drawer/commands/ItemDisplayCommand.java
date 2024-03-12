package com.drawer.commands;


import com.drawer.DrawerItem;
import com.drawer.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ItemDisplayCommand implements CommandExecutor, TabCompleter {

    private final Main main;

    public ItemDisplayCommand(final Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if (strings.length == 3 && "give".equalsIgnoreCase(strings[0])) {
            if (!player.hasPermission("drawer.give") && !player.isOp()) return false;
            Player target = Bukkit.getPlayer(strings[1]);
            if (target == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlayer doesn't exist!"));
                return false;
            }
            String drawerType = strings[2];
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aReceived a drawer!"));

            ItemStack itemStack = main.createDrawerItem(drawerType);
            target.getInventory().addItem(itemStack);
            return true;
        }

        if(strings.length == 2){
            String action = strings[0];

            if(action.equalsIgnoreCase("bulk")){
                String worldStr = strings[1];
                World world = Bukkit.getWorld(worldStr);
                if(world == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cWorld doesn't exist!"));
                    return false;
                }
                if(!player.hasPermission("drawer.manage") && !player.isOp()) return false;
                Main.getDrawersManager().removeDrawers(world);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&aSucessfully removed drawers from world"));

            }else if(action.equals("freeze")){
                String worldStr = strings[1];
                World world = Bukkit.getWorld(worldStr);
                if(world == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cWorld doesn't exist!"));
                    return false;
                }
                if(!Main.getDrawersManager().isFrozen()) {
                    if(!player.hasPermission("drawer.manage") && !player.isOp()) return false;
                    Main.getDrawersManager().setFrozen(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&aSucessfully frozen drawers in world"));
                }else{
                    Main.getDrawersManager().setFrozen(false);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&aSucessfully unfrozen drawers in world"));
                }
            }else if(action.equals("craft")){
                String type = strings[1];
                if(!player.hasPermission("drawer.display.craft") && !player.isOp()) return false;

                if (!main.getConfig().contains("drawers." + type + ".craft.ingredients")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cCe type de tiroir n'existe pas."));
                    return true;
                }
                player.openInventory(createCraftInventory(type));
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> completions = new ArrayList<>();

        if (strings.length == 1) {
            String input = strings[0].toLowerCase();

            if ("give".startsWith(input) || "bulk".startsWith(input) || "freeze".startsWith(input) || "craft".startsWith(input)) {
                completions.add("give");
                completions.add("bulk");
                completions.add("freeze");
                completions.add("craft");
            }

        } else if (strings.length == 2) {
            if ("give".equalsIgnoreCase(strings[0])) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    completions.add(onlinePlayer.getName());
                }
            } else if ("craft".equalsIgnoreCase(strings[0])) {
                completions.addAll(getAllTypes());
            }
        } else if (strings.length == 3) {
            if ("give".equalsIgnoreCase(strings[0])) {
                completions.addAll(getAllTypes());
            }
        }

        return completions;
    }
    private Inventory createCraftInventory(String drawerType) {
        Inventory inventory = main.getServer().createInventory(null, InventoryType.WORKBENCH,"Drawer Craft");
        ItemStack result = main.createDrawerItem(drawerType);
        String[] shape = main.getConfig().getStringList("drawers." + drawerType + ".craft.shape").toArray(new String[0]);
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length(); col++) {
                char c = shape[row].charAt(col);
                if (c != ' ') {
                    Material material = Material.valueOf(main.getConfig().getString("drawers." + drawerType + ".craft.ingredients." + c));
                    ItemStack itemStack = new ItemStack(material);
                    inventory.setItem(1 + row * 3 + col, itemStack);
                }
            }
        }

        inventory.setItem(0,result);

        return inventory;
    }

    private List<String> getAllTypes() {
        List<String> types = new ArrayList<>();

        ConfigurationSection drawersConfig = main.getConfig().getConfigurationSection("drawers");

        if (drawersConfig != null) {
            types.addAll(drawersConfig.getKeys(false));
        }

        return types;
    }
}
