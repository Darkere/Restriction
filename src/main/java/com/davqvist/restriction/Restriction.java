package com.davqvist.restriction;

import com.davqvist.restriction.RestrictionTypes.*;
import com.davqvist.restriction.utility.Network;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.nio.ch.Net;

@Mod(Restriction.MOD_ID)
public class Restriction {

    public static final String MOD_ID = "restriction";
    public static final Logger LOGGER = LogManager.getLogger();
    public static boolean isDedicatedServer = false;

    public Restriction() {

        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        //MinecraftForge.EVENT_BUS.register(new EventHandler());
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()-> ()->MinecraftForge.EVENT_BUS.register(new ClientEventHandler()));
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        MinecraftForge.EVENT_BUS.addListener(RestrictionReader::addReloadListener);
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, ()->()->{
            isDedicatedServer = true;
            MinecraftForge.EVENT_BUS.addListener(this::join);
        });

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
    @SubscribeEvent
    public void common(FMLCommonSetupEvent event) {
        Network.register();
    }

    public void join(PlayerEvent.PlayerLoggedInEvent e){
        Network.sendToPlayer((ServerPlayerEntity) e.getPlayer(),RestrictionManager.INSTANCE.LogInMessage);

    }
//    @SubscribeEvent
//    public void init(FMLCommonSetupEvent event) {
//        RestrictionReader rr = new RestrictionReader();
//        File file = new File(FMLPaths.CONFIGDIR.get().toFile(), "datapacktest.json");
//        rr.createTestConfig(file);
//    }
}
