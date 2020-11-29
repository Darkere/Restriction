package com.davqvist.restriction.utility;


import com.davqvist.restriction.config.RestrictionReader;

public class RestrictionNotifications{

    public static String getNotificationSeeSky( boolean reverse ){
        return "Block must " + ( reverse ? "not " : "" ) + "see the sky.";
    }

    public static String getNotificationClosedRoom( RestrictionReader.RestrictionDescriptor desc){
        boolean reverse = desc.getReverse();
        int size = desc.getAmount();
        String blockString =  desc.block.name;
        int amount = desc.block.getCount();
        String blockname = getBlockName(desc);
        switch( RestrictionHelper.LAST_IN_ROOM_ERROR ){
            case CLOSED: return "Block must " + ( reverse ? "not " : "" ) + "be in closed room.";
            case SIZE: return "Room is not big enough. Missing " + RestrictionHelper.LAST_IN_ROOM_ERROR_AMOUNT + " blocks.";
            case BLOCKS: if( blockString != null && amount > 0 ){ return "Room is missing " + RestrictionHelper.LAST_IN_ROOM_ERROR_AMOUNT + " exposed " + blockname  + "."; }
        }
        return "Block must " + ( reverse ? "not " : "" ) + "be in closed room" + ( size > 0 ? " of at least " + size + " blocks in interior size" : "" ) + ( amount > 0  ? ( " and at least " + amount + " exposed " + blockname + "" ) : "" ) + ".";
    }

    public static String getNotificationClosedRoom(RestrictionReader.RestrictionDescriptor desc, boolean tooltip ){
        boolean reverse = desc.getReverse();
        int size = desc.getAmount();
        int amount = desc.block.getCount();
        if( !tooltip ){ return getNotificationClosedRoom( desc ); }
        String blockname = getBlockName(desc);
        String rev = reverse ? "not " : "";
        String sizeString = size > 0 ? " of at least " + size + " blocks in interior size" : "";
        return "Block must " + rev + "be in closed room" + sizeString + ( amount > 0  ? ( " and at least " + amount + " exposed " + blockname + "" ) : "" ) + ".";
    }

    public static String getNotificationDimension( boolean reverse, String id ){
        return "Block must " + ( reverse ? "not " : "" ) + "be in dimension " + id ;
    }

    public static String getNotificationNearbyBlocks(RestrictionReader.RestrictionDescriptor desc){
        boolean reverse = desc.getReverse();
        int size = desc.getAmount();
        int amount = desc.block.getCount();
        String blockname = getBlockName(desc);
        return "Block must " + ( reverse ? "not " : "" ) + "be surrounded by at least " + amount + " " + blockname + " in a range of " + Math.min( 5, size ) + " blocks.";
    }

    public static String getNotificationExperience( boolean reverse, int amount ){
        return "You must " + ( reverse ? "not " : "" ) + "have at least " + amount + " levels of experience.";
    }

    public static String getNotificationMinHeight( boolean reverse, int amount ){
        return "Block must be at a " + ( reverse ? "maximum" : "minimum" ) + " height of " + amount;
    }

    private static String getBlockName(RestrictionReader.RestrictionDescriptor desc){
        return desc.block.isTag() ? "Blocks in tag #" + desc.block.name : UtilityHelper.getBlockName( desc.block.name );
    }
}
