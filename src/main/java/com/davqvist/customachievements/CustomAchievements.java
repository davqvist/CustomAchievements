package com.davqvist.customachievements;

import com.davqvist.customachievements.proxy.CommonProxy;
import com.davqvist.customachievements.reference.Reference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod( modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY_CLASS )
public class CustomAchievements {
    @Mod.Instance( Reference.MOD_ID )
    public static CustomAchievements instance;

    @SidedProxy( clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS )
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit( FMLPreInitializationEvent event ){
        proxy.preInit( event );
    }

    @Mod.EventHandler
    public void init( FMLInitializationEvent event ){
        proxy.init( event );
    }

    @Mod.EventHandler
    public void postInit( FMLPostInitializationEvent event ){
        proxy.postInit( event );
    }
}