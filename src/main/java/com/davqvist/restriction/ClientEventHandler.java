package com.davqvist.restriction;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEventHandler {


    public static void loadClientRestrictions() {
        RestrictionManager.INSTANCE.loadRestrictions(Minecraft.getInstance().world);
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent e) {
        if (e.getPlayer() == null) return;
        RestrictionManager.INSTANCE.getMessages(e.getItemStack().getItem()).forEach(txt -> e.getToolTip().add(txt));
    }
}
