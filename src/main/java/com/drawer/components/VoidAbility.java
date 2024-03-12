package com.drawer.components;

import com.drawer.Main;
import com.drawer.obj.impl.Drawer;

public class VoidAbility extends ComponentAbility{

    public VoidAbility(Components component) {
        super(component);
    }

    @Override
    public void apply(Drawer drawer, Main main) {
        drawer.enableVoid();
    }

    @Override
    public void unApply(Drawer drawer, Main main) {
        drawer.disableVoid();
    }
}
