package com.davqvist.restriction.config;

import com.davqvist.restriction.utility.LogHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RestrictionReader{

    public RestrictionRoot root;

    public void readRestrictions( File file ){
        if( file.exists() ){
            try{
                Gson gson = new Gson();
                JsonReader reader = new JsonReader( new FileReader( file ) );
                root = gson.fromJson( reader, RestrictionRoot.class );
            } catch( Exception e ){
                LogHelper.error( "The Restriction json was invalid and is ignored." );
                e.printStackTrace();
            }
        } else {
            root = new RestrictionRoot();
            RestrictionBlock block = new RestrictionBlock();
            block.block = "minecraft:bed";
            block.meta = 0;
            block.ignoreMeta = true;

            RestrictionDesciptor desc = new RestrictionDesciptor();
            desc.type = RestrictionType.SEESKY;
            desc.reverse = true;
            block.restrictions.add( desc );

            root.entries.add( block );

            block = new RestrictionBlock();
            block.block = "minecraft:furnace";
            block.meta = 0;
            block.ignoreMeta = true;

            desc = new RestrictionDesciptor();
            desc.type = RestrictionType.CLOSEDROOM;
            desc.size = 50;
            desc.block = "minecraft:planks";
            desc.meta = 0;
            desc.ignoreMeta = true;
            desc.amount = 40;
            block.restrictions.add( desc );

            root.entries.add( block );

            block = new RestrictionBlock();
            block.block = "minecraft:brewing_stand";
            block.meta = 0;
            block.ignoreMeta = true;

            desc = new RestrictionDesciptor();
            desc.type = RestrictionType.DIMENSION;
            desc.id = "";
            block.restrictions.add( desc );

            root.entries.add( block );

            block = new RestrictionBlock();
            block.block = "minecraft:enchanting_table";
            block.meta = 0;

            desc = new RestrictionDesciptor();
            desc.type = RestrictionType.NEARBYBLOCKS;
            desc.size = 3;
            desc.amount = 4;
            desc.block = "minecraft:lapis_block";
            desc.meta = 0;
            block.restrictions.add( desc );

            root.entries.add( block );

            block = new RestrictionBlock();
            block.block = "minecraft:beacon";
            block.meta = 0;

            desc = new RestrictionDesciptor();
            desc.type = RestrictionType.EXPERIENCE;
            desc.amount = 20;
            block.restrictions.add( desc );

            root.entries.add( block );

            block = new RestrictionBlock();
            block.block = "minecraft:obsidian";
            block.meta = 0;

            desc = new RestrictionDesciptor();
            desc.type = RestrictionType.MINHEIGHT;
            desc.amount = 16;
            desc.reverse = true;
            block.restrictions.add( desc );

            root.entries.add( block );

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson( root );
            try{
                FileWriter writer = new FileWriter( file );
                writer.write( json );
                writer.close();
            } catch( IOException e ){
                LogHelper.error( "The default config was invalid and not created." );
                e.printStackTrace();
            }
        }
    }

    public class RestrictionRoot{
        public ArrayList<RestrictionBlock> entries = new ArrayList<>();
    }

    public class RestrictionBlock{
        public String block;
        public Integer meta;
        public boolean ignoreMeta;
        public ArrayList<RestrictionDesciptor> restrictions = new ArrayList<>();
    }

    public class RestrictionDesciptor{
        public RestrictionType type;
        public boolean reverse;
        public Integer size;
        public String block;
        public Integer meta;
        public boolean ignoreMeta;
        public Integer amount;
        public String id;
    }

    public enum RestrictionType{
        SEESKY, CLOSEDROOM, DIMENSION, NEARBYBLOCKS, EXPERIENCE, MINHEIGHT, ADVANCEMENT
    }
}
