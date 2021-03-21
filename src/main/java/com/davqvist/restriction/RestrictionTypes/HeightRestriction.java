package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.RestrictionReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import static com.davqvist.restriction.utility.MessageHelper.*;
public class HeightRestriction extends RestrictionType {
    public static final String ID ="height";

    public HeightRestriction(RestrictionReader.Descriptor descriptor) {
        super(descriptor);
    }

    @Override
    public boolean test(World world, BlockPos pos, PlayerEntity player) {
        if (descriptor.isReversed()) {
            return pos.getY() < descriptor.getAmount();
        } else {
            return pos.getY() >= descriptor.getAmount();
        }
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public String getMessage() {
        return  getBlockItem(descriptor) + "can only be used at a " + (descriptor.isReversed() ? "maximum" : "minimum") + " height of " + descriptor.getAmount();
    }
}
