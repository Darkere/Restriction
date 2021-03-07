package com.davqvist.restriction.handler;

import com.davqvist.restriction.Restriction;
import com.davqvist.restriction.config.RestrictionReader;
import com.davqvist.restriction.utility.RestrictionNotifications;
import com.davqvist.restriction.utility.UtilityHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class TooltipHandler {

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent e) {
        if (e.getPlayer() == null) return;
        for (RestrictionReader.RestrictionRoot root : RestrictionReader.roots) {
            for (RestrictionReader.RestrictionSection rblock : root.entries) {
                if (UtilityHelper.matches(e.getPlayer().getEntityWorld(), rblock.block, e.getItemStack().getItem())) {
                    e.getToolTip().addAll(getNotifications(rblock));
                }
            }
        }
    }

    private List<ITextComponent> getNotifications(RestrictionReader.RestrictionSection rblock) {

        List<ITextComponent> notifications = new ArrayList<>();

        for (RestrictionReader.RestrictionDescriptor desc : rblock.restrictions) {
            String notification = null;
            if (desc.type == Restriction.Type.SEESKY) {
                notification = RestrictionNotifications.getNotificationSeeSky(desc.getIsReversed());
            }
            if (desc.type == Restriction.Type.CLOSEDROOM) {
                notification = RestrictionNotifications.getNotificationClosedRoom(desc, true);
            }
            if (desc.type == Restriction.Type.DIMENSION) {
                notification = RestrictionNotifications.getNotificationDimension(desc.getIsReversed(), desc.dimension);
            }
            if (desc.type == Restriction.Type.NEARBYBLOCKS) {
                notification = RestrictionNotifications.getNotificationNearbyBlocks(desc);
            }
            if (desc.type == Restriction.Type.EXPERIENCE) {
                notification = RestrictionNotifications.getNotificationExperience(desc.getIsReversed(), desc.getAmount());
            }
            if (desc.type == Restriction.Type.MINHEIGHT) {
                notification = RestrictionNotifications.getNotificationMinHeight(desc.getIsReversed(), desc.getAmount());
            }
            if (notification != null) {
                notifications.add(new StringTextComponent("Restriction: ").mergeStyle(TextFormatting.RED).append(new StringTextComponent(notification).mergeStyle(TextFormatting.WHITE)));
            }
        }

        return notifications;

    }
}
