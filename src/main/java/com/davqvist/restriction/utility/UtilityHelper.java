package com.davqvist.restriction.utility;

import com.davqvist.restriction.Restriction;
import com.davqvist.restriction.config.RestrictionReader;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class UtilityHelper{

    public static String getBlockName( String blockString){
        Block block =  ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockString));
        if(block == null) return "";
        else return block.getTranslatedName().getString();
    }

    public static boolean matches(World world, RestrictionReader.BlockOrTag block, Block candidate){
        if(block.isTag()){
            ITag<Block> tag = world.getTags().getBlockTags().get(new ResourceLocation(block.name));
            return tag != null && tag.contains(candidate);
        } else {
            return candidate.equals(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block.name)));
        }
    }
    public static boolean matches(World world, RestrictionReader.BlockOrTag block, Item candidate){
        if(!(candidate instanceof BlockItem)) return false;
        Block candidateBlock = ((BlockItem)candidate).getBlock();
        if(block.isTag()){
            ITag<Block> tag = world.getTags().getBlockTags().get(new ResourceLocation(block.name));
            return tag != null && tag.contains(candidateBlock);
        } else {
            return candidateBlock.equals(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block.name)));
        }
    }
}
