package com.cecilia.heavygear.mixin;

import com.cecilia.heavygear.combat.ArmorSmash;
import com.cecilia.heavygear.dive.DiveTracker;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Feeds the per-tick dive tracker without depending on the Fabric API server tick event. */
@Mixin(Entity.class)
public abstract class PlayerTickDiveMixin {
    @Inject(method = "method_5773", at = @At("HEAD"))
    private void heavygear$trackDive(CallbackInfo ci) {
        if (((Object) this) instanceof ServerPlayer player) {
            DiveTracker.tick(player);
            ArmorSmash.tick(player);
        }
    }
}
