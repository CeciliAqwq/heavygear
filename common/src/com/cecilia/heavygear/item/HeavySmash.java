package com.cecilia.heavygear.item;

import com.cecilia.heavygear.dive.DiveTracker;
import com.cecilia.heavygear.enchant.HeavyGearEnchantments;
import com.cecilia.heavygear.mixin.FireworkRocketAccessor;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;

/**
 * Gives the heavy sword, axe, shovel, pickaxe and hoe the vanilla mace smash attack.
 *
 * <p>Every effect is delegated to the real {@code minecraft:mace} instance instead of being
 * re-implemented, so the numbers stay in sync with vanilla and nothing is lost: the fall
 * distance damage curve {@code MaceItem#getAttackDamageBonus} (which applies Density through
 * {@code EnchantmentHelper.modifyFallBasedDamage} on the <em>attacker's actual weapon</em>),
 * the smash sound / particles / impulse of {@code MaceItem#hurtEnemy} (which also keeps
 * Breach working because it is part of the same single hurt call) and
 * {@code MaceItem#postHurtEnemy} (resets the fall distance so one fall cannot smash twice).</p>
 *
 * <p>Because the tool keeps its own item class, its own enchantments (Efficiency, Fortune,
 * Silk Touch, Unbreaking, Mending, ...) are untouched; the mace enchantments
 * (Density, Breach, Wind Burst) reach the tools through {@code #minecraft:enchantable/mace}.</p>
 */
public final class HeavySmash {
    /** Victim / tick of the most recent landed weapon smash (see {@link #didSmash}). */
    private static int SMASH_VICTIM_ID = -1;
    private static int SMASH_TICK = -100;

    private HeavySmash() {
    }

    public static boolean isSmashTool(ItemStack stack) {
        return stack != null && !stack.isEmpty() && isSmashTool(stack.getItem());
    }

    /** True while the heavy potion effect is active on {@code entity}. */
    public static boolean hasHeavyEffect(net.minecraft.world.entity.Entity entity) {
        return com.cecilia.heavygear.effect.HeavyEffect.has(entity);
    }

    public static boolean isSmashTool(Item item) {
        return item == HeavyGear.HEAVY_SWORD
                || item == HeavyGear.HEAVY_AXE
                || item == HeavyGear.HEAVY_SHOVEL
                || item == HeavyGear.HEAVY_PICKAXE
                || item == HeavyGear.HEAVY_HOE;
    }

    /** Smash damage bonus, i.e. {@codeMaceItem#getAttackDamageBonus} on this attacker. */
    public static float attackDamageBonus(Entity target, float damage, DamageSource source) {
        if (!(source.getDirectEntity() instanceof LivingEntity attacker)) {
            return 0.0F;
        }
        double saved = prepareDive(attacker);
        try {
            float bonus = Items.MACE.getAttackDamageBonus(target, damage, source);
            if (bonus > 0.0F) {
                // remember that this hit carried smash damage: the weapon smash rides on the
                // ordinary player_attack damage type, so the death message cannot detect it later
                SMASH_VICTIM_ID = target.getId();
                SMASH_TICK = target.tickCount;
            }
            return bonus;
        } finally {
            restoreFallDistance(attacker, saved);
        }
    }

    /** True when the weapon smash bonus landed on {@code victim} for the hit happening right now. */
    public static boolean didSmash(Entity victim) {
        return victim.getId() == SMASH_VICTIM_ID && victim.tickCount - SMASH_TICK <= 1;
    }

    /** Smash sound, particles and the small upward impulse, in the single vanilla hit. */
    public static void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        double saved = prepareDive(attacker);
        try {
            Items.MACE.hurtEnemy(stack, target, attacker);
        } finally {
            restoreFallDistance(attacker, saved);
        }
    }

    /** Vanilla resets the fall distance after a smash, so the bonus cannot be reused. */
    public static void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Items.MACE.postHurtEnemy(stack, target, attacker);
    }

    /**
     * While gliding {@code fallDistance} stays at 0, so the smash bonus would be 0. When the
     * weapon carries the dive bombing enchantment the tracked dive distance is substituted for
     * the duration of the call (same trick as {@code DiveSmashDamageMixin} does for the spear).
     *
     * @return the fall distance that has to be restored, or {@link Double#NaN} when untouched
     */
    private static double prepareDive(LivingEntity attacker) {
        if (!attacker.isFallFlying()
                || HeavyGearEnchantments.diveBombingLevel(attacker.getWeaponItem()) <= 0) {
            return Double.NaN;
        }
        double descended = DiveTracker.descended(attacker);
        if (descended < 1.5) {
            return Double.NaN;
        }
        cancelFirework(attacker);
        double saved = attacker.fallDistance;
        attacker.fallDistance = Math.min(descended, 20.0);
        return saved;
    }

    private static void restoreFallDistance(LivingEntity attacker, double saved) {
        if (!Double.isNaN(saved)) {
            attacker.fallDistance = saved;
        }
    }

    /** Stops the elytra fireworks from lifting the attacker back up during a dive smash. */
    public static void cancelFirework(LivingEntity attacker) {
        if (!(attacker.level() instanceof ServerLevel level)) {
            return;
        }
        AABB box = attacker.getBoundingBox().inflate(8.0);
        for (FireworkRocketEntity rocket : level.getEntitiesOfClass(FireworkRocketEntity.class, box)) {
            if (((FireworkRocketAccessor) rocket).heavygear$attachedTo() == attacker) {
                rocket.discard();
            }
        }
    }
}
