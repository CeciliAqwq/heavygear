package com.cecilia.heavygear.mixin.client;

import com.cecilia.heavygear.client.LonginusTridentState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/** Adds a "this trident render state belongs to a Longinus spear" flag. */
@Mixin(targets = "net.minecraft.class_10073")
public class TridentRenderStateMixin implements LonginusTridentState {
    @Unique
    private boolean heavygear$longinus;

    @Override
    public boolean heavygear$isLonginus() {
        return heavygear$longinus;
    }

    @Override
    public void heavygear$setLonginus(boolean value) {
        heavygear$longinus = value;
    }
}
