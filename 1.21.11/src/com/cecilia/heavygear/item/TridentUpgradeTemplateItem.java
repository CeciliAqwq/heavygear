package com.cecilia.heavygear.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.List;
import java.util.function.Consumer;

public class TridentUpgradeTemplateItem extends SmithingTemplateItem {
    private static final String SUFFIX_KEY = "item.heavygear.trident_upgrade_smithing_template";
    public static final String APPLIES_TO_KEY = SUFFIX_KEY + ".applies_to";
    public static final String INGREDIENTS_KEY = SUFFIX_KEY + ".ingredients";
    public static final String BASE_SLOT_KEY = SUFFIX_KEY + ".base_slot_description";
    public static final String ADDITIONS_SLOT_KEY = SUFFIX_KEY + ".additions_slot_description";

    public TridentUpgradeTemplateItem(Item.Properties properties) {
        super(
                Component.translatable(APPLIES_TO_KEY),
                Component.translatable(INGREDIENTS_KEY),
                Component.translatable(BASE_SLOT_KEY),
                Component.translatable(ADDITIONS_SLOT_KEY),
                List.of(ResourceLocation.withDefaultNamespace("container/slot/spear")),
                List.of(ResourceLocation.withDefaultNamespace("container/slot/ingot")),
                properties
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
                                Consumer<Component> consumer, TooltipFlag flag) {
        consumer.accept(Component.translatable("item.minecraft.smithing_template").withStyle(ChatFormatting.GRAY));
        consumer.accept(CommonComponents.EMPTY);
        consumer.accept(Component.translatable("item.minecraft.smithing_template.applies_to").withStyle(ChatFormatting.GRAY));
        consumer.accept(CommonComponents.space().append(Component.translatable(APPLIES_TO_KEY).withStyle(ChatFormatting.BLUE)));
        consumer.accept(Component.translatable("item.minecraft.smithing_template.ingredients").withStyle(ChatFormatting.GRAY));
        consumer.accept(CommonComponents.space().append(Component.translatable(INGREDIENTS_KEY).withStyle(ChatFormatting.BLUE)));
    }
}
