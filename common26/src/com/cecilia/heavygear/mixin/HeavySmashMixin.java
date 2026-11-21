package com.cecilia.heavygear.mixin;

import com.cecilia.heavygear.item.HeavySmash;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * {@code Item} is the common superclass of the heavy shovel / pickaxe / hoe and it is the class
 * {@code Player.attack} dispatches the smash hooks on, so overriding the base implementation
 * gives those tools the mace smash without changing their vanilla tool classes.
 */
@Mixin(Item.class)
public abstract class HeavySmashMixin {
    @Inject(
            method = "getAttackDamageBonus(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/damagesource/DamageSource;)F",
            at = @At("RETURN"),
            cancellable = true
    )
    private void heavygear$smashToolBonus(Entity target, float damage, DamageSource source,
                                           CallbackInfoReturnable<Float> cir) {
        if (HeavySmash.isSmashTool((Item) (Object) this)
                || HeavySmash.hasHeavyEffect(source.getDirectEntity())) {
            cir.setReturnValue(HeavySmash.attackDamageBonus(target, damage, source));
        }
    }

    @Inject(
            method = "hurtEnemy(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/LivingEntity;)V",
            at = @At("HEAD")
    )
    private void heavygear$smashToolHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker,
                                               CallbackInfo ci) {
        if (HeavySmash.isSmashTool(stack) || HeavySmash.hasHeavyEffect(attacker)) {
            HeavySmash.hurtEnemy(stack, target, attacker);
        }
    }

    @Inject(
            method = "postHurtEnemy(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/LivingEntity;)V",
            at = @At("HEAD")
    )
    private void heavygear$smashToolPostHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker,
                                                   CallbackInfo ci) {
        if (HeavySmash.isSmashTool(stack) || HeavySmash.hasHeavyEffect(attacker)) {
            HeavySmash.postHurtEnemy(stack, target, attacker);
        }
    }
}
