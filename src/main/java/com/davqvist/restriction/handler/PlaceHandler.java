package com.davqvist.restriction.handler;

import com.davqvist.restriction.Restriction;
import com.davqvist.restriction.config.RestrictionReader;
import com.davqvist.restriction.utility.RestrictionHelper;
import com.davqvist.restriction.utility.RestrictionNotifications;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class PlaceHandler{

    @SubscribeEvent
    public void onPlace( BlockEvent.EntityPlaceEvent event ){
        if(!(event.getEntity() instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity)event.getEntity();
        BlockPos pos = event.getPos();
        IWorld iWorld = event.getWorld();
        String message = "";
        if(!player.isCreative() && !player.world.isRemote && !event.isCanceled() && pos != null ){
            BlockState state = event.getPlacedBlock();
            ServerWorld world = (ServerWorld)iWorld;
            if( state != null ){
                for( RestrictionReader.RestrictionBlock block : Restriction.rr.root.entries ){
                    if(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block.block)).equals(state.getBlock())){
                        for( RestrictionReader.RestrictionDesciptor desc : block.restrictions ){
                            if( desc.type == RestrictionReader.RestrictionType.SEESKY ){
                                if( RestrictionHelper.canSeeSky( pos, world ) == desc.reverse ){
                                    event.setCanceled( true );
                                    message = RestrictionNotifications.getNotificationSeeSky( desc.reverse );
                                }
                            }
                            if( desc.type == RestrictionReader.RestrictionType.CLOSEDROOM ){
                                if( RestrictionHelper.isInRoom( pos, world, ( desc.size == null ? 0 : desc.size ), ( desc.block == null ? "" : desc.block ), desc.ignoreMeta, ( desc.meta == null ? 0 : desc.meta ), ( desc.amount == null ? 0 : desc.amount ) ) == desc.reverse ){
                                    event.setCanceled( true );
                                    message = RestrictionNotifications.getNotificationClosedRoom( desc.reverse, ( desc.size == null ? 0 : desc.size ), ( desc.block == null ? "" : desc.block ), desc.ignoreMeta, ( desc.meta == null ? 0 : desc.meta ), ( desc.amount == null ? 0 : desc.amount ) );
                                }
                            }
                            if( desc.type == RestrictionReader.RestrictionType.DIMENSION ){
                                if( RestrictionHelper.isInDimension( world, desc.id ) == desc.reverse ){
                                    event.setCanceled( true );
                                    message = RestrictionNotifications.getNotificationDimension( desc.reverse, desc.id );
                                }
                            }
                            if( desc.type == RestrictionReader.RestrictionType.NEARBYBLOCKS ){
                                if( RestrictionHelper.isNearby( pos, world, ( desc.size == null ? 0 : desc.size ), ( desc.block == null ? "" : desc.block ), desc.ignoreMeta, ( desc.meta == null ? 0 : desc.meta ), ( desc.amount == null ? 0 : desc.amount ) ) == desc.reverse ){
                                    event.setCanceled( true );
                                    message = RestrictionNotifications.getNotificationNearbyBlocks( desc.reverse, ( desc.size == null ? 0 : desc.size ), ( desc.block == null ? "" : desc.block ), desc.ignoreMeta, ( desc.meta == null ? 0 : desc.meta ), ( desc.amount == null ? 0 : desc.amount ) );
                                }
                            }
                            if( desc.type == RestrictionReader.RestrictionType.EXPERIENCE ){
                                if( RestrictionHelper.hasLevels( player, desc.amount ) == desc.reverse ){
                                    event.setCanceled( true );
                                    message = RestrictionNotifications.getNotificationExperience( desc.reverse, ( desc.amount == null ? 0 : desc.amount ) );
                                }
                            }
                            if( desc.type == RestrictionReader.RestrictionType.MINHEIGHT ){
                                if( RestrictionHelper.hasMinHeight( pos, desc.amount ) == desc.reverse ){
                                    event.setCanceled( true );
                                    message = RestrictionNotifications.getNotificationMinHeight( desc.reverse, ( desc.amount == null ? 0 : desc.amount ) );
                                }
                            }
                        }
                    }
                }
            }
        }
        if( event.isCanceled() ){
            player.sendStatusMessage( new StringTextComponent( message ), true );
        }
    }

    //private void cancelPlace( BlockEvent.PlaceEvent event ){
  //      event.setCanceled( true );
    //}
}
