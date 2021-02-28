package com.davqvist.restriction.utility;


import com.davqvist.restriction.config.RestrictionReader;

public class RestrictionNotifications{

    public static String getNotificationClosedRoom( RestrictionReader.RestrictionDescriptor desc){

    }

    public static String getNotificationClosedRoom(RestrictionReader.RestrictionDescriptor desc, boolean tooltip ){
        boolean reverse = desc.getIsReversed();
        int size = desc.getAmount();
        int amount = desc.block.getCount();
        if( !tooltip ){ return getNotificationClosedRoom( desc ); }
        String blockname = getBlockName(desc);
        String rev = reverse ? "not " : "";
        String sizeString = size > 0 ? " of at least " + size + " blocks in interior size" : "";
        return "Block must " + rev + "be in closed room" + sizeString + ( amount > 0  ? ( " and at least " + amount + " exposed " + blockname + "" ) : "" ) + ".";
    }

}
