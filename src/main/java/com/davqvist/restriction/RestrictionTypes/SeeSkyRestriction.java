package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.RestrictionReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import static com.davqvist.restriction.utility.MessageHelper.*;

public class SeeSkyRestriction extends RestrictionType {
    public static final String ID =  "seesky";

    public SeeSkyRestriction(RestrictionReader.Descriptor descriptor) {
        super(descriptor);
    }

    @Override
    public boolean test(World world, BlockPos pos, PlayerEntity player) {
        return world.canSeeSky(pos) == descriptor.isReversed();
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public String getMessage() {
        return getBlockItem(descriptor) + " must " + getNot(descriptor) + "see the sky.";
    }

}
