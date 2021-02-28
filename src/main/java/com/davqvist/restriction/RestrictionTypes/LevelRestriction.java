package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.config.RestrictionReader;
import com.davqvist.restriction.reference.Reference;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LevelRestriction implements RestrictionType{
    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID,"level");
    @Override
    public boolean test(World world, BlockPos pos, RestrictionReader.RestrictionDescriptor descriptor, PlayerEntity player) {
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
    public String getTooltip() {
        return "You must " + ( descriptor.getIsReversed() ? "not " : "" ) + "have at least " + descriptor.getAmount() + " levels of experience.";
    }
}
