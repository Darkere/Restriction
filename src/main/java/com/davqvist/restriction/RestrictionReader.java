package com.davqvist.restriction;

import com.davqvist.restriction.utility.Network;
import com.davqvist.restriction.utility.RestrictionMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RestrictionReader {

    @SubscribeEvent
    public static void addReloadListener(AddReloadListenerEvent event) {
       event.addListener(new ReloadListener(new Gson(),"restrictions"));
    }

    public static List<Descriptor> readRestrictions(JsonElement js, Gson gson) {
        List<Descriptor> root = null;
        try {
            root = gson.fromJson(js, new TypeToken<List<Descriptor>>(){}.getType());

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        if(root == null )return Collections.emptyList();
        return root;
    }

    public void createTestConfig(File file) {
        List<Descriptor> restrictions = new ArrayList<>();
        Descriptor desc = new Descriptor();
        restrictions.add(desc);
        desc.type = "seesky";
        desc.reverse = true;
        desc.block = true;
        desc.mod = "minecraft";

        desc = new Descriptor();
        restrictions.add(desc);
        desc.tag = "minecraft:beds";
        desc.type = "or";
        desc.block = true;
        desc.first = new Descriptor();
        desc.first.type = "dimension";
        desc.first.dimension = "minecraft:overworld";
        desc.first.reverse = true;
        desc.second = new Descriptor();
        desc.second.type = "gamestage";
        desc.second.stage = "rest";


        desc = new Descriptor();
        desc.name = "minecraft:stone";
        desc.block = true;
        desc.type = "level";
        desc.amount = 10;
        restrictions.add(desc);

        desc = new Descriptor();
        desc.name = "minecraft:bread";
        desc.item = true;
        desc.type = "dimension";
        desc.dimension = "minecraft:overworld";
        desc.reverse = true;
        restrictions.add(desc);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(restrictions);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Descriptor {
        public String type;
        private Integer amount;
        private Boolean reverse;
        public String dimension;
        public String mod;
        public String name;
        public String tag;
        private Boolean item;
        private Boolean block;
        public String stage;
        public Descriptor first;
        public Descriptor second;


        public boolean isReversed() {
            return reverse != null && reverse;
        }

        public int getAmount() {
            return amount == null ? 1 : amount;
        }


        public boolean isItemRestriction(){
            return item != null && item;
        }
        public boolean isBlockRestriction(){
            return block != null && block;
        }

        public Restriction.Applicator getApplicator(){
            if(mod != null) return Restriction.Applicator.MOD;
            if(tag != null) return Restriction.Applicator.TAG;
            if(name != null) return Restriction.Applicator.NAME;
            return null;
        }

        public String getApplicatorString(){
            switch (getApplicator()){
                case NAME: return name;
                case TAG: return tag;
                case MOD: return mod;
            }
            return "";
        }
    }

    public static class ReloadListener extends JsonReloadListener {
        Gson gson;

        public ReloadListener(Gson p_i51536_1_, String p_i51536_2_) {
            super(p_i51536_1_, p_i51536_2_);
            gson = p_i51536_1_;
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
            RestrictionManager.INSTANCE.init();
            objectIn.forEach((rl,js)-> readRestrictions(js,gson).forEach(RestrictionManager.INSTANCE::addRestriction));
            List<JsonElement> list = new ArrayList<>();
            objectIn.forEach((rl,js)-> list.add(js));
            RestrictionManager.INSTANCE.LogInMessage = new RestrictionMessage(list);
            if(ServerLifecycleHooks.getCurrentServer() != null && ServerLifecycleHooks.getCurrentServer().isDedicatedServer()){
                Network.sendToAll(RestrictionManager.INSTANCE.LogInMessage);
            }
        }

        @Override
        protected Map<ResourceLocation, JsonElement> prepare(IResourceManager resourceManagerIn, IProfiler profilerIn) {
            return super.prepare(resourceManagerIn, profilerIn);
        }
    }
}
