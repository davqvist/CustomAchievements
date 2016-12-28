package com.davqvist.customachievements.handler;

import com.davqvist.customachievements.Achievements;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
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

}
