package com.davqvist.customachievements.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.davqvist.customachievements.utility.LogHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class AchievementsReader {

    public AchievementsRoot root;

    public void readAchievements( File file ){
        if( file.exists() ){
            try {
                Gson gson = new Gson();
                JsonReader reader = new JsonReader( new FileReader( file ) );
                root = gson.fromJson( reader, AchievementsRoot.class );
            } catch ( Exception e ){
                LogHelper.error( "The CustomAchievements json was invalid and is ignored.");
                e.printStackTrace();
            }
        } else{
        	root = new AchievementsRoot();
            AchievementTab tab = new AchievementTab();
            tab.tabname = "CustomAchievements 1";
            
            AchievementDesciptor desc = new AchievementDesciptor();
            desc.uid = "detectLog";
            desc.name = "Log detected";
            desc.desc = "Pick up a log";
            desc.type = AchievementType.DETECT;
            desc.item = "minecraft:log";
            desc.ignoreMeta = true;
            desc.xpos = 0;
            desc.ypos = 0;
            tab.achievements.add(desc);
            
            desc = new AchievementDesciptor();
            desc.uid = "craftAcaciaPlanks";
            desc.name = "Acacia Planks crafted";
            desc.desc = "Craft Acacia Planks";
            desc.type = AchievementType.CRAFT;
            desc.item = "minecraft:planks";
            desc.meta = 4;
            desc.parent = "detectLog";
            desc.xpos = 2;
            desc.ypos = 0;
            tab.achievements.add(desc);

            desc = new AchievementDesciptor();
            desc.uid = "placeCrafting";
            desc.name = "Crafting Table placed";
            desc.desc = "Place a Crafting Table";
            desc.type = AchievementType.PLACE;
            desc.item = "minecraft:crafting_table";
            desc.parent = "craftAcaciaPlanks";
            desc.trophy = true;
            desc.xpos = 2;
            desc.ypos = 2;
            tab.achievements.add(desc);
            
            desc = new AchievementDesciptor();
            desc.uid = "breakCrafting";
            desc.name = "Crafting Table broken";
            desc.desc = "Break a Crafting Table";
            desc.type = AchievementType.MINE;
            desc.item = "minecraft:crafting_table";
            desc.parent = "placeCrafting";
            desc.trophy = true;
            desc.xpos = 0;
            desc.ypos = 2;
            tab.achievements.add(desc);
            
            root.tabs.add(tab);
            
            tab = new AchievementTab();
            tab.tabname = "CustomAchievements 2";

            desc = new AchievementDesciptor();
            desc.uid = "jump5";
            desc.name = "Five jumps";
            desc.desc = "Jump five times";
            desc.type = AchievementType.STAT;
            desc.item = "minecraft:rabbit_foot";
            desc.stat = "stat.jump";
            desc.statValue = 5;
            desc.trophy = false;
            desc.xpos = 0;
            desc.ypos = 0;
            tab.achievements.add( desc );
            
            desc = new AchievementDesciptor();
            desc.uid = "killZombie";
            desc.name = "Zombie killed";
            desc.desc = "Kill a zombie";
            desc.type = AchievementType.KILL;
            desc.item = "minecraft:rotten_flesh";
            desc.mob = "Zombie";
            desc.parent = "jump5";
            desc.trophy = true;
            desc.xpos = 2;
            desc.ypos = 0;
            tab.achievements.add( desc );

            desc = new AchievementDesciptor();
            desc.uid = "interactFurnace";
            desc.name = "Furnace opened";
            desc.desc = "Right clicked Furnace";
            desc.type = AchievementType.INTERACT;
            desc.item = "minecraft:furnace";
            desc.ignoreMeta = true;
            desc.trophy = true;
            desc.xpos = 0;
            desc.ypos = 2;
            tab.achievements.add( desc );

            desc = new AchievementDesciptor();
            desc.uid = "useBow";
            desc.name = "Shot bow";
            desc.desc = "Right clicked bow";
            desc.type = AchievementType.USE;
            desc.item = "minecraft:bow";
            desc.ignoreMeta = true;
            desc.parent = "interactFurnace";
            desc.trophy = true;
            desc.xpos = 2;
            desc.ypos = 2;
            tab.achievements.add( desc );

            
            root.tabs.add(tab);
            
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson( root );
            try {
                FileWriter writer = new FileWriter( file );
                writer.write( json );
                writer.close();
            } catch( IOException e ){
                LogHelper.error( "The default config was invalid and not created." );
                e.printStackTrace();
            }
        }
    }

    public class AchievementsRoot {
    	public ArrayList<AchievementTab> tabs = new ArrayList<>();
    }
    
    public class AchievementTab {
    	public String tabname;
        public ArrayList<AchievementDesciptor> achievements = new ArrayList<>();
    }
    
    public class AchievementDesciptor {
        public String uid;
        public String name;
        public String desc;
        public AchievementType type;
        public String item;
        public String mob;
        public String stat;
        public int statValue;
        public Integer meta;
        public boolean ignoreMeta;
        public String parent;
        public boolean trophy;
        public Integer xpos;
        public Integer ypos;
        
        @Override
        public int hashCode() {
        	return uid.hashCode();
        }
    }
    
    public enum AchievementType {
    	CRAFT, DETECT, KILL, STAT, MINE, PLACE, INTERACT, USE
    }

}