package com.davqvist.restriction.utility;

import com.davqvist.restriction.Restriction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Network {


    public static  SimpleChannel INSTANCE;
    private static final ResourceLocation CHANNELID = new ResourceLocation(Restriction.MOD_ID,"network");

    public static void sendToAll(Object Message){
        Network.INSTANCE.send(PacketDistributor.ALL.noArg(),Message);
    }

    public static void register() {
        INSTANCE = NetworkRegistry.newSimpleChannel(CHANNELID, () -> "1", s -> true, s -> true);

        INSTANCE.registerMessage(1, RestrictionMessage.class,RestrictionMessage::encode,RestrictionMessage::decode,RestrictionMessage::handle);
    }
}
