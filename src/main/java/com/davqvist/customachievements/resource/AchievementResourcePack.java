package com.davqvist.customachievements.resource;

import com.davqvist.customachievements.CustomAchievements;
import com.davqvist.customachievements.config.AchievementsReader;
import com.davqvist.customachievements.utility.LogHelper;
import com.google.common.base.Charsets;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Set;

public class AchievementResourcePack implements IResourcePack {
    @Override
    public InputStream getInputStream( ResourceLocation location ) throws IOException {
        if( location.getResourcePath().equals( "lang/en_US.lang" ) ){
            LogHelper.info( "CustomAchievements Translation Resource Pack loaded.");
            String translation = "";
            for( AchievementsReader.AchievementList alist : CustomAchievements.proxy.ar.root.achievements ){
                translation += "achievement." + alist.uid + "=" + alist.name + "\n";
                translation += "achievement." + alist.uid + ".desc=" + alist.desc + "\n";
            }
            return new ByteArrayInputStream( translation.getBytes( Charsets.UTF_8 ) );
        } else {
            LogHelper.error( "Error while loading CustomAchievements Translation Resource Pack.");
            return null;
        }
    }

    @Override
    public boolean resourceExists( ResourceLocation location ){
        return location.getResourcePath().equals( "lang/en_US.lang" );
    }

    @Override
    public Set<String> getResourceDomains() {
        return Collections.singleton( "customachievements" );
    }

    @Override
    public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {
        return null;
    }

    @Override
    public BufferedImage getPackImage() throws IOException {
        return null;
    }

    @Override
    public String getPackName() {
        return "Achievement Names";
    }
}
