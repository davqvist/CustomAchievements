package com.davqvist.customachievements.proxy;

import com.davqvist.customachievements.init.ModBlocks;
import com.davqvist.customachievements.render.RenderTrophy;
import com.davqvist.customachievements.resource.AchievementResourcePack;
import com.davqvist.customachievements.tileentity.TileEntityTrophy;
import com.davqvist.customachievements.utility.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import scala.collection.parallel.ParIterableLike;

import java.lang.reflect.Field;
import java.util.List;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit( FMLPreInitializationEvent e ){
        super.preInit( e );

        LogHelper.info( "Trying to load CustomAchievements Translation Resource Pack");
        try {
            List packs = (List) ObfuscationReflectionHelper.getPrivateValue( Minecraft.class, Minecraft.getMinecraft(), new String[]{"field_110449_ao"} ); //defaultResourcePacks
            packs.add( new AchievementResourcePack() );
            ObfuscationReflectionHelper.setPrivateValue( Minecraft.class, Minecraft.getMinecraft(), packs, new String[]{"field_110449_ao"} );
        } catch( Exception exception ){
            LogHelper.error( "Loading CustomAchievements Translation Resource Pack failed.");
            exception.printStackTrace();
        }

        ModBlocks.loadTextures();
        ClientRegistry.bindTileEntitySpecialRenderer( TileEntityTrophy.class, new RenderTrophy() );
    }
}
