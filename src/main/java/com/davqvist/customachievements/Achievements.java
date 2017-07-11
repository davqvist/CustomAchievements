package com.davqvist.customachievements;

import java.util.ArrayList;
import java.util.List;

import com.davqvist.customachievements.config.AchievementsReader;
import com.davqvist.customachievements.config.AchievementsReader.AchievementDesciptor;
import com.davqvist.customachievements.config.AchievementsReader.AchievementTab;
import com.davqvist.customachievements.config.AchievementsReader.AchievementType;
import com.davqvist.customachievements.init.ModBlocks;
import com.davqvist.customachievements.utility.NBTHelper;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatList;
import net.minecraftforge.common.AchievementPage;

public class Achievements {

    public static BiMap<AchievementDesciptor, Achievement> achievementsDescriptors = HashBiMap.<AchievementDesciptor, Achievement>create();
    public static Multimap<AchievementType, AchievementDesciptor> achievementHandlers = HashMultimap.<AchievementType, AchievementDesciptor>create();

    public static void init() {
    	for(AchievementTab tab : CustomAchievements.proxy.ar.root.tabs) {
        	List<Achievement> tabAchievements = new ArrayList();

	        for(AchievementsReader.AchievementDesciptor desc : tab.achievements){
	            if(!desc.uid.isEmpty()){
	                int meta = 0;
	                if(desc.meta != null)
	                	meta = desc.meta;
	                
	                ItemStack is = new ItemStack(Item.getByNameOrId(desc.item), 1, meta);
	                Achievement parent = null;
	                if(desc.parent != null && !desc.parent.isEmpty())
	                	parent = (Achievement) StatList.getOneShotStat("achievement." + desc.parent);
	                
                    Achievement tempAch = new Achievement("achievement." + desc.uid, desc.uid, desc.xpos, desc.ypos, is, parent).registerStat();
                    achievementsDescriptors.put(desc, tempAch);
                    achievementHandlers.put(desc.type, desc);
                    tabAchievements.add(tempAch);
	            }
	        }
	        
	        String tabName = tab.tabname.isEmpty() ? "NO NAME!" : tab.tabname;
	        AchievementPage page = new AchievementPage(tabName, tabAchievements.toArray(new Achievement[tabAchievements.size()]));
	        AchievementPage.registerAchievementPage(page);
    	}
    }

    public static void trigger( Achievement achievement, EntityPlayer player ){
        if(player != null && !player.world.isRemote && !player.hasAchievement(achievement)){
            player.addStat(achievement);
        }
    }
}
