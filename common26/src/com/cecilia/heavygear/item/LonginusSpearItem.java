package com.cecilia.heavygear.item;

import com.cecilia.heavygear.combat.RiptideContact;
import com.cecilia.heavygear.enchant.HeavyGearEnchantments;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

/**
 * Spear of Longinus. Two modes, toggled with the configured key:
 * - Normal mode: normal charge attack only. No Riptide, cannot be thrown.
 * - Trident mode: charge attack still works; Riptide (in water/rain) runs the
 *   vanilla dash on release, and without Riptide the spear is thrown on release.
 */
public class LonginusSpearItem extends HeavySpearItem {
    public static final int THROW_THRESHOLD_TIME = 7; // 70% of the vanilla trident's 10 tick charge
    public static final float SHOOT_POWER = 2.5F;
    private static final TridentItem VANILLA_TRIDENT = (TridentItem) Items.TRIDENT;

    public LonginusSpearItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.SPEAR;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (LonginusMode.isTrident(stack)
                && HeavyGearEnchantments.level(stack, Enchantments.RIPTIDE) > 0
                && player.isInWaterOrRain()) {
            return VANILLA_TRIDENT.use(level, player, hand);
        }
        return super.use(level, player, hand);
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) {
            return false;
        }
        if (LonginusMode.isTrident(stack)
                && HeavyGearEnchantments.level(stack, Enchantments.RIPTIDE) > 0
                && player.isInWaterOrRain()) {
            return VANILLA_TRIDENT.releaseUsing(stack, level, entity, timeLeft);
        }
        return super.releaseUsing(stack, level, entity, timeLeft);
    }

    /**
     * The riptide dash contact hit must behave like a vanilla trident (plain contact damage, no mace
     * smash), while charged attacks and stabs keep their smash — hence the narrow
     * {@link RiptideContact} bracket instead of disabling the smash for the whole dash.
     */
    @Override
    public float getAttackDamageBonus(Entity target, float damage, DamageSource source) {
        if (RiptideContact.isActive()) {
            return 0.0F;
        }
        return super.getAttackDamageBonus(target, damage, source);
    }

    /** Same for the smash feedback (sound, particles, impulse) of the dash contact hit. */
    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (RiptideContact.isActive()) {
            return;
        }
        super.hurtEnemy(stack, target, attacker);
    }

    /**
     * Shows the current mode in the item tooltip (same wording / colours as the toggle message).
     * Declared under its intermediary name: {@code Item#appendHoverText} is still unmapped for
     * {@code Item} itself in the mapping table, so a source-level {@code appendHoverText} would
     * neither compile nor override anything.
     */
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
                             Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        tooltip.accept(Component.translatable("tooltip.heavygear.lance_of_longinus.rune")
                .withStyle(ChatFormatting.GRAY));
        boolean trident = LonginusMode.isTrident(stack);
        tooltip.accept(Component.translatable("message.heavygear.longinus_mode.prefix")
                .withStyle(ChatFormatting.GRAY)
                .append(Component.translatable(trident
                                ? "message.heavygear.longinus_mode.trident"
                                : "message.heavygear.longinus_mode.normal")
                        .withStyle(trident ? ChatFormatting.AQUA : ChatFormatting.GREEN)));
        if (net.fabricmc.loader.api.FabricLoader.getInstance().getEnvironmentType()
                == net.fabricmc.api.EnvType.CLIENT) {
            // client-only: shows the key currently bound to the mode toggle
            tooltip.accept(com.cecilia.heavygear.client.LanceTooltipSupport.toggleHint());
        }
    }
}
