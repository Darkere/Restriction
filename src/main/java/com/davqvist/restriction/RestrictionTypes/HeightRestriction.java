package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.config.RestrictionReader;
import com.davqvist.restriction.reference.Reference;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HeightRestriction implements RestrictionType {
    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "height");

    @Override
    public boolean test(World world, BlockPos pos, RestrictionReader.RestrictionDescriptor descriptor, PlayerEntity player) {
        if (descriptor.getIsReversed()) {
            return pos.getY() < descriptor.getAmount();
        } else {
            return pos.getY() >= descriptor.getAmount();
        }
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public String getMessage() {
        return "Block must be at a " + (descriptor.getIsReversed() ? "maximum" : "minimum") + " height of " + descriptor.getAmount();
    }
}
