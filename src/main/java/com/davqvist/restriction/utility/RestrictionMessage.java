package com.davqvist.restriction.utility;

import com.davqvist.restriction.ClientEventHandler;
import com.davqvist.restriction.RestrictionManager;
import com.davqvist.restriction.RestrictionReader;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RestrictionMessage {

    List<JsonElement> json;
    public RestrictionMessage(List<JsonElement> json) {
        this.json = json;
    }


    public static void encode(RestrictionMessage data, PacketBuffer buf) {
        buf.writeInt(data.json.size());
        for (JsonElement jsonElement : data.json) {
            buf.writeString(jsonElement.toString());
        }
    }


    public static RestrictionMessage decode(PacketBuffer buf) {
        int len = buf.readInt();
        List<JsonElement> list = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            list.add(new JsonParser().parse(buf.readString()));
        }
        return new RestrictionMessage(list);
    }

    public static boolean handle(RestrictionMessage data, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Gson gson = new Gson();
            RestrictionManager.INSTANCE.init();
            for (JsonElement jsonElement : data.json) {
                RestrictionReader.readRestrictions(jsonElement,gson).forEach(RestrictionManager.INSTANCE::addRestriction);
            }
            ClientEventHandler.loadClientRestrictions();
        });

        return true;
    }
}
