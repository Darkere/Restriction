package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.RestrictionReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OrRestriction extends RestrictionType{
    public OrRestriction(RestrictionReader.Descriptor descriptor) {
        super(descriptor);
    }

    @Override
    public boolean test(World world, BlockPos pos, PlayerEntity player) {
        return super.test(world, pos, player);
    }

    @Override
    public String getID() {
        return super.getID();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
