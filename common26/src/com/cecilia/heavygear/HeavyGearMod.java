package com.cecilia.heavygear;

import com.cecilia.heavygear.common.LonginusThrow;
import com.cecilia.heavygear.entity.ThrownLonginus;
import com.cecilia.heavygear.effect.HeavyEffect;
import com.cecilia.heavygear.item.HeavyGear;
import com.cecilia.heavygear.item.HeavySpearItem;
import com.cecilia.heavygear.item.HeavyUpgradeTemplateItem;
import com.cecilia.heavygear.item.LonginusSpearItem;
import com.cecilia.heavygear.item.TridentUpgradeTemplateItem;
import com.cecilia.heavygear.network.LonginusThrowPayload;
import com.cecilia.heavygear.network.LonginusCancelPayload;
import com.cecilia.heavygear.network.LonginusModePayload;
import com.cecilia.heavygear.item.LonginusMode;
import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HeavyGearMod implements ModInitializer {
    public static final String MOD_ID = "heavygear";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final DataComponentType<Boolean> LONGINUS_TRIDENT_MODE = DataComponentType.<Boolean>builder()
            .persistent(Codec.BOOL)
            .networkSynchronized(ByteBufCodecs.BOOL)
            .build();

    public static final Identifier HEAVY_SPEAR_ID = Identifier.fromNamespaceAndPath(MOD_ID, "heavy_spear");
    public static final ResourceKey<Item> HEAVY_SPEAR_KEY = ResourceKey.create(Registries.ITEM, HEAVY_SPEAR_ID);
    public static final Identifier TEMPLATE_ID = Identifier.fromNamespaceAndPath(MOD_ID, "heavy_upgrade_smithing_template");
    public static final ResourceKey<Item> TEMPLATE_KEY = ResourceKey.create(Registries.ITEM, TEMPLATE_ID);

    public static final Identifier LONGINUS_ID = Identifier.fromNamespaceAndPath(MOD_ID, "lance_of_longinus");
    public static final ResourceKey<Item> LONGINUS_KEY = ResourceKey.create(Registries.ITEM, LONGINUS_ID);
    public static final Identifier TRIDENT_TEMPLATE_ID = Identifier.fromNamespaceAndPath(MOD_ID, "trident_upgrade_smithing_template");
    public static final ResourceKey<Item> TRIDENT_TEMPLATE_KEY = ResourceKey.create(Registries.ITEM, TRIDENT_TEMPLATE_ID);

    public static final ResourceKey<CreativeModeTab> HEAVY_GEAR_TAB_KEY = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(MOD_ID, "heavy_gear"));

    public static final ResourceKey<EntityType<?>> THROWN_LONGINUS_KEY = ResourceKey.create(Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(MOD_ID, "thrown_longinus"));
    public static final EntityType<ThrownLonginus> THROWN_LONGINUS_TYPE = EntityType.Builder
            .<ThrownLonginus>of((type, level) -> new ThrownLonginus(type, level), MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20)
            .build(THROWN_LONGINUS_KEY);

    public static final ResourceKey<EntityType<?>> COMPRESSED_WIND_CHARGE_KEY =
            ResourceKey.create(Registries.ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath(MOD_ID, "compressed_wind_charge"));
    public static final ResourceKey<EntityType<?>> DOUBLE_COMPRESSED_WIND_CHARGE_KEY =
            ResourceKey.create(Registries.ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath(MOD_ID, "double_compressed_wind_charge"));
    public static final EntityType<com.cecilia.heavygear.entity.DoubleCompressedWindCharge>
            DOUBLE_COMPRESSED_WIND_CHARGE_TYPE = EntityType.Builder
            .<com.cecilia.heavygear.entity.DoubleCompressedWindCharge>of(
                    (type, level) -> new com.cecilia.heavygear.entity.DoubleCompressedWindCharge(type, level),
                    MobCategory.MISC)
            .sized(0.3125F, 0.3125F)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build(DOUBLE_COMPRESSED_WIND_CHARGE_KEY);

    public static final EntityType<com.cecilia.heavygear.entity.CompressedWindCharge>
            COMPRESSED_WIND_CHARGE_TYPE = EntityType.Builder
            .<com.cecilia.heavygear.entity.CompressedWindCharge>of(
                    (type, level) -> new com.cecilia.heavygear.entity.CompressedWindCharge(type, level),
                    MobCategory.MISC)
            .sized(0.3125F, 0.3125F)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build(COMPRESSED_WIND_CHARGE_KEY);

    public static final ResourceKey<EntityType<?>> HEAVY_ARROW_KEY = ResourceKey.create(Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(MOD_ID, "heavy_arrow"));
    public static final EntityType<com.cecilia.heavygear.entity.HeavyArrow> HEAVY_ARROW_TYPE = EntityType.Builder
            .<com.cecilia.heavygear.entity.HeavyArrow>of(
                    (type, level) -> new com.cecilia.heavygear.entity.HeavyArrow(type, level), MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20)
            .build(HEAVY_ARROW_KEY);

    public static final Item HEAVY_SPEAR = new HeavySpearItem(spearProperties(HEAVY_SPEAR_KEY));
    public static final Item LONGINUS_SPEAR = new LonginusSpearItem(spearProperties(LONGINUS_KEY));
    public static final Item HEAVY_UPGRADE_SMITHING_TEMPLATE = new HeavyUpgradeTemplateItem(new Item.Properties()
            .setId(TEMPLATE_KEY)
            .rarity(Rarity.UNCOMMON));
    public static final Item TRIDENT_UPGRADE_SMITHING_TEMPLATE = new TridentUpgradeTemplateItem(new Item.Properties()
            .setId(TRIDENT_TEMPLATE_KEY)
            .rarity(Rarity.UNCOMMON));

    private static Item.Properties spearProperties(ResourceKey<Item> key) {
        return new Item.Properties()
                .setId(key)
                .rarity(Rarity.EPIC)
                .spear(ToolMaterial.NETHERITE, 1.15F, 1.2F, 0.4F, 2.5F, 9.0F, 5.5F, 5.1F, 8.75F, 4.6F)
                .fireResistant();
    }

    private static ResourceKey<CreativeModeTab> tab(String name) {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace(name));
    }

    @Override
    public void onInitialize() {
        Registry.register(BuiltInRegistries.ITEM, HEAVY_SPEAR_KEY, HEAVY_SPEAR);
        Registry.register(BuiltInRegistries.ITEM, LONGINUS_KEY, LONGINUS_SPEAR);
        Registry.register(BuiltInRegistries.ITEM, TEMPLATE_KEY, HEAVY_UPGRADE_SMITHING_TEMPLATE);
        Registry.register(BuiltInRegistries.ITEM, TRIDENT_TEMPLATE_KEY, TRIDENT_UPGRADE_SMITHING_TEMPLATE);
        HeavyGear.register();
        HeavyEffect.bootstrap();
        // Dedicated creative tab so the whole set is findable in one place
        // (the vanilla tabs below also get the items, in their usual categories).
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, HEAVY_GEAR_TAB_KEY, FabricCreativeModeTab.builder()
                .title(Component.translatable("itemGroup.heavygear.heavy_gear"))
                .icon(() -> new ItemStack(HEAVY_SPEAR))
                .build());
        CreativeModeTabEvents.modifyOutputEvent(HEAVY_GEAR_TAB_KEY).register(output -> {
            output.accept(HEAVY_SPEAR);
            output.accept(LONGINUS_SPEAR);
            output.accept(HeavyGear.HEAVY_SWORD);
            output.accept(HeavyGear.HEAVY_AXE);
            output.accept(HeavyGear.HEAVY_PICKAXE);
            output.accept(HeavyGear.HEAVY_SHOVEL);
            output.accept(HeavyGear.HEAVY_HOE);
            output.accept(HeavyGear.HEAVY_BOW);
            output.accept(HeavyGear.HEAVY_CROSSBOW);
            output.accept(HeavyGear.HEAVY_ARROW);
            output.accept(HeavyGear.HEAVY_FISHING_ROD);
            output.accept(HeavyGear.HEAVY_HELMET);
            output.accept(HeavyGear.HEAVY_CHESTPLATE);
            output.accept(HeavyGear.HEAVY_LEGGINGS);
            output.accept(HeavyGear.HEAVY_BOOTS);
            output.accept(HEAVY_UPGRADE_SMITHING_TEMPLATE);
            output.accept(TRIDENT_UPGRADE_SMITHING_TEMPLATE);
            output.accept(HeavyGear.ECAM);
            output.accept(HeavyGear.COMPRESSED_WIND_CHARGE);
            output.accept(HeavyGear.DOUBLE_COMPRESSED_WIND_CHARGE);
        });
        Registry.register(BuiltInRegistries.ENTITY_TYPE, THROWN_LONGINUS_KEY, THROWN_LONGINUS_TYPE);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, HEAVY_ARROW_KEY, HEAVY_ARROW_TYPE);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, COMPRESSED_WIND_CHARGE_KEY,
                COMPRESSED_WIND_CHARGE_TYPE);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, DOUBLE_COMPRESSED_WIND_CHARGE_KEY,
                DOUBLE_COMPRESSED_WIND_CHARGE_TYPE);
        // dispensers must throw the compressed charges too
        net.minecraft.world.level.block.DispenserBlock.registerBehavior(
                com.cecilia.heavygear.item.HeavyGear.COMPRESSED_WIND_CHARGE,
                new net.minecraft.core.dispenser.ProjectileDispenseBehavior(
                        com.cecilia.heavygear.item.HeavyGear.COMPRESSED_WIND_CHARGE));
        net.minecraft.world.level.block.DispenserBlock.registerBehavior(
                com.cecilia.heavygear.item.HeavyGear.DOUBLE_COMPRESSED_WIND_CHARGE,
                new net.minecraft.core.dispenser.ProjectileDispenseBehavior(
                        com.cecilia.heavygear.item.HeavyGear.DOUBLE_COMPRESSED_WIND_CHARGE));
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                Identifier.fromNamespaceAndPath(MOD_ID, "longinus_trident_mode"), LONGINUS_TRIDENT_MODE);

        PayloadTypeRegistry.serverboundPlay().register(LonginusThrowPayload.TYPE, LonginusThrowPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(LonginusThrowPayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            LOGGER.info("longinus payload received hand={} charge={} player={}", payload.hand(), payload.chargeTicks(),
                    player.getName().getString());
            context.server().execute(() -> LonginusThrow.perform(player, payload));
        });

        PayloadTypeRegistry.serverboundPlay().register(LonginusCancelPayload.TYPE, LonginusCancelPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(LonginusCancelPayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            context.server().execute(player::stopUsingItem);
        });

        PayloadTypeRegistry.serverboundPlay().register(LonginusModePayload.TYPE, LonginusModePayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(
                com.cecilia.heavygear.network.LonginusModeSyncPayload.TYPE,
                com.cecilia.heavygear.network.LonginusModeSyncPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(LonginusModePayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            context.server().execute(() -> {
                ItemStack stack = player.getItemInHand(payload.hand());
                if (stack.getItem() instanceof LonginusSpearItem) {
                    boolean trident = !LonginusMode.isTrident(stack);
                    LonginusMode.setTrident(stack, trident);
                    LOGGER.info("longinus mode set trident={} player={}", trident, player.getName().getString());
                    net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player,
                            new com.cecilia.heavygear.network.LonginusModeSyncPayload(trident));
                    player.sendSystemMessage(Component.translatable(
                            "message.heavygear.longinus_mode.prefix").append(Component.translatable(trident
                            ? "message.heavygear.longinus_mode.trident"
                            : "message.heavygear.longinus_mode.normal")
                            .withStyle(trident ? net.minecraft.ChatFormatting.AQUA
                                    : net.minecraft.ChatFormatting.GREEN)), true);
                }
            });
        });

        CreativeModeTabEvents.modifyOutputEvent(tab("combat"))
                .register(entries -> {
                    // the heavy spear belongs behind the netherite spear (1.21.11 spear family)
                    Item netheriteSpear = BuiltInRegistries.ITEM
                            .getOptional(Identifier.withDefaultNamespace("netherite_spear"))
                            .orElse(Items.AIR);
                    Item spearAnchor = netheriteSpear == Items.AIR ? Items.TRIDENT : netheriteSpear;
                    entries.insertAfter(new ItemStack(spearAnchor), new ItemStack(HEAVY_SPEAR));
                    // ... and the lance right in front of the mace
                    entries.insertBefore(new ItemStack(Items.MACE), new ItemStack(LONGINUS_SPEAR));
                    entries.insertAfter(new ItemStack(Items.NETHERITE_SWORD),
                            new ItemStack(HeavyGear.HEAVY_SWORD));
                    entries.insertAfter(new ItemStack(Items.NETHERITE_AXE),
                            new ItemStack(HeavyGear.HEAVY_AXE));
                    // the four heavy armour pieces stay together, right after the netherite set
                    entries.insertAfter(new ItemStack(Items.NETHERITE_BOOTS),
                            new ItemStack(HeavyGear.HEAVY_HELMET),
                            new ItemStack(HeavyGear.HEAVY_CHESTPLATE),
                            new ItemStack(HeavyGear.HEAVY_LEGGINGS),
                            new ItemStack(HeavyGear.HEAVY_BOOTS));
                    entries.insertAfter(new ItemStack(Items.BOW), new ItemStack(HeavyGear.HEAVY_BOW));
                    entries.insertAfter(new ItemStack(Items.CROSSBOW),
                            new ItemStack(HeavyGear.HEAVY_CROSSBOW));
                    entries.insertAfter(new ItemStack(Items.ARROW), new ItemStack(HeavyGear.HEAVY_ARROW));
                });
        CreativeModeTabEvents.modifyOutputEvent(tab("tools_and_utilities"))
                .register(entries -> {
                    // the four heavy tools stay together, right after the netherite tools, in the
                    // requested order: shovel -> pickaxe -> axe -> hoe
                    entries.insertAfter(new ItemStack(Items.NETHERITE_HOE),
                            new ItemStack(HeavyGear.HEAVY_SHOVEL),
                            new ItemStack(HeavyGear.HEAVY_PICKAXE),
                            new ItemStack(HeavyGear.HEAVY_AXE),
                            new ItemStack(HeavyGear.HEAVY_HOE));
                    entries.insertAfter(new ItemStack(Items.FISHING_ROD),
                            new ItemStack(HeavyGear.HEAVY_FISHING_ROD));
                    entries.insertAfter(new ItemStack(HeavyGear.HEAVY_FISHING_ROD),
                            new ItemStack(HeavyGear.HEAVY_POTION),
                            new ItemStack(HeavyGear.HEAVY_SPLASH_POTION),
                            new ItemStack(HeavyGear.HEAVY_LINGERING_POTION));
                });
        CreativeModeTabEvents.modifyOutputEvent(tab("ingredients"))
                .register(entries -> {
                    // both smithing templates sit right behind the netherite upgrade template
                    entries.insertAfter(new ItemStack(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                            new ItemStack(HEAVY_UPGRADE_SMITHING_TEMPLATE),
                            new ItemStack(TRIDENT_UPGRADE_SMITHING_TEMPLATE));
                });
    }
}
