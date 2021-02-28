package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.config.RestrictionReader;
import com.davqvist.restriction.reference.Reference;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DimensionRestriction implements RestrictionType{
    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID,"dimension");
    @Override
    public boolean test(World world, BlockPos pos, RestrictionReader.RestrictionDescriptor descriptor, PlayerEntity player) {
        return world.getDimensionKey().getLocation().equals(new ResourceLocation(descriptor.dimension));
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public String getTooltip() {
        return "Block must " + ( descriptor.getIsReversed() ? "not " : "" ) + "be in dimension " + descriptor.dimension;
    }

}
