package com.davqvist.customachievements.handler;

import com.davqvist.customachievements.Achievements;
import com.davqvist.customachievements.init.ModBlocks;
import com.davqvist.customachievements.utility.NBTHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class AchievementHandler {

    @SubscribeEvent
    public void onItemDetect( EntityItemPickupEvent event ){
        for( Achievement a : Achievements.detectAchievements ){
            if( Achievements.achievementsIgnoreMeta.get( a.hashCode() ) ){
                if( event.getItem().getEntityItem().getItem() == a.theItemStack.getItem() ){
                    Achievements.trigger( a, event.getEntityPlayer() );
                }
            } else{
                if( ItemStack.areItemsEqual( event.getItem().getEntityItem(), a.theItemStack ) ){
                    Achievements.trigger( a, event.getEntityPlayer() );
                }
            }

        }
    }

    @SubscribeEvent
    public void onItemCraft( PlayerEvent.ItemCraftedEvent event ){
        for( Achievement a : Achievements.craftAchievements ){
            if( Achievements.achievementsIgnoreMeta.get( a.hashCode() ) ){
                if( event.crafting.getItem() == a.theItemStack.getItem() ){
                    Achievements.trigger( a, event.player );
                }
            } else{
                if( ItemStack.areItemsEqual( event.crafting, a.theItemStack ) ){
                    Achievements.trigger( a, event.player );
                }
            }
        }
    }

    @SubscribeEvent
    public void onAchievement( AchievementEvent event ) {
        EntityPlayer player = event.getEntityPlayer();
        Achievement achievement = event.getAchievement();
        if( player != null && player instanceof EntityPlayerMP){
            StatisticsManagerServer statfile = ((EntityPlayerMP) player).getStatFile();
            if( statfile.canUnlockAchievement( achievement ) && Achievements.achievements.containsValue(achievement) && Achievements.achievementsTrophy.get(achievement.hashCode()) ){
                ItemStack is = new ItemStack(ModBlocks.trophy);
                NBTTagCompound compound = NBTHelper.getTagCompound(is);
                compound.setTag("item", achievement.theItemStack.serializeNBT());
                compound.setString("player", player.getName());
                is.setTagCompound(compound);
                ((EntityPlayerMP) player).worldObj.spawnEntityInWorld(new EntityItem(((EntityPlayerMP) player).worldObj, player.posX, player.posY, player.posZ, is));
            }
        }
    }
}
