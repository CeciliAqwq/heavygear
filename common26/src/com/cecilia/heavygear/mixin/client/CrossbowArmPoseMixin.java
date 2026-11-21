package com.cecilia.heavygear.mixin.client;

import com.cecilia.heavygear.client.CrossbowEquivalence;

import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * The two-handed "holding a loaded crossbow" arm pose is gated on {@code stack.is(Items.CROSSBOW)},
 * which never matches a look-alike item; without this the loaded heavy crossbow is held like a plain
 * item.
 */
@Mixin(AvatarRenderer.class)
public class CrossbowArmPoseMixin {
    @Redirect(method = "getArmPose(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"))
    private static boolean heavygear$isCrossbow(ItemStack stack, Object type) {
        return CrossbowEquivalence.is(stack, type);
    }
}