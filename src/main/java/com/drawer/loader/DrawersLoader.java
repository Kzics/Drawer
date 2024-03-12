package com.drawer.loader;


import com.drawer.DrawersManager;
import com.drawer.Main;
import com.drawer.blocks.DrawerController;
import com.drawer.components.ComponentAbility;
import com.drawer.components.Components;
import com.drawer.components.MaxUpgradeAbility;
import com.drawer.display.ComponentDisplay;
import com.drawer.display.DrawerItemDisplay;
import com.drawer.display.DrawerPart;
import com.drawer.display.DrawerTextDisplay;
import com.drawer.obj.drawers.AdvancedDrawer;
import com.drawer.obj.drawers.NormalDrawer;
import com.drawer.obj.drawers.SuperAdvancedDrawer;
import com.drawer.obj.impl.Drawer;
import com.drawer.utils.LocationSerializer;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrawersLoader {

    public static void saveDrawers(final DrawersManager drawersManager, Main main) {
        final File file = new File(main.getDataFolder(), "drawers.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (JsonWriter writer = new JsonWriter(new FileWriter(file))) {
            writer.beginObject();
            writer.name("drawers");
            writer.beginArray();
            for (Drawer drawer : drawersManager.getDrawers().values()) {
                writer.beginObject();
                writer.name("type").value(drawer.getClass().getSimpleName());
                writer.name("location").value(LocationSerializer.serializeLocation(drawer.getLocation()));
                writer.name("maximum").value(drawer.getMaximum());
                writer.name("face").value(drawer.getFace().name());

                writer.name("drawerParts").beginArray();
                for (DrawerPart drawerPart : drawer.getDrawerParts()) {
                    writer.beginObject();
                    serializeTextDisplay(writer, drawerPart.getTextDisplay());
                    serializeItemDisplay(writer, drawerPart.getItemDisplay());
                    writer.endObject();
                }
                writer.endArray();

                writer.name("components").beginArray();
                for (ComponentAbility componentAbility : drawer.getComponents()) {
                    writer.beginObject();
                    writer.name("componentType").value(componentAbility.getComponent().name());
                    writer.endObject();
                }
                writer.endArray();

                writer.endObject();
            }
            writer.endArray();
            writer.endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void serializeTextDisplay(JsonWriter writer, DrawerTextDisplay textDisplay) throws IOException {
        writer.name("textDisplay").beginObject();
        writer.name("location").value(LocationSerializer.serializeLocation(textDisplay.getLocation()));
        writer.name("face").value(textDisplay.getFace().name());
        writer.name("verticalOffset").value(textDisplay.getVerticalOffset());
        writer.name("horizontalOffset").value(textDisplay.getHorizontalOffset());
        writer.name("amount").value(textDisplay.getText().asInt());
        writer.name("lateralOffset").value(textDisplay.getLateralOffset());
        writer.endObject();
    }

    private static void serializeItemDisplay(JsonWriter writer, DrawerItemDisplay itemDisplay) throws IOException {
        writer.name("itemDisplay").beginObject();
        writer.name("location").value(LocationSerializer.serializeLocation(itemDisplay.getLocation()));
        writer.name("face").value(itemDisplay.getFace().name());
        writer.name("verticalOffset").value(itemDisplay.getVerticalOffset());
        writer.name("item").value(itemDisplay.getItemStack().getType().name());
        writer.name("horizontalOffset").value(itemDisplay.getHorizontalOffset());
        writer.name("lateralOffset").value(itemDisplay.getLateralOffset());
        writer.endObject();
    }

    public static void loadDrawers(final DrawersManager drawersManager, Main main) throws IOException {
        HashMap<Location, Drawer> drawers = new HashMap<>();
        final File file = new File(main.getDataFolder(), "drawers.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
                try (JsonWriter writer = new JsonWriter(new FileWriter(file))) {
                    writer.beginObject();
                    writer.name("drawers").beginArray();
                    writer.endArray();
                    writer.endObject();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (JsonReader reader = new JsonReader(new FileReader(file))) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("drawers")) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        reader.beginObject();
                        String drawerType = null;
                        Location location = null;
                        int maximum = 0;
                        BlockFace face = null;
                        List<ComponentAbility> components = new ArrayList<>();
                        List<DrawerPart> drawerParts = new ArrayList<>();
                        while (reader.hasNext()) {
                            String key = reader.nextName();
                            switch (key) {
                                case "type":
                                    drawerType = reader.nextString();
                                    break;
                                case "location":
                                    location = LocationSerializer.deserializeLocation(reader.nextString());
                                    break;
                                case "maximum":
                                    maximum = reader.nextInt();
                                    break;
                                case "face":
                                    face = BlockFace.valueOf(reader.nextString());
                                    break;
                                case "drawerParts":
                                    reader.beginArray();
                                    while (reader.hasNext()) {
                                        reader.beginObject();
                                        DrawerTextDisplay textDisplay = null;
                                        DrawerItemDisplay itemDisplay = null;

                                        while (reader.hasNext()) {
                                            String drawerPartKey = reader.nextName();
                                            switch (drawerPartKey) {
                                                case "textDisplay":
                                                    textDisplay = deserializeTextDisplay(reader);
                                                    break;
                                                case "itemDisplay":
                                                    itemDisplay = deserializeItemDisplay(reader);
                                                    break;
                                            }
                                        }

                                        if (textDisplay != null && itemDisplay != null) {
                                            DrawerPart drawerPart = new DrawerPart(itemDisplay, textDisplay);
                                            drawerParts.add(drawerPart);
                                        }

                                        reader.endObject();
                                    }
                                    reader.endArray();
                                    break;
                                case "components":
                                    components = deserializeComponents(reader,location, face);
                                    break;
                            }
                        }
                        reader.endObject();
                        if (drawerType != null && location != null && face != null) {
                            Drawer drawer;
                            switch (drawerType) {
                                case "NormalDrawer":
                                    drawer = new NormalDrawer(location, maximum, face);
                                    break;
                                case "AdvancedDrawer":
                                    drawer = new AdvancedDrawer(location, maximum, face);
                                    break;
                                case "SuperAdvancedDrawer":
                                    drawer = new SuperAdvancedDrawer(location, maximum, face);
                                    break;
                                case "controller":
                                    drawer = new DrawerController(location, face);
                                    break;
                                default:
                                    drawer = new NormalDrawer(location, maximum, face);
                                    break;
                            }
                            drawer.getDrawerParts().addAll(drawerParts);
                            drawer.getComponents().addAll(components);
                            if(drawer instanceof DrawerController){
                                drawersManager.getControllers().put(location, (DrawerController) drawer);
                            }
                            drawers.put(location, drawer);
                        }
                    }
                    reader.endArray();
                }
            }
            reader.endObject();
        }
        drawersManager.getDrawers().putAll(drawers);
    }
    private static DrawerTextDisplay deserializeTextDisplay(JsonReader reader) throws IOException {
        Location location = null;
        BlockFace face = null;
        double verticalOffset = 0;
        double horizontalOffset = 0;
        double lateralOffset = 0;
        int amount = 10;

        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            switch (key) {
                case "location":
                    location = LocationSerializer.deserializeLocation(reader.nextString());
                    break;
                case "face":
                    face = BlockFace.valueOf(reader.nextString());
                    break;
                case "verticalOffset":
                    verticalOffset = reader.nextDouble();
                    break;
                case "horizontalOffset":
                    horizontalOffset = reader.nextDouble();
                    break;
                case "lateralOffset":
                    lateralOffset = reader.nextDouble();
                    break;
                case "amount":
                    amount = reader.nextInt();
                    break;
            }
        }
        reader.endObject();

        DrawerTextDisplay drawerTextDisplay = new DrawerTextDisplay(location, face, verticalOffset, horizontalOffset, lateralOffset);
        drawerTextDisplay.setText(String.valueOf(amount));
        return drawerTextDisplay;
    }

    private static DrawerItemDisplay deserializeItemDisplay(JsonReader reader) throws IOException {
        Location location = null;
        BlockFace face = null;
        double verticalOffset = 0;
        double horizontalOffset = 0;
        double lateralOffset = 0;
        ItemStack itemStack = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            switch (key) {
                case "location":
                    location = LocationSerializer.deserializeLocation(reader.nextString());
                    break;
                case "face":
                    face = BlockFace.valueOf(reader.nextString());
                    break;
                case "verticalOffset":
                    verticalOffset = reader.nextDouble();
                    break;
                case "horizontalOffset":
                    horizontalOffset = reader.nextDouble();
                    break;
                case "lateralOffset":
                    lateralOffset = reader.nextDouble();
                    break;
                case "item":
                    itemStack = new ItemStack(Material.valueOf(reader.nextString()));
                    break;
            }
        }
        reader.endObject();

        DrawerItemDisplay itemDisplay = new DrawerItemDisplay(location, face, verticalOffset, horizontalOffset, lateralOffset);
        itemDisplay.setItemStack(itemStack);
        return itemDisplay;
    }

    private static ComponentAbility deserializeComponentAbility(JsonReader reader)  {
        try {
            reader.beginObject();
            String componentType = null;

            while (reader.hasNext()) {
                String key = reader.nextName();
                switch (key) {
                    case "componentType":
                        componentType = reader.nextString();
                        break;
                    // Add more properties based on your ComponentAbility class
                }
            }
            reader.endObject();

            return new MaxUpgradeAbility(Components.valueOf(componentType)); // Adjust this based on your enum structure

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<ComponentAbility> deserializeComponents(JsonReader reader, Location location, BlockFace face) throws IOException {
        List<ComponentAbility> components = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            double lateralOffset = -0.25 + components.size() * 0.125;
            ComponentAbility componentAbility = deserializeComponentAbility(reader);
            componentAbility.setDisplay(new ComponentDisplay(componentAbility,location,face,0.9,0.52,lateralOffset));
            components.add(componentAbility);
        }
        reader.endArray();
        return components;
    }
}