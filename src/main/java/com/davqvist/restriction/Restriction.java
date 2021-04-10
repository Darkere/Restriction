package com.davqvist.restriction;

import com.davqvist.restriction.RestrictionTypes.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(Restriction.MOD_ID)
public class Restriction {

    public static final String MOD_ID = "restriction";
    public static final Logger LOGGER = LogManager.getLogger();

    public Restriction() {
        //FMLJavaModLoadingContext.get().getModEventBus().register(this);
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        MinecraftForge.EVENT_BUS.addListener(RestrictionReader::addReloadListener);

        RestrictionManager.INSTANCE.registerRestrictionType(DimensionRestriction.ID, DimensionRestriction::new);
        RestrictionManager.INSTANCE.registerRestrictionType(HeightRestriction.ID, HeightRestriction::new);
        RestrictionManager.INSTANCE.registerRestrictionType(LevelRestriction.ID, LevelRestriction::new);
        RestrictionManager.INSTANCE.registerRestrictionType(SeeSkyRestriction.ID, SeeSkyRestriction::new);
        if(ModList.get().isLoaded("gamestages")){
            RestrictionManager.INSTANCE.registerRestrictionType(GameStageRestriction.ID, GameStageRestriction::new);
        }

        RestrictionManager.INSTANCE.registerRestrictionType(TimeRestriction.ID, TimeRestriction::new);
        RestrictionManager.INSTANCE.registerRestrictionType(OrRestriction.ID, OrRestriction::new);
        RestrictionManager.INSTANCE.registerRestrictionType(AndRestriction.ID, AndRestriction::new);
    }

    public enum Applicator {
        TAG, NAME, MOD
    }

//    @SubscribeEvent
//    public void init(FMLCommonSetupEvent event) {
//        RestrictionReader rr = new RestrictionReader();
//        File file = new File(FMLPaths.CONFIGDIR.get().toFile(), "notatest.json");
//        rr.createTestConfig(file);
//    }
}
