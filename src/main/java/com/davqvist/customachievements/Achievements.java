package com.davqvist.customachievements;

import com.davqvist.customachievements.config.AchievementsReader;
import com.davqvist.customachievements.init.ModBlocks;
import com.davqvist.customachievements.utility.LogHelper;
import com.davqvist.customachievements.utility.NBTHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Achievements {
    public static AchievementPage page;

    public static Map<String, Achievement> achievements = new HashMap<String, Achievement>();
    public static Map<Integer, Boolean> achievementsIgnoreMeta = new HashMap<Integer, Boolean>();
    public static Map<Integer, Boolean> achievementsTrophy = new HashMap<Integer, Boolean>();
    public static List<Achievement> detectAchievements = new ArrayList<>();
    public static List<Achievement> craftAchievements = new ArrayList<>();

    public static void init() {

        for( AchievementsReader.AchievementList alist : CustomAchievements.proxy.ar.root.achievements ){
            if( !alist.uid.isEmpty() ){
                int meta = 0;
                if( alist.meta != null ){ meta = alist.meta; }
                ItemStack is = new ItemStack( Item.getByNameOrId( alist.item ), 1, meta );
                if( is != null ) {
                    Achievement tempAch = new Achievement( "achievement." + alist.uid, alist.uid, alist.xpos, alist.ypos, is, achievements.get( alist.parent ) ).registerStat();
                    achievements.put( alist.uid, tempAch );
                    achievementsIgnoreMeta.put( tempAch.hashCode(), alist.ignoreMeta );
                    achievementsTrophy.put( tempAch.hashCode(), alist.trophy );

                    if( alist.type.equals( "Detect" ) ){ detectAchievements.add( tempAch ); }
                    if( alist.type.equals( "Craft" ) ){ craftAchievements.add( tempAch ); }
                }
            }
        }

        String tabName = CustomAchievements.proxy.ar.root.tabname.isEmpty() ? "Custom Achievements" : CustomAchievements.proxy.ar.root.tabname;
        page = new AchievementPage( tabName, achievements.values().toArray( new Achievement[achievements.size()] ) );
        AchievementPage.registerAchievementPage( page );
    }

    public static void trigger( Achievement achievement, EntityPlayer player ){
        if( player != null ){
            boolean getTrophy = false;
            if( !player.worldObj.isRemote && player instanceof EntityPlayerMP && !player.hasAchievement( achievement ) && achievementsTrophy.get( achievement.hashCode() ) ){
                getTrophy = true;
            }
            player.addStat( achievement );
            if( player.hasAchievement( achievement ) && getTrophy ){
                ItemStack is = new ItemStack( ModBlocks.trophy );
                NBTTagCompound compound = NBTHelper.getTagCompound( is );
                compound.setTag( "item", achievement.theItemStack.serializeNBT() );
                compound.setString( "player", player.getName() );
                is.setTagCompound( compound );
                player.worldObj.spawnEntityInWorld( new EntityItem( player.worldObj, player.posX, player.posY, player.posZ, is ) );
            }
        }
    }
}
