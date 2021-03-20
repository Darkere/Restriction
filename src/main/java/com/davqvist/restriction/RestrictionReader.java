package com.davqvist.restriction;

import com.davqvist.restriction.Restriction;
import com.davqvist.restriction.utility.RestrictionManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RestrictionReader {


    @SubscribeEvent
    public static void addReloadListener(AddReloadListenerEvent event) {
       event.addListener(new ReloadListener(new Gson(),"restriction"));
    }

    public static List<Descriptor> readRestrictions(JsonElement js, Gson gson) {
        List<Descriptor> descriptors = new ArrayList<>();
        RestrictionOrigin origin = null;
        try {
            origin = gson.fromJson(js, RestrictionOrigin.class);

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        if(origin == null )return descriptors;
        for (Root root : origin.roots) {
            for (Section entry : root.entries) {
                descriptors.addAll(entry.restrictions);
            }
        }
        return descriptors;
    }

//    public void createTestConfig(File file) {
//        Root root = new Root();
//        roots.add(root);
//        Section rBlock = new Section();
//        BlockOrTag block = new BlockOrTag();
//        block.name = "minecraft (example disabled, use just \"modname\")";
//        block.isMod = true;
//
//        Descriptor desc = new Descriptor();
//        desc.type = Restriction.Type.SEESKY;
//        desc.reverse = true;
//        rBlock.restrictions.add(desc);
//        rBlock.block = block;
//
//        root.entries.add(rBlock);
//
//        rBlock = new Section();
//        block = new BlockOrTag();
//        block.name = "minecraft:furnace";
//        rBlock.block = block;
//
//        desc = new Descriptor();
//        desc.type = Restriction.Type.CLOSEDROOM;
//        desc.amount = 50;
//        block = new BlockOrTag();
//        block.name = "minecraft:planks";
//        block.count = 40;
//        block.isTag = true;
//        desc.block = block;
//        rBlock.restrictions.add(desc);
//
//        root.entries.add(rBlock);
//
//        rBlock = new Section();
//        block = new BlockOrTag();
//        block.name = "minecraft:brewing_stand";
//        rBlock.block = block;
//
//        desc = new Descriptor();
//        desc.type = Restriction.Type.DIMENSION;
//        desc.dimension = "minecraft:the_nether";
//        rBlock.restrictions.add(desc);
//
//        root.entries.add(rBlock);
//
//        rBlock = new Section();
//        block = new BlockOrTag();
//        block.name = "minecraft:enchanting_table";
//        rBlock.block = block;
//
//        desc = new Descriptor();
//        desc.amount = 3;
//        desc.type = Restriction.Type.NEARBYBLOCKS;
//
//
//        block = new BlockOrTag();
//        block.name = "minecraft:lapis_block";
//        block.count = 4;
//        desc.block = block;
//        rBlock.restrictions.add(desc);
//
//        root.entries.add(rBlock);
//
//        rBlock = new Section();
//        block = new BlockOrTag();
//        block.name = "minecraft:beacon";
//        rBlock.block = block;
//
//        desc = new Descriptor();
//        desc.type = Restriction.Type.EXPERIENCE;
//        desc.amount = 20;
//        rBlock.restrictions.add(desc);
//
//        root.entries.add(rBlock);
//
//        rBlock = new Section();
//        block = new BlockOrTag();
//        block.name = "minecraft:obsidian";
//        rBlock.block = block;
//
//        desc = new Descriptor();
//        desc.type = Restriction.Type.MINHEIGHT;
//        desc.amount = 16;
//        desc.reverse = true;
//        rBlock.restrictions.add(desc);
//
//        root.entries.add(rBlock);
//
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        String json = gson.toJson(new RestrictionOrigin(roots));
//        try {
//            FileWriter writer = new FileWriter(file);
//            writer.write(json);
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public class Root {
        public ArrayList<Section> entries = new ArrayList<>();
    }

    public class Section {
        public ArrayList<Descriptor> restrictions = new ArrayList<>();
    }

    public class Descriptor {
        public String type;
        private Integer amount;
        private Integer exposed;
        public String name2;
        private Boolean reverse;
        public String dimension;
        public String mod;
        public String name;
        public String tag;
        private Boolean item;
        private Boolean block;


        public boolean IsReversed() {
            return reverse != null && reverse;
        }

        public int getAmount() {
            return amount == null ? 1 : amount;
        }

        public int getExposed(){
            return exposed == null ? 0 : exposed;
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


    private static class RestrictionOrigin {
        List<Root> roots;

        public RestrictionOrigin(List<Root> roots) {
            this.roots = roots;
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
            objectIn.forEach((rl,js)->{
               readRestrictions(js,gson).forEach(RestrictionManager.INSTANCE::addRestriction);
            });
        }
    }
}
