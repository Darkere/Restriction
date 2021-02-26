package com.davqvist.restriction.handler;

import com.davqvist.restriction.config.RestrictionReader;
import com.davqvist.restriction.utility.RestrictionHelper;
import com.davqvist.restriction.utility.RestrictionNotifications;
import com.davqvist.restriction.utility.UtilityHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RightClickHandler {

    @SubscribeEvent
    public void onRightClick(RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        BlockPos pos = event.getPos();
        if (event.getWorld().isRemote) return;
        World world = event.getWorld();
        if (player != null && !player.isCreative() && !event.isCanceled()) {
            BlockState state = world.getBlockState(event.getPos());
            for (RestrictionReader.RestrictionRoot root : RestrictionReader.roots) {
                for (RestrictionReader.RestrictionSection rBlock : root.entries) {
                    if (UtilityHelper.matches(world, rBlock.block, state.getBlock())) {
                        for (RestrictionReader.RestrictionDescriptor desc : rBlock.restrictions) {
                            if (desc.type == RestrictionReader.RestrictionOptions.SEESKY) {
                                if (RestrictionHelper.canSeeSky(pos, world) == desc.getIsReversed()) {
                                    cancelRightClick(event, RestrictionNotifications.getNotificationSeeSky(desc.getIsReversed()));
                                }
                            }
                            if (desc.type == RestrictionReader.RestrictionOptions.CLOSEDROOM) {
                                if (RestrictionHelper.isInRoom(pos, world, desc) == desc.getIsReversed()) {
                                    cancelRightClick(event, RestrictionNotifications.getNotificationClosedRoom(desc, false));
                                }
                            }
                            if (desc.type == RestrictionReader.RestrictionOptions.DIMENSION) {
                                if (RestrictionHelper.isInDimension(world, desc.dimension) == desc.getIsReversed()) {
                                    cancelRightClick(event, RestrictionNotifications.getNotificationDimension(desc.getIsReversed(), desc.dimension));
                                }
                            }
                            if (desc.type == RestrictionReader.RestrictionOptions.NEARBYBLOCKS) {
                                if (RestrictionHelper.isNearby(pos, world, desc) == desc.getIsReversed()) {
                                    cancelRightClick(event, RestrictionNotifications.getNotificationNearbyBlocks(desc));
                                }
                            }
                            if (desc.type == RestrictionReader.RestrictionOptions.EXPERIENCE) {
                                if (RestrictionHelper.hasLevels(player, desc.getAmount()) == desc.getIsReversed()) {
                                    cancelRightClick(event, RestrictionNotifications.getNotificationExperience(desc.getIsReversed(), desc.getAmount()));
                                }
                            }
                            if (desc.type == RestrictionReader.RestrictionOptions.MINHEIGHT) {
                                if (RestrictionHelper.hasMinHeight(pos, desc.getAmount()) == desc.getIsReversed()) {
                                    cancelRightClick(event, RestrictionNotifications.getNotificationMinHeight(desc.getIsReversed(), desc.getAmount()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void cancelRightClick(RightClickBlock event, String message) {
        event.setCanceled(true);
        event.getPlayer().sendStatusMessage(new StringTextComponent(message), true);
    }
}
