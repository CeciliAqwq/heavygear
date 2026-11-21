package com.cecilia.heavygear.entity;

import com.cecilia.heavygear.item.CompressedWindChargeMarker;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.level.Level;

/** A wind charge with a compressed core: darkened model, double impact. */
public class CompressedWindCharge extends WindCharge {
    public CompressedWindCharge(EntityType<? extends WindCharge> type, Level level) {
        this(type, level, 2.0D);
    }

    protected CompressedWindCharge(EntityType<? extends WindCharge> type, Level level, double impactScale) {
        super(type, level);
        ((CompressedWindChargeMarker) (Object) this).heavygear$setImpactScale(impactScale);
    }
}