package com.davqvist.restriction.utility;

import com.davqvist.restriction.RestrictionReader;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class MatchHelper {

    public static boolean matches(World world, RestrictionReader.Descriptor descriptor, Block candidate){
        boolean result = false;
        switch (descriptor.getApplicator())
        {
            case MOD:
                result = descriptor.mod.equals(candidate.getRegistryName().getNamespace());
                break;
            case TAG:
                ITag<Block> tag = world.getTags().getBlockTags().get(new ResourceLocation(descriptor.tag));
                result =  tag != null && tag.contains(candidate);
                break;
            case NAME:
                result =  ForgeRegistries.BLOCKS.getValue(new ResourceLocation(descriptor.name)) == candidate;
                break;
        }
        return  result == descriptor.isReversed();
    }

    public static boolean matches(World world, RestrictionReader.Descriptor descriptor, Item candidate){
        boolean result = false;
        switch (descriptor.getApplicator())
        {
            case MOD:
                result = descriptor.mod.equals(candidate.getRegistryName().getNamespace());
                break;
            case TAG:
                ITag<Item> tag = world.getTags().getItemTags().get(new ResourceLocation(descriptor.tag));
                result =  tag != null && tag.contains(candidate);
                break;
            case NAME:
                result =   ForgeRegistries.ITEMS.getValue(new ResourceLocation(descriptor.name)) == candidate;
                break;
        }
        return  result == descriptor.isReversed();
    }
}
