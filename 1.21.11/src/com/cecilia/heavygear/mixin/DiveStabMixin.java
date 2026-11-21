package com.cecilia.heavygear.mixin;

import com.cecilia.heavygear.dive.DiveTracker;
import com.cecilia.heavygear.enchant.HeavyGearEnchantments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Spear stabs (piercing attack and kinetic charge) go through Player.stabAttack, which
 * never asks the item for its smash damage bonus, so Density and the fall-distance smash
 * bonus are skipped. Add the same bonus the normal attack gets.
 */
@Mixin(Player.class)
public abstract class DiveStabMixin {
    private static final ThreadLocal<Entity> HEAVYSPEAR_TARGET = new ThreadLocal<>();

    @Inject(method = "method_75123", at = @At("HEAD"))
    private void heavygear$beginStab(EquipmentSlot slot, Entity target, float damage,
                                     boolean dealDamage, boolean knockback, boolean dismount,
                                     CallbackInfoReturnable<Boolean> cir) {
        HEAVYSPEAR_TARGET.set(target);
    }

    @Inject(method = "method_75123", at = @At("RETURN"))
    private void heavygear$endStab(EquipmentSlot slot, Entity target, float damage,
                                   boolean dealDamage, boolean knockback, boolean dismount,
                                   CallbackInfoReturnable<Boolean> cir) {
        HEAVYSPEAR_TARGET.remove();
    }

    @ModifyArg(
            method = "method_75123",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1297;method_64420(Lnet/minecraft/class_1282;F)Z"),
            index = 1
    )
    private float heavygear$stabWithSmashBonus(float amount) {
        Player self = (Player) (Object) this;
        Entity target = HEAVYSPEAR_TARGET.get();
        if (target == null) {
            return amount;
        }
        ItemStack stack = self.getWeaponItem();
        if (!(stack.getItem() instanceof MaceItem)) {
            return amount;
        }
        float fall;
        if (self.isFallFlying()) {
            double descended = DiveTracker.descended(self);
            if (HeavyGearEnchantments.diveBombingLevel(stack) <= 0 || descended < 1.5) {
                return amount;
            }
            fall = (float) Math.min(descended, 20.0);
        } else if (self.fallDistance > 1.5) {
            fall = (float) self.fallDistance;
        } else {
            return amount;
        }
        double bonus = fall <= 3.0
                ? 4.0 * fall
                : (fall <= 8.0 ? 12.0 + 2.0 * (fall - 3.0) : 22.0 + (fall - 8.0));
        if (self.level() instanceof ServerLevel level) {
            DamageSource source = self.damageSources().playerAttack(self);
            bonus += EnchantmentHelper.modifyFallBasedDamage(level, stack, target, source, 0.0F) * fall;
        }
        return amount + (float) bonus;
    }
}
