package com.davqvist.restriction.utility;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class RestrictionNotifications{

    public static String getNotificationSeeSky( boolean reverse ){
        return "Block must " + ( reverse ? "not " : "" ) + "see the sky.";
    }

    public static String getNotificationClosedRoom( boolean reverse, int size, String blockString, boolean ignoreMeta, int meta, int amount ){
        String blockname = UtilityHelper.getBlockName( blockString, ignoreMeta, meta );
        switch( RestrictionHelper.LAST_IN_ROOM_ERROR ){
            case CLOSED: return "Block must " + ( reverse ? "not " : "" ) + "be in closed room.";
            case SIZE: return "Room is not big enough. Missing " + RestrictionHelper.LAST_IN_ROOM_ERROR_AMOUNT + " blocks.";
            case BLOCKS: if( blockString != null && amount > 0 ){ return "Room is missing " + RestrictionHelper.LAST_IN_ROOM_ERROR_AMOUNT + " exposed " + blockname  + " blocks."; }
        }
        return "Block must " + ( reverse ? "not " : "" ) + "be in closed room" + ( size > 0 ? " of at least " + size + " blocks in interior size" : "" ) + ( ( blockString != null && amount > 0 ) ? ( " and at least " + amount + " exposed " + blockname + "" ) : "" ) + ".";
    }

    public static String getNotificationClosedRoom( boolean reverse, int size, String blockString, boolean ignoreMeta, int meta, int amount, boolean tooltip ){
        if( !tooltip ){ return getNotificationClosedRoom( reverse, size, blockString, ignoreMeta, meta, amount ); }
        String blockname = UtilityHelper.getBlockName( blockString, ignoreMeta, meta );
        return "Block must " + ( reverse ? "not " : "" ) + "be in closed room" + ( size > 0 ? " of at least " + size + " blocks in interior size" : "" ) + ( ( blockString != null && amount > 0 ) ? ( " and at least " + amount + " exposed " + blockname + "" ) : "" ) + ".";
    }

    public static String getNotificationDimension( boolean reverse, String id ){
        return "Block must " + ( reverse ? "not " : "" ) + "be in dimension " + id ;
    }

    public static String getNotificationNearbyBlocks( boolean reverse, int size, String blockString, boolean ignoreMeta, int meta, int amount ){
        String blockname = UtilityHelper.getBlockName( blockString, ignoreMeta, meta );
        return "Block must " + ( reverse ? "not " : "" ) + "be surrounded by at least " + amount + " " + blockname + " in a range of " + Math.min( 5, size ) + " blocks.";
    }

    public static String getNotificationExperience( boolean reverse, int amount ){
        return "You must " + ( reverse ? "not " : "" ) + "have at least " + amount + " levels of experience.";
    }

    public static String getNotificationMinHeight( boolean reverse, int amount ){
        return "Block must be at a " + ( reverse ? "maximum" : "minimum" ) + " height of " + amount;
    }
}
