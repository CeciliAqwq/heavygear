package com.cecilia.heavygear.mixin.client;

import com.cecilia.heavygear.client.LonginusTridentState;
import com.cecilia.heavygear.item.LonginusSpearItem;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
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
 *
 * <p>Before 26.3 the renderer does not fold texture and glint into one render type: it submits the
 * model once with a plain {@code Identifier} texture and once more with a glint render type, so this
 * port swaps the texture on the identifier call (and, where the glint pass builds its render type
 * from the texture as well, on that call too - hence {@code require = 0}).</p>
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

    /** The solid pass takes the texture identifier directly (8 argument overload, pre-26.3). */
    @ModifyArg(method = "submit",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/OrderedSubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/resources/Identifier;IIILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"),
            index = 3)
    private static Identifier heavygear$modelTexture(Identifier texture) {
        return heavygear$longinus ? HEAVYSPEAR$TEXTURE : texture;
    }

    /**
     * 26.1.2 builds the glint render type from the trident texture; 26.2 uses a plain glint type
     * instead, so this hook is optional and must not fail the mixin when the call is absent.
     */
    @ModifyArg(method = "submit",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/model/object/projectile/TridentModel;renderType(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;"),
            index = 0, require = 0)
    private static Identifier heavygear$glintTexture(Identifier texture) {
        return heavygear$longinus ? HEAVYSPEAR$TEXTURE : texture;
    }
}
