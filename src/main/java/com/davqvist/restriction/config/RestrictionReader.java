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
            RestrictionRoot root = gson.fromJson(reader, RestrictionRoot.class);
            if (root != null)
                roots.add(root);
        } catch (Exception e) {
            LogHelper.error("The Restriction json was invalid and is ignored.");
            e.printStackTrace();
        }
    }

    public void createTestConfig(File file) {
        RestrictionRoot root = new RestrictionRoot();
        roots.add(root);
        RestrictionSection rBlock = new RestrictionSection();
        BlockOrTag block = new BlockOrTag();
        block.name = "minecraft:orange_bed";

        RestrictionDescriptor desc = new RestrictionDescriptor();
        desc.type = RestrictionType.SEESKY;
        desc.reverse = true;
        rBlock.restrictions.add(desc);
        rBlock.block = block;

        root.entries.add(rBlock);

        rBlock = new RestrictionSection();
        block = new BlockOrTag();
        block.name = "minecraft:furnace";
        rBlock.block = block;

        desc = new RestrictionDescriptor();
        desc.type = RestrictionType.CLOSEDROOM;
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
        desc.type = RestrictionType.DIMENSION;
        desc.dimension = "minecraft:the_nether";
        rBlock.restrictions.add(desc);

        root.entries.add(rBlock);

        rBlock = new RestrictionSection();
        block = new BlockOrTag();
        block.name = "minecraft:enchanting_table";
        rBlock.block = block;

        desc = new RestrictionDescriptor();
        desc.amount = 3;
        desc.type = RestrictionType.NEARBYBLOCKS;


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
        desc.type = RestrictionType.EXPERIENCE;
        desc.amount = 20;
        rBlock.restrictions.add(desc);

        root.entries.add(rBlock);

        rBlock = new RestrictionSection();
        block = new BlockOrTag();
        block.name = "minecraft:obsidian";
        rBlock.block = block;

        desc = new RestrictionDescriptor();
        desc.type = RestrictionType.MINHEIGHT;
        desc.amount = 16;
        desc.reverse = true;
        rBlock.restrictions.add(desc);

        root.entries.add(rBlock);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(roots);
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
        public RestrictionType type;
        private Integer amount;
        private Boolean reverse;
        public String dimension;
        public BlockOrTag block;

        public boolean getReverse() {
            return reverse != null && reverse;
        }

        public int getAmount() {
            return amount == null ? 0 : amount;
        }
    }

    public class BlockOrTag {
        public String name;
        private Boolean isTag;
        private Integer count;

        public int getCount() {
            return count == null ? 0 : count;
        }

        public boolean isTag() {
            return isTag != null && isTag;
        }
    }

    public enum RestrictionType {
        SEESKY, CLOSEDROOM, DIMENSION, NEARBYBLOCKS, EXPERIENCE, MINHEIGHT, ADVANCEMENT
    }
}
