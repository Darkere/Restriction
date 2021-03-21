package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.Restriction;
import com.davqvist.restriction.RestrictionReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class RestrictionType {

    RestrictionReader.Descriptor descriptor = null;

    public RestrictionType(RestrictionReader.Descriptor descriptor) {
        this.descriptor = descriptor;
    }

    public boolean test(World world, BlockPos pos, PlayerEntity player) {
        return true;
    }

    public String getID() {
        return "MISSING_ID";
    }

    public String getMessage() {
        return "missingTooltip";
    }

    public Restriction.Applicator getRestrictingType(){
        return descriptor.getApplicator();
    }

}


