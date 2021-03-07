package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.config.RestrictionReader;
import com.davqvist.restriction.reference.Reference;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class SeeSkyRestriction implements RestrictionType {
    private static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "seesky");
    RestrictionReader.RestrictionDescriptor descriptor;

    @Override
    public boolean test(World world, BlockPos pos, RestrictionReader.RestrictionDescriptor descriptor, PlayerEntity player) {
        return world.canSeeSky(pos) != descriptor.getIsReversed();
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public String getMessage() {
        return "Block must " + ( descriptor.getIsReversed() ? "not " : "" ) + "see the sky.";
    }

}
