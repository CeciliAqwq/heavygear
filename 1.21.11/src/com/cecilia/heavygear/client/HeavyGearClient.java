package com.cecilia.heavygear.client;

import com.cecilia.heavygear.HeavyGearMod;
import com.cecilia.heavygear.network.LonginusThrowPayload;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.world.InteractionHand;

public class HeavyGearClient implements ClientModInitializer {
    public static final String THROW_KEY_ID = "key.heavygear.throw";

    /** Held at the moment right click is released to throw the Spear of Longinus. */
    public static final KeyMapping THROW_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            THROW_KEY_ID,
            InputConstants.Type.KEYSYM,
            // 86 = keyboard code for V (GLFW_KEY_V), written as a literal so that no LWJGL
            // module is needed on the compile classpath (26.3 no longer ships org.lwjgl.glfw).
            86,
            KeyMapping.Category.GAMEPLAY
    ));

    public static InteractionHand throwHand = InteractionHand.MAIN_HAND;

    /** Cached Longinus mode for instant client side decisions (normal = false). */
    public static boolean tridentMode;
    public static int lastModeToggleTick;

    public static boolean isTridentMode() {
        return tridentMode;
    }

    public static boolean isThrowKeyHeld() {
        return THROW_KEY.isDown();
    }

    /** Called from Item#releaseUsing on the client when right click is released. */
    /** Called from the release hook; the caller has already decided that this is a throw. */
    public static void finishThrowCharge(int timeLeft) {
        int charge = 72000 - timeLeft;
        HeavyGearMod.LOGGER.info("longinus throw release charge={}", charge);
        ClientPlayNetworking.send(new LonginusThrowPayload(throwHand, charge));
    }

    @Override
    public void onInitializeClient() {
        // Use the vanilla trident renderer/model for the thrown Longinus spear.
        EntityRendererRegistry.register(HeavyGearMod.THROWN_LONGINUS_TYPE, ThrownTridentRenderer::new);
        @SuppressWarnings({"unchecked", "rawtypes"})
        EntityRendererProvider<com.cecilia.heavygear.entity.HeavyArrow> arrowRenderer = ctx ->
                (net.minecraft.client.renderer.entity.EntityRenderer<
                        com.cecilia.heavygear.entity.HeavyArrow, ?>)
                        (net.minecraft.client.renderer.entity.EntityRenderer) new HeavyArrowRenderer(ctx);
        EntityRendererRegistry.register(HeavyGearMod.HEAVY_ARROW_TYPE, arrowRenderer);
        EntityRendererRegistry.register(HeavyGearMod.COMPRESSED_WIND_CHARGE_TYPE,
                context -> new com.cecilia.heavygear.client.DarkWindChargeRenderer(context,
                        ResourceLocation.fromNamespaceAndPath("heavygear",
                                "textures/entity/projectiles/compressed_wind_charge.png")));
        EntityRendererRegistry.register(HeavyGearMod.DOUBLE_COMPRESSED_WIND_CHARGE_TYPE,
                context -> new com.cecilia.heavygear.client.DarkWindChargeRenderer(context,
                        ResourceLocation.fromNamespaceAndPath("heavygear",
                                "textures/entity/projectiles/double_compressed_wind_charge.png")));

        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(
                com.cecilia.heavygear.network.LonginusModeSyncPayload.TYPE,
                (payload, context) -> context.client().execute(() -> tridentMode = payload.trident()));
    }
}
