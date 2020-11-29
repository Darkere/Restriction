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
        for (RestrictionReader.RestrictionSection rblock : Restriction.rr.root.entries) {
            if (UtilityHelper.matches(e.getPlayer().getEntityWorld(), rblock.block, e.getItemStack().getItem())) {
                e.getToolTip().addAll(getNotifications(rblock));
            }
        }
    }

    private List<ITextComponent> getNotifications(RestrictionReader.RestrictionSection rblock) {

        List<ITextComponent> notifications = new ArrayList<>();

        for (RestrictionReader.RestrictionDescriptor desc : rblock.restrictions) {
            String notification = null;
            if (desc.type == RestrictionReader.RestrictionType.SEESKY) {
                notification = RestrictionNotifications.getNotificationSeeSky(desc.getReverse());
            }
            if (desc.type == RestrictionReader.RestrictionType.CLOSEDROOM) {
                notification = RestrictionNotifications.getNotificationClosedRoom(desc, true);
            }
            if (desc.type == RestrictionReader.RestrictionType.DIMENSION) {
                notification = RestrictionNotifications.getNotificationDimension(desc.getReverse(), desc.dimension);
            }
            if (desc.type == RestrictionReader.RestrictionType.NEARBYBLOCKS) {
                notification = RestrictionNotifications.getNotificationNearbyBlocks(desc);
            }
            if (desc.type == RestrictionReader.RestrictionType.EXPERIENCE) {
                notification = RestrictionNotifications.getNotificationExperience(desc.getReverse(), desc.getAmount());
            }
            if (desc.type == RestrictionReader.RestrictionType.MINHEIGHT) {
                notification = RestrictionNotifications.getNotificationMinHeight(desc.getReverse(), desc.getAmount());
            }
            if (notification != null) {
                notifications.add(new StringTextComponent("Restriction: ").mergeStyle(TextFormatting.RED).append(new StringTextComponent(notification).mergeStyle(TextFormatting.WHITE)));
            }
        }

        return notifications;

    }
}
