package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.Restriction;
import com.davqvist.restriction.RestrictionReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HeightRestriction extends RestrictionType {
    public static final ResourceLocation ID = new ResourceLocation(Restriction.MOD_ID, "height");

    public HeightRestriction(RestrictionReader.Descriptor descriptor) {
        super(descriptor);
    }

    @Override
    public boolean test(World world, BlockPos pos, PlayerEntity player) {
        if (descriptor.IsReversed()) {
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
        return "Block must be at a " + (descriptor.IsReversed() ? "maximum" : "minimum") + " height of " + descriptor.getAmount();
    }
}
