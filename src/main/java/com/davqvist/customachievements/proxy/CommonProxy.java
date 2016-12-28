package com.davqvist.customachievements.proxy;

import com.davqvist.customachievements.Achievements;
import com.davqvist.customachievements.config.AchievementsReader;
import com.davqvist.customachievements.handler.AchievementHandler;
import com.davqvist.customachievements.init.ModBlocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public abstract class CommonProxy {

    public AchievementsReader ar = new AchievementsReader();

    public void preInit( FMLPreInitializationEvent e ){
        File configdir = new File( e.getModConfigurationDirectory(), "customachievements" );
        if( !configdir.exists() ){
            configdir.mkdir();
        }
        File file = new File( configdir, "achievements.json");
        ar.readAchievements( file );

        ModBlocks.registerBlocks();
    }
    public void init( FMLInitializationEvent e ) {
        Achievements.init();
        MinecraftForge.EVENT_BUS.register( new AchievementHandler() );
    }
    public void postInit( FMLPostInitializationEvent e ) {}
}
