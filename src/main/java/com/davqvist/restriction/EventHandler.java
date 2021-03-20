package com.davqvist.restriction;

import com.davqvist.restriction.utility.RestrictionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent e) {
        if (e.getPlayer() == null) return;
        RestrictionManager.INSTANCE.getMessages(e.getItemStack().getItem().getRegistryName()).forEach(txt -> e.getToolTip().add(txt));
    }

    @SubscribeEvent //this event is only called serverside in 1.16
    public void onPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof PlayerEntity) || event.getEntity() instanceof FakePlayer) return;
        if (event.getWorld().isRemote()) return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
        BlockPos pos = event.getPos();
        ServerWorld world = (ServerWorld) event.getWorld();
        if (!player.isCreative() && !event.isCanceled() && pos != null) {
            if (RestrictionManager.INSTANCE.testBlockRestriction(world, pos, player, event.getPlacedBlock().getBlock().getRegistryName())) {
                event.setCanceled(true);
                ItemStack stack = player.getHeldItemMainhand();
                player.sendSlotContents(player.container, player.inventory.currentItem + 36, stack);
            }
        }
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickItem event) {
        PlayerEntity player = event.getPlayer();
        if (event.getWorld().isRemote) return;
        World world = event.getWorld();
        if (player != null && !player.isCreative() && !event.isCanceled()) {
            if (RestrictionManager.INSTANCE.testItemRestriction(world, event.getPos(), player, event.getItemStack().getItem().getRegistryName())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        if (event.getWorld().isRemote) return;
        World world = event.getWorld();
        if (player != null && !player.isCreative() && !event.isCanceled()) {
            if (RestrictionManager.INSTANCE.testItemRestriction(world, event.getPos(), player, event.getItemStack().getItem().getRegistryName())) {
                event.setCanceled(true);
            }
        }
    }
}


