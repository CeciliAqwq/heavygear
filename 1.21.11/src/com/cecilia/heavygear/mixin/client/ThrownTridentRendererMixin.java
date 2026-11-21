package com.cecilia.heavygear.mixin.client;

import com.cecilia.heavygear.client.LonginusTridentState;
import com.cecilia.heavygear.item.LonginusSpearItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.ThrownTrident;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Render the thrown Spear of Longinus with the vanilla trident model but its own
 * texture (heavy-core head, breeze-rod handle).
 */
@Mixin(ThrownTridentRenderer.class)
public class ThrownTridentRendererMixin {
    @Unique
    private static final ResourceLocation HEAVYSPEAR$TEXTURE =
            ResourceLocation.fromNamespaceAndPath("heavygear", "textures/entity/longinus_trident.png");

    @Unique
    private static boolean heavygear$longinus;

    @Inject(method = "method_62552", at = @At("RETURN"))
    private void heavygear$markLonginus(ThrownTrident entity, net.minecraft.class_10073 state, float partial,
                                         CallbackInfo ci) {
        boolean longinus = entity.getPickupItemStackOrigin().getItem() instanceof LonginusSpearItem;
        ((LonginusTridentState) state).heavygear$setLonginus(longinus);
        // belt and braces: the glint flag lives on the render state, so set it straight from the
        // carried stack instead of relying on the entity's synced ID_FOIL flag
        if (longinus && entity.getPickupItemStackOrigin().hasFoil()) {
            state.field_53594 = true;
        }
    }

    @Inject(method = "method_4133", at = @At("HEAD"))
    private void heavygear$beforeRender(net.minecraft.class_10073 state, PoseStack pose,
                                         net.minecraft.class_11659 buffers, net.minecraft.class_12075 camera,
                                         CallbackInfo ci) {
        heavygear$longinus = ((LonginusTridentState) state).heavygear$isLonginus();
    }

    @Inject(method = "method_4133", at = @At("RETURN"))
    private void heavygear$afterRender(net.minecraft.class_10073 state, PoseStack pose,
                                        net.minecraft.class_11659 buffers, net.minecraft.class_12075 camera,
                                        CallbackInfo ci) {
        heavygear$longinus = false;
    }

    @ModifyArg(method = "method_4133",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/class_613;method_23500(Lnet/minecraft/class_2960;)Lnet/minecraft/class_1921;"),
            index = 0)
    private static ResourceLocation heavygear$texture(ResourceLocation texture) {
        return heavygear$longinus ? HEAVYSPEAR$TEXTURE : texture;
    }
}
