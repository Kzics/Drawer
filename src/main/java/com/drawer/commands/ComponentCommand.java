package com.drawer.commands;

import com.drawer.Main;
import com.drawer.components.ComponentAbility;
import com.drawer.components.Components;
import com.drawer.components.MaxUpgradeAbility;
import com.drawer.components.VoidAbility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ComponentCommand implements CommandExecutor, TabCompleter {

    private final Main main;
    public ComponentCommand(final Main main){
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(strings.length != 1){
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cCorrect usage: /component (component"));
        }
        String componentStr = strings[0];

        if(!(commandSender.hasPermission("drawer.component.give"))) return false;

        ComponentAbility components = switch (componentStr) {
            case "maxupgrade1" -> new MaxUpgradeAbility(Components.MAX_UPGRADE_1);
            case "maxupgrade2" -> new MaxUpgradeAbility( Components.MAX_UPGRADE_2);
            case "maxupgrade3" -> new MaxUpgradeAbility( Components.MAX_UPGRADE_3);
            case "hidekey" -> new MaxUpgradeAbility( Components.HIDE_KEY);
            case "hideupgrade" -> new MaxUpgradeAbility(Components.HIDE_UPGRADE);
            case "void" -> new VoidAbility(Components.VOID_UPGRADE);
            default -> null;
        };

        ItemStack componentItem = main.createComponentItem(components.getComponent());

        ((Player)commandSender).getInventory().addItem(componentItem);
        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> componentList = new ArrayList<>();
            componentList.add("hidekey");
            componentList.add("hideupgrade");
            componentList.add("maxupgrade1");
            componentList.add("maxupgrade2");
            componentList.add("maxupgrade3");
            componentList.add("void");
            List<String> completions = new ArrayList<>();
            String input = args[0].toLowerCase(Locale.ROOT);

            for (String component : componentList) {
                String componentName = component.toLowerCase(Locale.ROOT);

                if (componentName.startsWith(input)) {
                    completions.add(componentName);
                }
            }

            return completions;
        }

        return null;
    }
}
