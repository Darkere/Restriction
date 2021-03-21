package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.RestrictionReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.davqvist.restriction.utility.MessageHelper.*;
public class DimensionRestriction extends RestrictionType {
    public static final String ID = "dimension";

    public DimensionRestriction(RestrictionReader.Descriptor descriptor) {
        super(descriptor);
    }

    @Override
    public boolean test(World world, BlockPos pos, PlayerEntity player) {
        return world.getDimensionKey().getLocation().equals(new ResourceLocation(descriptor.dimension)) == descriptor.isReversed();
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public String getMessage() {
        return getBlockItem(descriptor) + " " + (descriptor.isReversed() ? "cannot" : "can only") + " be used in dimension " + descriptor.dimension;
    }

}
