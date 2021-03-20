package com.davqvist.restriction;

import com.davqvist.restriction.RestrictionTypes.*;
import com.davqvist.restriction.utility.RestrictionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Restriction.MOD_ID)
public class Restriction {

    public static final String MOD_ID = "restriction";

    public Restriction() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        MinecraftForge.EVENT_BUS.addListener(RestrictionReader::addReloadListener);

        RestrictionManager.INSTANCE.registerRestrictionType(ClosedRoomRestriction.ID,ClosedRoomRestriction::new);
        RestrictionManager.INSTANCE.registerRestrictionType(DimensionRestriction.ID,DimensionRestriction::new);
        RestrictionManager.INSTANCE.registerRestrictionType(HeightRestriction.ID,HeightRestriction::new);
        RestrictionManager.INSTANCE.registerRestrictionType(LevelRestriction.ID,LevelRestriction::new);
        RestrictionManager.INSTANCE.registerRestrictionType(NearbyRestriction.ID,NearbyRestriction::new);
        RestrictionManager.INSTANCE.registerRestrictionType(SeeSkyRestriction.ID,SeeSkyRestriction::new);
    }

    @SubscribeEvent
    public void client(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    public enum Applicator {
        TAG, NAME, MOD
    }
}
