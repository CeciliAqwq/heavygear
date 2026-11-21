package com.cecilia.heavygear.mixin.client;

import com.cecilia.heavygear.item.HeavyGear;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.client.renderer.entity.state.FishingHookRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Draws the bobber of the heavy fishing rod with its own texture (red swapped for breeze rod blues).
 *
 * <p>The vanilla renderer keeps a single static {@code RENDER_TYPE} built from
 * {@code textures/entity/fishing_hook.png}, so the texture is chosen per rendered hook instead: the
 * owner is remembered while the render state is extracted, and the render type passed to the custom
 * geometry call inside {@code submit} is swapped when that owner holds a heavy rod.</p>
 */
@Mixin(FishingHookRenderer.class)
public abstract class HeavyFishingBobberMixin {
    @Unique
    private static final Identifier HEAVY_TEXTURE =
            Identifier.fromNamespaceAndPath("heavygear", "textures/entity/fishing_hook.png");
    @Unique
    private static final Identifier VANILLA_TEXTURE =
            Identifier.withDefaultNamespace("textures/entity/fishing_hook.png");

    /** Freshly set for every rendered hook, before its submit call. */
    @Unique
    private static boolean heavygear$heavyRod;

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void heavygear$rememberOwner(FishingHook hook, FishingHookRenderState state,
                                         float partialTick, CallbackInfo ci) {
        Player owner = hook.getPlayerOwner();
        heavygear$heavyRod = owner != null
                && (owner.getMainHandItem().is(HeavyGear.HEAVY_FISHING_ROD)
                || owner.getOffhandItem().is(HeavyGear.HEAVY_FISHING_ROD));
    }

    /**
     * The bobber is the first custom geometry submission of the render pass; the fishing line uses
     * {@code RenderTypes.lines()} and must not be touched.
     */
    @ModifyArg(method = "submit",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitCustomGeometry(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;Lnet/minecraft/client/renderer/SubmitNodeCollector$CustomGeometryRenderer;)V",
                    ordinal = 0),
            index = 1)
    private static RenderType heavygear$bobberType(RenderType original) {
        return RenderTypes.entityCutout(heavygear$heavyRod ? HEAVY_TEXTURE : VANILLA_TEXTURE);
    }
}
