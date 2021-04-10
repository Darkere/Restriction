package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.RestrictionManager;
import com.davqvist.restriction.RestrictionReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AndRestriction extends RestrictionType{
    public static String ID = "and";
    RestrictionType first;
    RestrictionType second;

    public AndRestriction(RestrictionReader.Descriptor descriptor) {
        super(descriptor);
        first = RestrictionManager.INSTANCE.createRestriction(descriptor.first);
        second = RestrictionManager.INSTANCE.createRestriction(descriptor.second);
    }

    @Override
    public boolean test(World world, BlockPos pos, PlayerEntity player) {
        return first.test(world, pos, player) || second.test(world, pos, player);
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public String getMessage() {
        return first.getMessage() + " and " + second.getMessage();
    }
}
