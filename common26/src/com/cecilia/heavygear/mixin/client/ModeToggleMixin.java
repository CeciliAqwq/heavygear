package com.cecilia.heavygear.mixin.client;

import com.cecilia.heavygear.client.HeavyGearClient;
import com.cecilia.heavygear.item.LonginusMode;
import com.cecilia.heavygear.item.LonginusSpearItem;
import com.cecilia.heavygear.network.LonginusModePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Pressing the configured key toggles the held Longinus between normal and trident mode. */
@Mixin(LocalPlayer.class)
public class ModeToggleMixin {
    @Inject(method = "tick", at = @At("RETURN"))
    private void heavygear$toggleMode(CallbackInfo ci) {
        if (!HeavyGearClient.THROW_KEY.consumeClick()) {
            return;
        }
        LocalPlayer player = (LocalPlayer) (Object) this;
        ItemStack held = player.getMainHandItem();
        if (!(held.getItem() instanceof LonginusSpearItem)) {
            return;
        }
        // Server is authoritative: it flips the item's component and broadcasts the new state back,
        // which also refreshes the client mirror. Flipping a local cache here desynced it whenever
        // the payload was not applied (e.g. dying while the toggle was in flight).
        ClientPlayNetworking.send(new LonginusModePayload(InteractionHand.MAIN_HAND));
        HeavyGearClient.tridentMode = !LonginusMode.isTrident(held);
        HeavyGearClient.lastModeToggleTick = player.tickCount;
        com.cecilia.heavygear.HeavyGearMod.LOGGER.info("longinus mode toggle key pressed (was trident={})",
                LonginusMode.isTrident(held));
    }
}
