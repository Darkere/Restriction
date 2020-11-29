package com.davqvist.restriction.handler;

import com.davqvist.restriction.Restriction;
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
            for (RestrictionReader.RestrictionSection rBlock : Restriction.rr.root.entries) {
                if (UtilityHelper.matches(world, rBlock.block, state.getBlock())) {
                    for (RestrictionReader.RestrictionDescriptor desc : rBlock.restrictions) {
                        if (desc.type == RestrictionReader.RestrictionType.SEESKY) {
                            if (RestrictionHelper.canSeeSky(pos, world) == desc.getReverse()) {
                                cancelRightClick(event, RestrictionNotifications.getNotificationSeeSky(desc.getReverse()));
                            }
                        }
                        if (desc.type == RestrictionReader.RestrictionType.CLOSEDROOM) {
                            if (RestrictionHelper.isInRoom(pos, world, desc)== desc.getReverse()) {
                                cancelRightClick(event, RestrictionNotifications.getNotificationClosedRoom(desc, false));
                            }
                        }
                        if (desc.type == RestrictionReader.RestrictionType.DIMENSION) {
                            if (RestrictionHelper.isInDimension(world, desc.dimension) == desc.getReverse()) {
                                cancelRightClick(event, RestrictionNotifications.getNotificationDimension(desc.getReverse(), desc.dimension));
                            }
                        }
                        if (desc.type == RestrictionReader.RestrictionType.NEARBYBLOCKS) {
                            if (RestrictionHelper.isNearby(pos, world, desc)== desc.getReverse()) {
                                cancelRightClick(event, RestrictionNotifications.getNotificationNearbyBlocks(desc));
                            }
                        }
                        if (desc.type == RestrictionReader.RestrictionType.EXPERIENCE) {
                            if (RestrictionHelper.hasLevels(player, desc.getAmount()) == desc.getReverse()) {
                                cancelRightClick(event, RestrictionNotifications.getNotificationExperience(desc.getReverse(), desc.getAmount()));
                            }
                        }
                        if (desc.type == RestrictionReader.RestrictionType.MINHEIGHT) {
                            if (RestrictionHelper.hasMinHeight(pos, desc.getAmount()) == desc.getReverse()) {
                                cancelRightClick(event, RestrictionNotifications.getNotificationMinHeight(desc.getReverse(), desc.getAmount()));
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
