package com.cecilia.heavygear.mixin;

import com.cecilia.heavygear.item.CompressedWindChargeImpulse;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Awards the "fly higher" advancement when a double compressed wind charge launches a player 50+ blocks. */
@Mixin(ServerPlayer.class)
public abstract class PlayerFlyHigherMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void heavygear$checkFlyHigher(CallbackInfo ci) {
        CompressedWindChargeImpulse.checkFlyHigher((ServerPlayer) (Object) this);
    }
}
