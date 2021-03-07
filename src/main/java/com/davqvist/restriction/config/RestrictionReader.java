package com.davqvist.restriction.config;

import com.davqvist.restriction.Restriction;
import com.davqvist.restriction.utility.LogHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class RestrictionReader {

    public static List<RestrictionRoot> roots = new ArrayList<>();

    @SubscribeEvent
    public static void reload(AddReloadListenerEvent event) {
        roots.clear();
        try {
            Files.list(Restriction.configdir.toPath()).forEach(path -> Restriction.rr.readRestrictions(path.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void readRestrictions(File file) {
        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(file));
            RestrictionOrigin origin = gson.fromJson(reader, RestrictionOrigin.class);
            if (origin != null)
                roots.addAll(origin.roots);
        } catch (Exception e) {
            LogHelper.error(" The Restriction json was invalid and is ignored.");
            e.printStackTrace();
            LogHelper.error(" The Restriction json was invalid and is ignored.");
        }
    }

    public void createTestConfig(File file) {
        RestrictionRoot root = new RestrictionRoot();
        roots.add(root);
        RestrictionSection rBlock = new RestrictionSection();
        BlockOrTag block = new BlockOrTag();
        block.name = "minecraft (example disabled, use just \"modname\")";
        block.isMod = true;

        RestrictionDescriptor desc = new RestrictionDescriptor();
        desc.type = Restriction.Type.SEESKY;
        desc.reverse = true;
        rBlock.restrictions.add(desc);
        rBlock.block = block;

        root.entries.add(rBlock);

        rBlock = new RestrictionSection();
        block = new BlockOrTag();
        block.name = "minecraft:furnace";
        rBlock.block = block;

        desc = new RestrictionDescriptor();
        desc.type = Restriction.Type.CLOSEDROOM;
        desc.amount = 50;
        block = new BlockOrTag();
        block.name = "minecraft:planks";
        block.count = 40;
        block.isTag = true;
        desc.block = block;
        rBlock.restrictions.add(desc);

        root.entries.add(rBlock);

        rBlock = new RestrictionSection();
        block = new BlockOrTag();
        block.name = "minecraft:brewing_stand";
        rBlock.block = block;

        desc = new RestrictionDescriptor();
        desc.type = Restriction.Type.DIMENSION;
        desc.dimension = "minecraft:the_nether";
        rBlock.restrictions.add(desc);

        root.entries.add(rBlock);

        rBlock = new RestrictionSection();
        block = new BlockOrTag();
        block.name = "minecraft:enchanting_table";
        rBlock.block = block;

        desc = new RestrictionDescriptor();
        desc.amount = 3;
        desc.type = Restriction.Type.NEARBYBLOCKS;


        block = new BlockOrTag();
        block.name = "minecraft:lapis_block";
        block.count = 4;
        desc.block = block;
        rBlock.restrictions.add(desc);

        root.entries.add(rBlock);

        rBlock = new RestrictionSection();
        block = new BlockOrTag();
        block.name = "minecraft:beacon";
        rBlock.block = block;

        desc = new RestrictionDescriptor();
        desc.type = Restriction.Type.EXPERIENCE;
        desc.amount = 20;
        rBlock.restrictions.add(desc);

        root.entries.add(rBlock);

        rBlock = new RestrictionSection();
        block = new BlockOrTag();
        block.name = "minecraft:obsidian";
        rBlock.block = block;

        desc = new RestrictionDescriptor();
        desc.type = Restriction.Type.MINHEIGHT;
        desc.amount = 16;
        desc.reverse = true;
        rBlock.restrictions.add(desc);

        root.entries.add(rBlock);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(new RestrictionOrigin(roots));
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            LogHelper.error("The example config was invalid and not created.");
            e.printStackTrace();
        }
    }

    public class RestrictionRoot {
        public ArrayList<RestrictionSection> entries = new ArrayList<>();
    }

    public class RestrictionSection {
        public BlockOrTag block;
        public ArrayList<RestrictionDescriptor> restrictions = new ArrayList<>();
    }

    public class RestrictionDescriptor {
        public Restriction.Type type;
        private Integer amount;
        private Boolean reverse;
        public String dimension;
        public String mod;
        public String name;
        public String tag;
        public boolean getIsReversed() {
            return reverse != null && reverse;
        }

        public int getAmount() {
            return amount == null ? 1 : amount;
        }

        public Restriction.Applicator getApplicator(){
            if(mod != null) return Restriction.Applicator.MOD;
            if(tag != null) return Restriction.Applicator.TAG;
            if(name != null) return Restriction.Applicator.NAME;
            return null;
        }
    }


    private static class RestrictionOrigin {
        List<RestrictionRoot> roots;

        public RestrictionOrigin(List<RestrictionRoot> roots) {
            this.roots = roots;
        }
    }
}
