package com.cecilia.heavygear.mixin;

import com.cecilia.heavygear.item.CompressedWindChargeMarker;

import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/** Adds the impact scale to wind charges. */
@Mixin(AbstractWindCharge.class)
public abstract class CompressedWindChargeMarkerMixin implements CompressedWindChargeMarker {
    @Unique
    private double heavygear$impactScale = 1.0D;

    @Override
    public void heavygear$setImpactScale(double scale) {
        this.heavygear$impactScale = scale;
    }

    @Override
    public double heavygear$impactScale() {
        return this.heavygear$impactScale;
    }
}