package com.drawer.components;

import com.drawer.Main;
import com.drawer.obj.impl.Drawer;

import java.util.Locale;

public class MaxUpgradeAbility extends ComponentAbility {

    private final Components components;

    public MaxUpgradeAbility(final Components components){
        super(components);
        this.components = components;
    }

    @Override
    public void apply(Drawer drawer, Main main) {
        String multiplierKey = components.name().toLowerCase().replace("_","-") + "-multiplier";

        drawer.setMaximum(drawer.getMaximum() +  (100 * main.getConfig().getInt(multiplierKey)));
    }

    @Override
    public void unApply(Drawer drawer, Main main) {
        String multiplierKey = main.getConfig().getInt(components.name().toLowerCase(Locale.ROOT).replace("_","-")) +  "-multiplier";

        drawer.setMaximum(drawer.getMaximum() - main.getConfig().getInt(multiplierKey));

    }
}
