package com.drawer;

import com.drawer.blocks.DrawerController;
import com.drawer.commands.ComponentCommand;
import com.drawer.commands.ItemDisplayCommand;
import com.drawer.components.ComponentAbility;
import com.drawer.components.Components;
import com.drawer.components.HideAbility;
import com.drawer.components.MaxUpgradeAbility;
import com.drawer.craft.component.HideKeyRecipe;
import com.drawer.craft.component.HideUpgradeRecipe;
import com.drawer.craft.component.MaxUpgradeRecipe;
import com.drawer.craft.component.VoidRecipe;
import com.drawer.craft.drawers.*;
import com.drawer.display.DrawerItemDisplay;
import com.drawer.display.DrawerPart;
import com.drawer.display.DrawerTextDisplay;
import com.drawer.display.PartType;
import com.drawer.event.DrawerAddEvent;
import com.drawer.event.DrawerRemoveEvent;
import com.drawer.event.listeners.DrawerListeners;
import com.drawer.loader.DrawersLoader;
import com.drawer.menu.DrawerMenu;
import com.drawer.obj.drawers.*;
import com.drawer.obj.impl.Drawer;
import com.drawer.utils.CustomPair;
import com.drawer.utils.Metrics;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class Main extends JavaPlugin implements Listener {

    private static DrawersManager drawersManager;
    public final NamespacedKey drawerKey = new NamespacedKey(this, "drawer");
    public final NamespacedKey componentKey = new NamespacedKey(this, "component");

    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this,20998);
        metrics.addCustomChart(new Metrics.SingleLineChart("players", new Callable<Integer>() {
            @Override
            public Integer call() {
                return Bukkit.getOnlinePlayers().size();
            }
        }));
        if(!getDataFolder().exists()) getDataFolder().mkdir();
        drawersManager = new DrawersManager(this);

        File file = new File(getDataFolder(), "config.yml");
        try {
            if(!file.exists()) Files.copy(getResource("config.yml"),file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            DrawersLoader.loadDrawers(drawersManager,this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new DrawerListeners(this), this);

        getCommand("drawer").setExecutor(new ItemDisplayCommand(this));
        getCommand("drawer").setTabCompleter(new ItemDisplayCommand(this));

        getCommand("component").setExecutor(new ComponentCommand(this));
        getCommand("component").setTabCompleter(new ComponentCommand(this));

        DrawerScheduler.runTaskTimer(new HopperTask(),0,50L,this);

        // Drawer recipes
        new NormalDrawerRecipe(new NamespacedKey(this,"normalDrawer"),createDrawerItem("normal"),this).register();
        new AdvancedDrawerRecipe(new NamespacedKey(this,"advancedDrawer"),createDrawerItem("advanced"),this).register();
        new SuperAdvancedDrawerRecipe(new NamespacedKey(this,"superAdvancedDrawer"),createDrawerItem("superadvanced"),this).register();
        new ControllerRecipe(new NamespacedKey(this,"controllerDrawer"),createDrawerItem("controller"),this).register();
        new BlankDrawerRecipe(new NamespacedKey(this, "blankDrawer"),createDrawerItem("blank"),this).register();

        // Component recipes

        new HideKeyRecipe(new NamespacedKey(this,"hideKey"),createComponentItem(Components.HIDE_KEY),this).register();
        new HideUpgradeRecipe(new NamespacedKey(this,"hideUpgradeKey"),createComponentItem(Components.HIDE_UPGRADE),this).register();
        new VoidRecipe(new NamespacedKey(this,"voidKey"),createComponentItem(Components.VOID_UPGRADE),this).register();
        new MaxUpgradeRecipe(1,new NamespacedKey(this,"maxupgrade1key"),createComponentItem(Components.MAX_UPGRADE_1),this).register();
        new MaxUpgradeRecipe(2,new NamespacedKey(this,"maxupgrade2key"),createComponentItem(Components.MAX_UPGRADE_2),this).register();
        new MaxUpgradeRecipe(3,new NamespacedKey(this,"maxupgrade3key"),createComponentItem(Components.MAX_UPGRADE_3),this).register();
    }


    private boolean checkHasWorldGuard() {
        return Bukkit.getPluginManager().getPlugin("WorldGuard") != null;
    }

    public static DrawersManager getDrawersManager() {
        return drawersManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getHand().equals(EquipmentSlot.HAND)) return;

        if(event.getClickedBlock() == null) return;

        if (checkHasWorldGuard()) {
            RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(event.getPlayer().getWorld()));

            for (ProtectedRegion region : regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(event.getClickedBlock().getLocation())).getRegions()) {
                if (!region.isOwner(WorldGuardPlugin.inst().wrapPlayer(event.getPlayer())) || !region.isMember(WorldGuardPlugin.inst().wrapPlayer(event.getPlayer())))
                    return;
            }
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.valueOf(getConfig().getString("drawer-block"))) {
            DrawerAddEvent addEvent = new DrawerAddEvent(event);
            getServer().getPluginManager().callEvent(addEvent);

        } else if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.valueOf(getConfig().getString("drawer-block"))) {
            DrawerRemoveEvent removeEvent = new DrawerRemoveEvent(event);
            getServer().getPluginManager().callEvent(removeEvent);
        }

    }


    @Override
    public void onDisable() {
        DrawersLoader.saveDrawers(drawersManager,this);

        for (Drawer drawer : drawersManager.getDrawers().values()){
            drawer.removeDrawer(false);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if(clickedInventory != null && event.getView().getTitle().equals("Drawer Craft")){
            event.setCancelled(true);
            return;
        }
        if (clickedInventory == null || !event.getView().getTitle().equals("Drawer")) return;
        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;
        if(clickedItem.getItemMeta() == null) return;
        if(!event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(componentKey,PersistentDataType.STRING)) return;

        Inventory drawerInventory = event.getView().getTopInventory();

        if(clickedInventory.getType().equals(InventoryType.PLAYER)) {
            for (int slot = 0; slot < drawerInventory.getSize(); slot++) {
                if (drawerInventory.getItem(slot) == null) {
                    Drawer drawer = drawersManager.getDrawer(event.getWhoClicked().getTargetBlock(null, 5).getLocation());
                    Components type = Components.valueOf(event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(componentKey, PersistentDataType.STRING));
                    switch (type) {
                        case MAX_UPGRADE_1, MAX_UPGRADE_2, MAX_UPGRADE_3 ->
                                drawer.addComponent(new MaxUpgradeAbility(type),this);
                        case HIDE_KEY -> drawer.addComponent(new HideAbility(),this);
                    }

                    drawerInventory.setItem(slot, clickedItem);
                    clickedItem.setType(Material.AIR);
                    clickedInventory.setItem(event.getSlot(),null);

                    return;
                }
            }
        }else{
            Drawer drawer = drawersManager.getDrawer(event.getWhoClicked().getTargetBlock(null, 5).getLocation());
            event.getWhoClicked().getInventory().addItem(event.getCurrentItem());
            drawer.removeComponent(drawer.getComponent(event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(componentKey,PersistentDataType.STRING)),this);
            drawerInventory.setItem(event.getSlot(),null);
        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        final Block placed = event.getBlockPlaced();

        if (placed.getType().equals(Material.AIR) || !event.getItemInHand().hasItemMeta() || !event.getItemInHand().getItemMeta().getPersistentDataContainer().has(drawerKey, PersistentDataType.STRING))
            return;
        Directional blockDir = (Directional) event.getBlockPlaced().getBlockData();

        if(event.getBlockPlaced().getType().equals(Material.valueOf(getConfig().getString("controller-block")))){
            DrawerController controller = new DrawerController(placed.getLocation(), BlockFace.valueOf(getConfig().getString("controller-face")));
            drawersManager.getDrawers().put(placed.getLocation(), controller);
            drawersManager.getControllers().put(placed.getLocation(), controller);
            BlockFace face = BlockFace.valueOf(getConfig().getString("controller-face"));
            blockDir.setFacing(face);
            return;
        }
        if (!event.getBlockPlaced().getType().equals(Material.valueOf(getConfig().getString("drawer-block")))) return;


        String[] infos = event.getItemInHand().getItemMeta().getPersistentDataContainer().get(drawerKey, PersistentDataType.STRING).split(",");
        String type = infos[0];

        Drawer drawer;

        switch (type) {
            case "normal":
                drawer = new NormalDrawer(placed.getLocation(), getConfig().getInt("max-amount"), blockDir.getFacing());
                drawersManager.addDrawer(drawer);
                break;
            case "advanced":
                drawer = new AdvancedDrawer(placed.getLocation(), getConfig().getInt("max-amount"), blockDir.getFacing());
                drawersManager.addDrawer(drawer);
                break;
            case "superadvanced":
                drawer = new SuperAdvancedDrawer(placed.getLocation(), getConfig().getInt("max-amount"), blockDir.getFacing());
                drawersManager.addDrawer(drawer);
                break;
            case "ore":
                drawer = new OreDrawer(placed.getLocation(), getConfig().getInt("max-amount"), blockDir.getFacing());
                drawersManager.addDrawer(drawer);
                break;
            case "blank":
                drawer = new BlankDrawer(placed.getLocation(), 0,blockDir.getFacing());
                drawersManager.addDrawer(drawer);

            default:
                return;
        }
        
        PersistentDataContainer dataContainer = event.getItemInHand().getItemMeta().getPersistentDataContainer();
        Map<Integer, CustomPair<DrawerItemDisplay, DrawerTextDisplay>> partPairs = new HashMap<>();

        for (NamespacedKey key : dataContainer.getKeys()) {
            String[] keyParts = key.getKey().split("part");
            if (keyParts.length != 2) continue;

            int index = Integer.parseInt(keyParts[1]);
            String value = dataContainer.get(new NamespacedKey(this, key.getKey()), PersistentDataType.STRING);

            String[] params = value.split(",");
            double verticalOffset = Double.parseDouble(params[0]);
            double horizontalOffset = Double.parseDouble(params[1]);
            double lateralOffset = Double.parseDouble(params[2]);
            double scale = Double.parseDouble(params[3]);
            PartType partType = PartType.ENABLED;
            if(params.length == 6) partType = PartType.valueOf(params[5]);

            if (key.getKey().startsWith("itempart")) {
                DrawerItemDisplay itemDisplay = new DrawerItemDisplay(placed.getLocation(), blockDir.getFacing(), verticalOffset, horizontalOffset, lateralOffset, partType);
                itemDisplay.setItemStack(new ItemStack(Material.valueOf(params[4])));
                itemDisplay.changeScale(scale);
                if (partPairs.get(index) == null) {
                    partPairs.put(index, new CustomPair<>(itemDisplay, null));
                } else {
                    CustomPair<DrawerItemDisplay, DrawerTextDisplay> pair = partPairs.get(index);
                    pair.setKey(itemDisplay);
                }

            } else if (key.getKey().startsWith("textpart")) {
                DrawerTextDisplay textDisplay = new DrawerTextDisplay(placed.getLocation(), blockDir.getFacing(), verticalOffset, horizontalOffset, lateralOffset);
                textDisplay.setText(params[4]);
                textDisplay.changeScale(scale);

                if (partPairs.get(index) == null) {
                    partPairs.put(index, new CustomPair<>(null, textDisplay));
                } else {
                    CustomPair<DrawerItemDisplay, DrawerTextDisplay> pair = partPairs.get(index);
                    pair.setValue(textDisplay);
                }
            }
        }


        for (Map.Entry<Integer, CustomPair<DrawerItemDisplay, DrawerTextDisplay>> entry : partPairs.entrySet()) {
            CustomPair<DrawerItemDisplay, DrawerTextDisplay> pair = entry.getValue();
            if (pair.getKey() != null || pair.getValue() != null) {
                drawer.drawerParts.add(new DrawerPart(pair.getKey(), pair.getValue()));
            }
        }
        for (int i = 0; i < 5; i++) {
            String componentKey = "component" + i;
            if (dataContainer.has(new NamespacedKey(this, componentKey), PersistentDataType.STRING)) {
                String componentName = dataContainer.get(new NamespacedKey(this, componentKey), PersistentDataType.STRING);
                ComponentAbility component = new MaxUpgradeAbility(Components.valueOf(componentName.toUpperCase(Locale.ROOT)));
                drawer.addComponent(component, this);
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Block broken = event.getBlock();

        if(!drawersManager.getDrawers().containsKey(broken.getLocation())) return;
        Drawer drawer = drawersManager.getDrawer(broken.getLocation());
        if (drawer == null) return;

        event.setCancelled(true);
        broken.setType(Material.AIR);

        String type = drawer instanceof AdvancedDrawer ? "advanced" : drawer instanceof NormalDrawer ? "normal" : drawer instanceof SuperAdvancedDrawer ? "superadvanced" : drawer instanceof BlankDrawer ? "blank": "controller";
        Material material = type.equals("controller") ? Material.valueOf(getConfig().getString("controller-block")) : Material.valueOf(getConfig().getString("drawer-block"));

        ItemStack drawerItem = new DrawerItem(material,
                getConfig().getString(String.format("drawers.%s.name", type)), drawer, this, type);
        ItemMeta meta = drawerItem.getItemMeta();
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();

        for (int i = 0; i < drawer.getDrawerParts().size(); i++) {
            DrawerPart drawerPart = drawer.getDrawerParts().get(i);

            String itemKey = "itemPart" + i;
            double itemVerticalOffset = drawerPart.getItemDisplay().getVerticalOffset();
            double itemHorizontalOffset = drawerPart.getItemDisplay().getHorizontalOffset();

            dataContainer.set(new NamespacedKey(this, itemKey), PersistentDataType.STRING,
                    String.format("%s,%s,%s,%s,%s",
                            itemVerticalOffset,
                            itemHorizontalOffset,
                            drawerPart.getItemDisplay().getLateralOffset(),
                            drawerPart.getItemDisplay().getScale(),
                            drawerPart.getItemDisplay().getItemStack().getType()));

            String textKey = "textPart" + i;
            double textVerticalOffset = drawerPart.getTextDisplay().getVerticalOffset();
            double textHorizontalOffset = drawerPart.getTextDisplay().getHorizontalOffset();
            dataContainer.set(new NamespacedKey(this, textKey), PersistentDataType.STRING,
                    String.format("%s,%s,%s,%s,%s",
                            textVerticalOffset,
                            textHorizontalOffset,
                            drawerPart.getTextDisplay().getLateralOffset(),
                            drawerPart.getTextDisplay().getScale(),
                            drawerPart.getText().asInt()));
        }

        for (int i = 0; i < drawer.getComponents().size(); i++) {
            ComponentAbility component = drawer.getComponents().get(i);
            component.getDisplay().remove();

            String componentKey = "component" + i;
            dataContainer.set(new NamespacedKey(this, componentKey), PersistentDataType.STRING, component.getComponent().name());
        }

        meta.getPersistentDataContainer().set(drawerKey, PersistentDataType.STRING, type);

        drawersManager.removeDrawer(drawer);
        drawerItem.setItemMeta(meta);
        player.getInventory().addItem(drawerItem);
        drawer.removeDrawer(true);

        if(drawer instanceof DrawerController){
            drawersManager.getControllers().remove(drawer.getLocation());
        }
    }

    public ItemStack createDrawerItem(String type) {
        Material mat = type.equals("controller") ? Material.valueOf(getConfig().getString("controller-block")) : Material.valueOf(getConfig().getString("drawer-block"));
        UnaryOperator<String> operator = s -> {
            if (s.equals("{item}")) {
                return mat.name().toLowerCase().replace("_"," ");
            } else if (s.equals("{amount}")) {
                return "0";
            } else {
                return s;
            }
        };

        List<String> list = getConfig().getStringList(String.format("drawers.%s.lore", type));
        list.replaceAll(operator);

        DrawerItem drawerItem = new DrawerItem(mat,getConfig().getString(String.format("drawers.%s.name",type)),null,this, type);
        ItemMeta meta = drawerItem.getItemMeta();

        switch (type.toLowerCase()) {
            case "advanced" -> {
                meta.getPersistentDataContainer().set(drawerKey, PersistentDataType.STRING, "advanced");
                addFromType("advanced",meta);
            }
            case "normal" -> {
                meta.getPersistentDataContainer().set(drawerKey, PersistentDataType.STRING, "normal");
                addFromType("normal",meta);
            }
            case "controller" ->
                    meta.getPersistentDataContainer().set(drawerKey, PersistentDataType.STRING, "0,AIR,controller");
            case "superadvanced" ->{
                meta.getPersistentDataContainer().set(drawerKey, PersistentDataType.STRING, "superadvanced");

                addFromType("superadvanced",meta);
            }
            case "ore"->{
                meta.getPersistentDataContainer().set(drawerKey, PersistentDataType.STRING, "ore");
                addPart(meta, "textPart0", "0.5,0.51,0,0.75,0");
                addPart(meta, "itemPart0", "0.8,0.51,0,0.75,AIR");

                addPart(meta, "textPart1", "0,0.51,-0.25,0.75,0");
                addPart(meta, "itemPart1", "0.3,0.51,-0.25,0.75,AIR");

                addPart(meta, "textPart2", "0,0.51,0.25,0.75,0");
                addPart(meta, "itemPart2", "0.3,0.51,0.25,0.75,AIR");
            }
            case "blank" ->{
                meta.getPersistentDataContainer().set(drawerKey, PersistentDataType.STRING, "blank");
            }
            default -> {
            }
        }

        drawerItem.setItemMeta(meta);
        return drawerItem;
    }
    public ItemStack createComponentItem(Components component) {
        ItemStack componentItem = new ItemStack(Material.TRIPWIRE_HOOK);
        String componentName = component.name();

        ItemMeta meta = componentItem.getItemMeta();
        String displayName = getConfig().getString("components." + componentName.toLowerCase().replace("_","-") + ".name");


        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        List<String> componentDescription = getConfig().getStringList("components." + componentName.toLowerCase() + ".lore");

        List<String> coloredLores = new ArrayList<>();

        for (String lore : componentDescription) {
            coloredLores.add(ChatColor.translateAlternateColorCodes('&', lore));
        }

        meta.setLore(coloredLores);
        meta.getPersistentDataContainer().set(componentKey, PersistentDataType.STRING, componentName);
        componentItem.setItemMeta(meta);

        return componentItem;
    }

    private void addPart(ItemMeta meta, String partKey, String partValue) {
        meta.getPersistentDataContainer().set(new NamespacedKey(this, partKey), PersistentDataType.STRING, partValue);
    }
    private void addFromType(String type,ItemMeta meta){
        List<Map<?, ?>> textParts = getConfig().getMapList(String.format("drawers.%s.textParts",type));
        List<Map<?, ?>> itemParts = getConfig().getMapList(String.format("drawers.%s.itemParts",type));

        for (Map<?, ?> part : textParts) {
            Map<?, ?> values = (Map<?, ?>) part.get("values");
            String partVal = String.format("%s,%s,%s,%s,0",values.get("vertical"),values.get("horizontal")
                    ,values.get("lateral"),values.get("scale"));
            addPart(meta, (String) part.get("name"),partVal);
        }

        for (Map<?, ?> part : itemParts) {
            String partType = (String) part.get("type");
            Map<?, ?> values = (Map<?, ?>) part.get("values");
            String partVal = String.format("%s,%s,%s,%s,AIR,%s",values.get("vertical"),values.get("horizontal")
                    ,values.get("lateral"),values.get("scale"), partType);
            addPart(meta, (String) part.get("name"),partVal);
        }
    }

}
