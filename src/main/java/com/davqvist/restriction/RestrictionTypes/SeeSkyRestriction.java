package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.Restriction;
import com.davqvist.restriction.RestrictionReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class SeeSkyRestriction extends RestrictionType {
    public static final ResourceLocation ID = new ResourceLocation(Restriction.MOD_ID, "seesky");

    public SeeSkyRestriction(RestrictionReader.Descriptor descriptor) {
        super(descriptor);
    }

    @Override
    public boolean test(World world, BlockPos pos, PlayerEntity player) {
        return world.canSeeSky(pos) != descriptor.IsReversed();
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public String getMessage() {
        return "Block must " + ( descriptor.IsReversed() ? "not " : "" ) + "see the sky.";
    }

}
