package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.config.RestrictionReader;
import com.davqvist.restriction.reference.Reference;
import com.davqvist.restriction.utility.RestrictionHelper;
import com.davqvist.restriction.utility.UtilityHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class ClosedRoomRestriction implements RestrictionType {
    private static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "closedroom");


    @Override
    public boolean test(World world, BlockPos pos, RestrictionReader.RestrictionDescriptor descriptor, PlayerEntity player) {

    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public String getMessage() {
        boolean reverse = descriptor.getIsReversed();
        int size = descriptor.getAmount();
        String blockString =  descriptor.block.name;
        int amount = descriptor.block.getCount();
        String blockname = UtilityHelper.getBlockOrTagName(descriptor);
        switch( RestrictionHelper.LAST_IN_ROOM_ERROR ){
            case CLOSED: return "Block must " + ( reverse ? "not " : "" ) + "be in closed room.";
            case SIZE: return "Room is not big enough. Missing " + RestrictionHelper.LAST_IN_ROOM_ERROR_AMOUNT + " blocks.";
            case BLOCKS: if( blockString != null && amount > 0 ){ return "Room is missing " + RestrictionHelper.LAST_IN_ROOM_ERROR_AMOUNT + " exposed " + blockname  + "."; }
        }
        return "Block must " + ( reverse ? "not " : "" ) + "be in closed room" + ( size > 0 ? " of at least " + size + " blocks in interior size" : "" ) + ( amount > 0  ? ( " and at least " + amount + " exposed " + blockname + "" ) : "" ) + ".";
    }
}
