package com.davqvist.restriction.handler;

import com.davqvist.restriction.Restriction;
import com.davqvist.restriction.config.RestrictionReader;
import com.davqvist.restriction.utility.RestrictionHelper;
import com.davqvist.restriction.utility.RestrictionNotifications;
import com.davqvist.restriction.utility.UtilityHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlaceHandler {

    @SubscribeEvent //this event is only called serverside in 1.16
    public void onPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof PlayerEntity) || event.getEntity() instanceof FakePlayer) return;
        if (event.getWorld().isRemote()) return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
        BlockPos pos = event.getPos();
        ServerWorld world = (ServerWorld) event.getWorld();
        String message = "";
        if (!player.isCreative() && !event.isCanceled() && pos != null) {
            BlockState state = event.getPlacedBlock();
            if (state != null) {
                for (RestrictionReader.RestrictionRoot root : RestrictionReader.roots) {
                    for (RestrictionReader.RestrictionSection block : root.entries) {
                        if (UtilityHelper.matches(world, block.block, state.getBlock())) {
                            for (RestrictionReader.RestrictionDescriptor desc : block.restrictions) {
                                if (desc.type == Restriction.Type.SEESKY) {
                                    if (RestrictionHelper.canSeeSky(pos, world) == desc.getIsReversed()) {
                                        event.setCanceled(true);
                                        message = RestrictionNotifications.getNotificationSeeSky(desc.getIsReversed());
                                    }
                                }
                                if (desc.type == Restriction.Type.CLOSEDROOM) {
                                    if (RestrictionHelper.isInRoom(pos, world, desc)) {
                                        event.setCanceled(true);
                                        message = RestrictionNotifications.getNotificationClosedRoom(desc, false);
                                    }
                                }
                                if (desc.type == Restriction.Type.DIMENSION) {
                                    if (RestrictionHelper.isInDimension(world, desc.dimension) == desc.getIsReversed()) {
                                        event.setCanceled(true);
                                        message = RestrictionNotifications.getNotificationDimension(desc.getIsReversed(), desc.dimension);
                                    }
                                }
                                if (desc.type == Restriction.Type.NEARBYBLOCKS) {
                                    if (RestrictionHelper.isNearby(pos, world, desc)) {
                                        event.setCanceled(true);
                                        message = RestrictionNotifications.getNotificationNearbyBlocks(desc);
                                    }
                                }
                                if (desc.type == Restriction.Type.EXPERIENCE) {
                                    if (RestrictionHelper.hasLevels(player, desc.getAmount()) == desc.getIsReversed()) {
                                        event.setCanceled(true);
                                        message = RestrictionNotifications.getNotificationExperience(desc.getIsReversed(), desc.getAmount());
                                    }
                                }
                                if (desc.type == Restriction.Type.MINHEIGHT) {
                                    if (RestrictionHelper.hasMinHeight(pos, desc.getAmount()) == desc.getIsReversed()) {
                                        event.setCanceled(true);
                                        message = RestrictionNotifications.getNotificationMinHeight(desc.getIsReversed(), desc.getAmount());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (event.isCanceled()) {
            player.sendStatusMessage(new StringTextComponent(message), true);
            ItemStack stack = player.getHeldItemMainhand();
            player.sendSlotContents(player.container, player.inventory.currentItem + 36, stack);
        }
    }
}
