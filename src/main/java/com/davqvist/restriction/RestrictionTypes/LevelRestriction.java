package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.RestrictionReader;
import com.davqvist.restriction.utility.MessageHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LevelRestriction extends RestrictionType {
    public static final String ID = "level";

    public LevelRestriction(RestrictionReader.Descriptor descriptor) {
        super(descriptor);
    }

    @Override
    public boolean test(World world, BlockPos pos, PlayerEntity player) {
        return player.experienceLevel > descriptor.getAmount() == descriptor.isReversed();
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public String getMessage() {
        return "You " + (descriptor.isReversed() ? "can have at most ": "need at least ") + descriptor.getAmount() + " levels of experience to use this " + MessageHelper.getBlockItem(descriptor) + ".";
    }
}
