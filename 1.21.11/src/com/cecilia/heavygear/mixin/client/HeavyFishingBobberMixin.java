package com.cecilia.heavygear.mixin.client;

import com.cecilia.heavygear.item.HeavyGear;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.resources.ResourceLocation;
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
 * owner is remembered while the render state is extracted, and the render type passed to the buffer
 * call inside {@code render} is swapped when that owner holds a heavy rod.</p>
 */
@Mixin(FishingHookRenderer.class)
public abstract class HeavyFishingBobberMixin {
    @Unique
    private static final ResourceLocation VANILLA_TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/entity/fishing_hook.png");
    @Unique
    private static final ResourceLocation HEAVY_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("heavygear", "textures/entity/fishing_hook.png");

    /** Freshly set for every rendered hook, before its render call. */
    @Unique
    private static boolean heavygear$heavyRod;

    @Inject(method = "method_62443(Lnet/minecraft/class_1536;Lnet/minecraft/class_10026;F)V",
            at = @At("TAIL"))
    private void heavygear$rememberOwner(FishingHook hook, net.minecraft.class_10026 state,
                                         float partialTick, CallbackInfo ci) {
        Player owner = hook.getPlayerOwner();
        heavygear$heavyRod = owner != null
                && (owner.getMainHandItem().is(HeavyGear.HEAVY_FISHING_ROD)
                || owner.getOffhandItem().is(HeavyGear.HEAVY_FISHING_ROD));
    }

    /**
     * The bobber is the first buffer submission of the render pass; the fishing line uses a
     * different render type and must not be touched (its geometry has no UV/lightmap elements).
     */
    @ModifyArg(method = "method_3974(Lnet/minecraft/class_10026;Lnet/minecraft/class_4587;"
            + "Lnet/minecraft/class_11659;Lnet/minecraft/class_12075;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/class_11659;method_73483(Lnet/minecraft/class_4587;"
                            + "Lnet/minecraft/class_1921;Lnet/minecraft/class_11659$class_11660;)V",
                    ordinal = 0),
            index = 1,
            remap = false)
    private static RenderType heavygear$bobberType(RenderType original) {
        return net.minecraft.class_12249.method_75990(
                heavygear$heavyRod ? HEAVY_TEXTURE : VANILLA_TEXTURE);
    }
}
