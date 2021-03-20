package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.Restriction;
import com.davqvist.restriction.RestrictionReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LevelRestriction extends RestrictionType {
    public static final ResourceLocation ID = new ResourceLocation(Restriction.MOD_ID,"level");

    public LevelRestriction(RestrictionReader.Descriptor descriptor) {
        super(descriptor);
    }

    @Override
    public boolean test(World world, BlockPos pos, PlayerEntity player) {
        if (descriptor.getAmount() >= 0) {
            return (pos.getY() >= descriptor.getAmount());
        }
        return true;
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public String getMessage() {
        return "You must " + ( descriptor.IsReversed() ? "not " : "" ) + "have at least " + descriptor.getAmount() + " levels of experience.";
    }
}
