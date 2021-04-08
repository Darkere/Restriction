package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.RestrictionReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OrRestriction extends RestrictionType {
    public static String ID = "OR";
    RestrictionType first;
    RestrictionType second;
    public OrRestriction(RestrictionReader.Descriptor descriptor, RestrictionType first, RestrictionType second) {
        super(descriptor);
        this.first = first;
        this.second = second;
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
        return first.getMessage() + " or " + second.getMessage();
    }
}
