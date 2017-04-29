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

    public GsonStart root;

    public void readAchievements( File file ){
        if( file.exists() ){
            try {
                Gson gson = new Gson();
                JsonReader reader = new JsonReader( new FileReader( file ) );
                root = gson.fromJson( reader, GsonStart.class );
            } catch ( Exception e ){
                LogHelper.error( "The CustomAchievements json was invalid and is ignored.");
                e.printStackTrace();
            }
        } else{
            root = new GsonStart();
            root.tabname = "CustomAchievements";
            AchievementList al1 = new AchievementList();
            al1.uid = "detectLog";
            al1.name = "Log detected";
            al1.desc = "Pick up a log";
            al1.type = "Detect";
            al1.item = "minecraft:log";
            al1.ignoreMeta = true;
            al1.xpos = 0;
            al1.ypos = 0;
            root.achievements.add( al1 );
            AchievementList al2 = new AchievementList();
            al2.uid = "craftAcaciaPlanks";
            al2.name = "Acacia Planks crafted";
            al2.desc = "Craft Acacia Planks";
            al2.type = "Craft";
            al2.item = "minecraft:planks";
            al2.meta = 4;
            al2.parent = "detectLog";
            al2.trophy = true;
            al2.xpos = 1;
            al2.ypos = 0;
            root.achievements.add( al2 );

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

    public class GsonStart {
        public String tabname;
        public ArrayList<AchievementList> achievements = new ArrayList<>();
    }
    
    public class AchievementList {
        public String uid;
        public String name;
        public String desc;
        public String type;
        public String item;
        public Integer meta;
        public boolean ignoreMeta;
        public String parent;
        public boolean trophy;
        public Integer xpos;
        public Integer ypos;
    }
}