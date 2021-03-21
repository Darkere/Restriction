package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.Restriction;
import com.davqvist.restriction.RestrictionReader;
import com.davqvist.restriction.utility.MessageHelper;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GameStageRestriction extends RestrictionType{
    public static final String ID = "gamestage";

    public GameStageRestriction(RestrictionReader.Descriptor descriptor) {
        super(descriptor);
        if(!GameStageHelper.isValidStageName(descriptor.stage) || !GameStageHelper.isStageKnown(descriptor.stage))
            Restriction.LOGGER.error(descriptor.stage + " is not a valid GameStage");
    }

    @Override
    public boolean test(World world, BlockPos pos, PlayerEntity player) {
        return  GameStageHelper.hasStage(player, descriptor.stage) == descriptor.isReversed();
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public String getMessage() {
        return MessageHelper.getBlockItem(descriptor) + " requires " + (descriptor.isReversed() ? "you to not have ": "") + "Gamestage " + descriptor.stage;
    }

}
