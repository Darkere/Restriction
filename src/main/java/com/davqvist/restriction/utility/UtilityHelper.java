package com.davqvist.restriction.utility;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class UtilityHelper{

    public static String getBlockName( String blockString, boolean ignoreMeta, int meta ){

        return ForgeRegistries. BLOCKS.getValue(new ResourceLocation(blockString)).getRegistryName().toString();
    }

}
