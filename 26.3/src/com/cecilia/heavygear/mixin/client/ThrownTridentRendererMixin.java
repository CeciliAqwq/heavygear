package com.cecilia.heavygear.mixin.client;

import com.cecilia.heavygear.client.LonginusTridentState;
import com.cecilia.heavygear.item.LonginusSpearItem;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
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
    private static final Identifier HEAVYSPEAR$TEXTURE =
            Identifier.fromNamespaceAndPath("heavygear", "textures/entity/longinus_trident.png");

    @Unique
    private static boolean heavygear$longinus;

    @Inject(method = "extractRenderState", at = @At("RETURN"))
    private void heavygear$markLonginus(ThrownTrident entity, ThrownTridentRenderState state, float partial,
                                        CallbackInfo ci) {
        boolean longinus = entity.getPickupItemStackOrigin().getItem() instanceof LonginusSpearItem;
        ((LonginusTridentState) state).heavygear$setLonginus(longinus);
        // the glint flag lives on the render state, so set it straight from the carried stack
        if (longinus && entity.getPickupItemStackOrigin().hasFoil()) {
            state.isFoil = true;
        }
    }

    @Inject(method = "submit", at = @At("HEAD"))
    private void heavygear$beforeRender(ThrownTridentRenderState state, PoseStack pose,
                                        SubmitNodeCollector submit, CameraRenderState camera,
                                        CallbackInfo ci) {
        heavygear$longinus = ((LonginusTridentState) state).heavygear$isLonginus();
    }

    @Inject(method = "submit", at = @At("RETURN"))
    private void heavygear$afterRender(ThrownTridentRenderState state, PoseStack pose,
                                       SubmitNodeCollector submit, CameraRenderState camera,
                                       CallbackInfo ci) {
        heavygear$longinus = false;
    }

    /** The glint/solid pass builds its render type from the trident texture. */
    @ModifyArg(method = "submit",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;entitySolidGlint(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;"),
            index = 0)
    private static Identifier heavygear$renderTypeTexture(Identifier texture) {
        return heavygear$longinus ? HEAVYSPEAR$TEXTURE : texture;
    }

    /** The model pass takes the texture identifier directly. */
    @ModifyArg(method = "submit",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/resources/Identifier;III)V"),
            index = 3)
    private static Identifier heavygear$modelTexture(Identifier texture) {
        return heavygear$longinus ? HEAVYSPEAR$TEXTURE : texture;
    }
}
