package com.davqvist.customachievements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.davqvist.customachievements.config.AchievementsReader;
import com.davqvist.customachievements.config.AchievementsReader.AchievementDesciptor;
import com.davqvist.customachievements.config.AchievementsReader.AchievementTab;
import com.davqvist.customachievements.init.ModBlocks;
import com.davqvist.customachievements.utility.NBTHelper;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class Achievements {
    public static AchievementPage page;

    public static Map<String, Achievement> achievements = new HashMap<String, Achievement>();
    public static Map<Integer, Boolean> achievementsIgnoreMeta = new HashMap<Integer, Boolean>();
    public static Map<Integer, Boolean> achievementsTrophy = new HashMap<Integer, Boolean>();
    public static List<Achievement> detectAchievements = new ArrayList<>();
    public static List<Achievement> craftAchievements = new ArrayList<>();

    public static void init() {
    	for(AchievementTab tab : CustomAchievements.proxy.ar.root.tabs)
	        for( AchievementsReader.AchievementDesciptor alist : tab.achievements ){
	            if( !alist.uid.isEmpty() ){
	                int meta = 0;
	                if( alist.meta != null ){ meta = alist.meta; }
	                ItemStack is = new ItemStack( Item.getByNameOrId( alist.item ), 1, meta );
	                if( !is.isEmpty() ) {
	                    Achievement tempAch = new Achievement( "achievement." + alist.uid, alist.uid, alist.xpos, alist.ypos, is, achievements.get( alist.parent ) ).registerStat();
	                    achievements.put( alist.uid, tempAch );
	                    achievementsIgnoreMeta.put( tempAch.hashCode(), alist.ignoreMeta );
	                    achievementsTrophy.put( tempAch.hashCode(), alist.trophy );
	
	                    if( alist.type.equals( "Detect" ) ){ detectAchievements.add( tempAch ); }
	                    if( alist.type.equals( "Craft" ) ){ craftAchievements.add( tempAch ); }
	                }
	            }
	        }

        for(AchievementTab tab : CustomAchievements.proxy.ar.root.tabs) {
	        String tabName = tab.tabname.isEmpty() ? "Custom Achievements" : tab.tabname;
	        List<Achievement> achievementList = tab.achievements.stream().filter((a) -> achievements.containsKey(a.uid)).map((a) -> achievements.get(a.uid)).collect(Collectors.toList());
	        page = new AchievementPage( tabName, achievementList.toArray( new Achievement[achievementList.size()] ) );
	        AchievementPage.registerAchievementPage( page );
        }
    }

    public static void trigger( Achievement achievement, EntityPlayer player ){
        if( player != null ){
            boolean getTrophy = false;
            if( !player.world.isRemote && player instanceof EntityPlayerMP && !player.hasAchievement( achievement ) && achievementsTrophy.get( achievement.hashCode() ) ){
                getTrophy = true;
            }
            player.addStat( achievement );
            if( player.hasAchievement( achievement ) && getTrophy ){
                ItemStack is = new ItemStack( ModBlocks.trophy );
                NBTTagCompound compound = NBTHelper.getTagCompound( is );
                compound.setTag( "item", achievement.theItemStack.serializeNBT() );
                compound.setString( "player", player.getName() );
                is.setTagCompound( compound );
                player.world.spawnEntity( new EntityItem( player.world, player.posX, player.posY, player.posZ, is ) );
            }
        }
    }
}
