package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.config.RestrictionReader;
import com.davqvist.restriction.reference.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.List;


public class SeeSkyRestriction implements RestrictionType {
    private static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "seesky");
    RestrictionReader.RestrictionDescriptor descriptor;

    @Override
    public boolean test(World world, BlockPos pos, RestrictionReader.RestrictionDescriptor descriptor) {
        return world.canSeeSky(pos) != descriptor.getIsReversed();
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public String getTooltip() {
        return "Block must " + ( descriptor.getIsReversed() ? "not " : "" ) + "see the sky.";
    }

}
