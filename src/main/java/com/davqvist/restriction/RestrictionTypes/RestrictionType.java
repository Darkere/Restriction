package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.Restriction;
import com.davqvist.restriction.config.RestrictionReader;
import com.davqvist.restriction.reference.Reference;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface RestrictionType {

    ResourceLocation MISSING_ID = new ResourceLocation(Reference.MOD_ID, "missing_id");
    String missingTooltip = "Missing Tooltip";
    RestrictionReader.RestrictionDescriptor descriptor = null;
    default boolean test(World world, BlockPos pos, RestrictionReader.RestrictionDescriptor descriptor, PlayerEntity player) {
        return true;
    }

    default ResourceLocation getID() {
        return MISSING_ID;
    }

    default String getMessage() {
        return missingTooltip;
    }
    default Restriction.Applicator getRestrictingType(){
        return descriptor.block.getRestrictingType();
    };

}


