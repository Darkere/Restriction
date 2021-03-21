package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.RestrictionReader;
import com.davqvist.restriction.utility.MessageHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TimeRestriction extends RestrictionType{
    public static final String ID = "time";

    public TimeRestriction(RestrictionReader.Descriptor descriptor) {
        super(descriptor);
    }

    @Override
    public boolean test(World world, BlockPos pos, PlayerEntity player) {
        return world.getDayTime() > descriptor.getAmount() == descriptor.isReversed();
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public String getMessage() {
        return MessageHelper.getBlockItem(descriptor)  + " can only be used " + (descriptor.isReversed() ? "before" : "after") + " " + getTimeAsString(descriptor.getAmount());
    }
    private String getTimeAsString(long time){
        long hourtime = time + 6000 % 24000;
        long hour = (int)hourtime / 1000;
        String hourstring = String.valueOf(hour);
        int  minute = (int)(((float)(hourtime - (hour*1000))) / 1000f * 60f);
        return hourstring + ":" + minute;
    }
}
