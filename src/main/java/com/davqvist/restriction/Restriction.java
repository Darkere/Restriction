package com.davqvist.restriction;

import com.davqvist.restriction.config.RestrictionReader;
import com.davqvist.restriction.handler.PlaceHandler;
import com.davqvist.restriction.handler.RightClickHandler;
import com.davqvist.restriction.handler.TooltipHandler;
import com.davqvist.restriction.reference.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

@Mod(Reference.MOD_ID)
public class Restriction {

    public static RestrictionReader rr = new RestrictionReader();
    public static File configdir = FMLPaths.CONFIGDIR.get().resolve("restriction").toFile();

    public Restriction() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        MinecraftForge.EVENT_BUS.register(new RightClickHandler());
        MinecraftForge.EVENT_BUS.register(new PlaceHandler());
        MinecraftForge.EVENT_BUS.addListener(RestrictionReader::reload);
    }

    @SubscribeEvent
    public void init(FMLCommonSetupEvent event) {
        if (!configdir.exists()) {
            configdir.mkdir();
            File file = new File(configdir, "example.json");
            if(!file.exists()) {
                rr.createTestConfig(file);
            }
        }
    }

    @SubscribeEvent
    public void client(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new TooltipHandler());
    }
}
