package com.davqvist.restriction.handler;

import com.davqvist.restriction.Restriction;
import com.davqvist.restriction.config.RestrictionReader;
import com.davqvist.restriction.proxy.CommonProxy;
import com.davqvist.restriction.utility.RestrictionNotifications;
import com.davqvist.restriction.utility.UtilityHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class TooltipHandler{

    @SubscribeEvent
    public void onTooltip( ItemTooltipEvent e ){
        for( RestrictionReader.RestrictionBlock rblock : Restriction.proxy.rr.root.entries ){
            Object block = UtilityHelper.getBlock( rblock.block, rblock.ignoreMeta, rblock.meta );
            try{
                if( e.getItemStack() != null && e.getItemStack().getItem() != null ){
                    if( ( block instanceof IBlockState && Block.getBlockFromItem( e.getItemStack().getItem() ).getStateFromMeta( rblock.meta ) == block ) || ( block instanceof Block && ( ( e.getItemStack().getItem() instanceof ItemBlock && Block.isEqualTo( Block.getBlockFromItem( e.getItemStack().getItem() ), (Block) block ) ) || ( e.getItemStack().getItem() instanceof ItemBlockSpecial && Block.isEqualTo( ( (ItemBlockSpecial) e.getItemStack().getItem() ).getBlock(), (Block) block ) ) ) ) ){
                        e.getToolTip().addAll( getNotifications( rblock ) );
                    }
                }
            } catch( Exception ex ){
            }
        }
    }

    private List<String> getNotifications( RestrictionReader.RestrictionBlock rblock ){

        List<String> notifications = new ArrayList<>();

        for( RestrictionReader.RestrictionDesciptor rdesc : rblock.restrictions ){
            String notification = null;
            if( rdesc.type == RestrictionReader.RestrictionType.SEESKY ){
                notification = RestrictionNotifications.getNotificationSeeSky( rdesc.reverse );
            }
            if( rdesc.type == RestrictionReader.RestrictionType.CLOSEDROOM ){
                notification = RestrictionNotifications.getNotificationClosedRoom( rdesc.reverse, ( rdesc.size == null ? 0 : rdesc.size ), rdesc.block, rdesc.ignoreMeta, ( rdesc.meta == null ? 0 : rdesc.meta ), ( rdesc.amount == null ? 0 : rdesc.amount ), true );
            }
            if( rdesc.type == RestrictionReader.RestrictionType.DIMENSION ){
                notification = RestrictionNotifications.getNotificationDimension( rdesc.reverse, rdesc.id );
            }
            if( rdesc.type == RestrictionReader.RestrictionType.NEARBYBLOCKS ){
                notification = RestrictionNotifications.getNotificationNearbyBlocks( rdesc.reverse, ( rdesc.size == null ? 0 : rdesc.size ), rdesc.block, rdesc.ignoreMeta, ( rdesc.meta == null ? 0 : rdesc.meta ), ( rdesc.amount == null ? 0 : rdesc.amount ) );
            }
            if( rdesc.<>type == RestrictionReader.RestrictionType.EXPERIENCE ){
                notification = RestrictionNotifications.getNotificationExperience( rdesc.reverse, rdesc.amount );
            }
            if( rdesc.type == RestrictionReader.RestrictionType.MINHEIGHT ){
                notification = RestrictionNotifications.getNotificationMinHeight( rdesc.reverse, rdesc.amount );
            }
            if( notification != null ){
                notifications.add( "§4Restriction: §r" + notification );
            }
        }

        return notifications;

    }
}
