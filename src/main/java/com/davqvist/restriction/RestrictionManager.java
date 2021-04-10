package com.davqvist.restriction;

import com.davqvist.restriction.RestrictionTypes.RestrictionType;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class RestrictionManager {
    public static final RestrictionManager INSTANCE = new RestrictionManager();
    private final Map<Restriction.Applicator, ListMultimap<String, RestrictionType>> itemRestrictions = new HashMap<>();
    private final Map<Restriction.Applicator, ListMultimap<String, RestrictionType>> blockRestrictions = new HashMap<>();
    public final Map<String, Function<RestrictionReader.Descriptor, RestrictionType>> RestrictionRegistry = new HashMap<>();
    private final Map<Restriction.Applicator, Function<ResourceLocation, String>> RSTransformer = new HashMap<>();
    private final List<Consumer<World>> loadRestrictions = new ArrayList<>();


    public RestrictionManager() {
        init();
    }

    @SuppressWarnings("UnstableApiUsage")
    public void init() {
        itemRestrictions.clear();
        blockRestrictions.clear();
        RSTransformer.clear();
        itemRestrictions.put(Restriction.Applicator.MOD, MultimapBuilder.hashKeys().arrayListValues().build());
        itemRestrictions.put(Restriction.Applicator.NAME, MultimapBuilder.hashKeys().arrayListValues().build());
        itemRestrictions.put(Restriction.Applicator.TAG, MultimapBuilder.hashKeys().arrayListValues().build());
        blockRestrictions.put(Restriction.Applicator.MOD, MultimapBuilder.hashKeys().arrayListValues().build());
        blockRestrictions.put(Restriction.Applicator.NAME, MultimapBuilder.hashKeys().arrayListValues().build());
        blockRestrictions.put(Restriction.Applicator.TAG, MultimapBuilder.hashKeys().arrayListValues().build());
        RSTransformer.put(Restriction.Applicator.MOD, ResourceLocation::getNamespace);
        RSTransformer.put(Restriction.Applicator.TAG, ResourceLocation::toString);
        RSTransformer.put(Restriction.Applicator.NAME, ResourceLocation::toString);
    }

    public void addRestriction(RestrictionReader.Descriptor descriptor) {
        loadRestrictions.add((world) -> {
            if (descriptor.getApplicator() == Restriction.Applicator.TAG) {
                if (descriptor.isBlockRestriction()) {
                    ITag<Block> blockTags = world.getTags().getBlockTags().get(new ResourceLocation(descriptor.tag));
                    if(blockTags == null){
                        Restriction.LOGGER.warn("Invalid tag: " + descriptor.tag);

                    } else {
                        blockTags.getAllElements().forEach(block-> blockRestrictions.get(descriptor.getApplicator()).put(block.getRegistryName().toString(), RestrictionRegistry.get(descriptor.type).apply(descriptor)));
                    }
                }
                if (descriptor.isItemRestriction()) {
                    ITag<Item> itemTags = world.getTags().getItemTags().get(new ResourceLocation(descriptor.tag));
                    if(itemTags == null){
                        Restriction.LOGGER.warn("Invalid tag: " + descriptor.tag);

                    } else {
                        itemTags.getAllElements().forEach(item-> blockRestrictions.get(descriptor.getApplicator()).put(item.getRegistryName().toString(), RestrictionRegistry.get(descriptor.type).apply(descriptor)));
                    }
                }
            }

            if (descriptor.isBlockRestriction())
                blockRestrictions.get(descriptor.getApplicator()).put(descriptor.getApplicatorString(), RestrictionRegistry.get(descriptor.type).apply(descriptor));
            if (descriptor.isItemRestriction())
                itemRestrictions.get(descriptor.getApplicator()).put(descriptor.getApplicatorString(), RestrictionRegistry.get(descriptor.type).apply(descriptor));
        });
    }

    public void registerRestrictionType(String ID, Function<RestrictionReader.Descriptor, RestrictionType> Translator) {
        RestrictionRegistry.put(ID, Translator);
    }

    public boolean testItemRestriction(World world, BlockPos pos, PlayerEntity player, ResourceLocation name) {
        return testRestriction(world, pos, player, name, true);
    }

    public boolean testBlockRestriction(World world, BlockPos pos, PlayerEntity player, ResourceLocation name) {
        return testRestriction(world, pos, player, name, false);
    }


    public List<ITextComponent> getMessages(Item item) {
        List<ITextComponent> text = new ArrayList<>();
        if (item instanceof BlockItem) {
            for (Map.Entry<Restriction.Applicator, ListMultimap<String, RestrictionType>> entry : blockRestrictions.entrySet()) {
                for (RestrictionType restriction : entry.getValue().get(RSTransformer.get(entry.getKey()).apply(item.getRegistryName()))) {
                    text.add(new StringTextComponent(restriction.getMessage()));
                }
            }
        } else {
            for (Map.Entry<Restriction.Applicator, ListMultimap<String, RestrictionType>> entry : itemRestrictions.entrySet()) {
                for (RestrictionType restriction : entry.getValue().get(RSTransformer.get(entry.getKey()).apply(item.getRegistryName()))) {
                    text.add(new StringTextComponent(restriction.getMessage()));
                }
            }
        }

        return text;
    }

    private boolean testRestriction(World world, BlockPos pos, PlayerEntity player, ResourceLocation name, boolean item) {
        loadRestrictions.forEach(w->w.accept(world));
        Map<Restriction.Applicator, ListMultimap<String, RestrictionType>> map = item ? itemRestrictions : blockRestrictions;
        boolean result = false;
        for (Map.Entry<Restriction.Applicator, ListMultimap<String, RestrictionType>> entry : map.entrySet()) {
            for (RestrictionType restriction : entry.getValue().get(RSTransformer.get(entry.getKey()).apply(name))) {
                result = restriction.test(world, pos, player);
                if (result) {
                    player.sendStatusMessage(new StringTextComponent(restriction.getMessage()), true);
                    break;
                }
            }
            if (result)
                break;
        }

        return result;
    }

}
