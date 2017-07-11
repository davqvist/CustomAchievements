package com.davqvist.customachievements.handler;

import java.util.Collection;

import com.davqvist.customachievements.Achievements;
import com.davqvist.customachievements.config.AchievementsReader.AchievementDesciptor;
import com.davqvist.customachievements.config.AchievementsReader.AchievementType;

import com.davqvist.customachievements.init.ModBlocks;
import com.davqvist.customachievements.utility.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AchievementHandler {

    @SubscribeEvent
    public void onItemDetect(EntityItemPickupEvent event){
        propagate(event.getEntityPlayer(), AchievementType.DETECT, event.getItem().getEntityItem());
    }

    @SubscribeEvent
    public void onItemCraft(PlayerEvent.ItemCraftedEvent event){
    	propagate(event.player, AchievementType.CRAFT, event.crafting);
    }
    
    @SubscribeEvent
    public void onMineBlock(BlockEvent.BreakEvent event){
    	propagate(event.getPlayer(), AchievementType.MINE, event.getState());
    }
    
    @SubscribeEvent
    public void onPlaceBlock(BlockEvent.PlaceEvent event){
    	propagate(event.getPlayer(), AchievementType.PLACE, event.getState());
    }
    
    @SubscribeEvent
    public void onTick(PlayerTickEvent event) {
    	if(event.player instanceof EntityPlayerMP) {
    		EntityPlayerMP pmp = (EntityPlayerMP) event.player;
	    	Collection<AchievementDesciptor> list = Achievements.achievementHandlers.get(AchievementType.STAT);
	    	for(AchievementDesciptor desc : list) {
	    		String statName = desc.stat;
	    		StatBase stat = StatList.getOneShotStat(statName);
	    		if(stat != null && pmp.getStatFile().readStat(stat) >= desc.statValue)
	    			Achievements.trigger(Achievements.achievementsDescriptors.get(desc), pmp);
	    	}
    	}
    }
    
    @SubscribeEvent
    public void onKillMob(LivingDeathEvent event){
    	if(event.getSource().getEntity() instanceof EntityPlayer) {
    		EntityPlayer player = (EntityPlayer) event.getSource().getEntity();
    		String entityName = EntityList.getEntityString(event.getEntity());
    		
        	Collection<AchievementDesciptor> list = Achievements.achievementHandlers.get(AchievementType.KILL);
        	for(AchievementDesciptor desc : list) {
        		if(desc.mob.equals(entityName))
        			Achievements.trigger(Achievements.achievementsDescriptors.get(desc), player);
        	}
    	}
    }

	@SubscribeEvent
	public void onClickEvent( PlayerInteractEvent.RightClickBlock event) {
    	if(event.getPos() != null){
            IBlockState state = event.getWorld().getBlockState(event.getPos());
            propagate(event.getEntityPlayer(), AchievementType.INTERACT, state);
		}
	}

	@SubscribeEvent
	public void onClickEvent( PlayerInteractEvent.RightClickItem event) {
		propagate(event.getEntityPlayer(), AchievementType.USE, event.getItemStack());
	}

	@SubscribeEvent
	public void onAchievement( AchievementEvent event ) {
		EntityPlayer player = event.getEntityPlayer();
		Achievement achievement = event.getAchievement();
		if( player != null && player instanceof EntityPlayerMP ){
			StatisticsManagerServer statfile = ((EntityPlayerMP) player).getStatFile();
			if( statfile.canUnlockAchievement( achievement ) && Achievements.achievementsDescriptors.inverse().containsKey(achievement) && Achievements.achievementsDescriptors.inverse().get(achievement).trophy ){
				ItemStack is = new ItemStack(ModBlocks.trophy);
				NBTTagCompound compound = NBTHelper.getTagCompound(is);
				compound.setTag("item", achievement.theItemStack.serializeNBT());
				compound.setString("player", player.getName());
				is.setTagCompound(compound);
				player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, is));
			}
		}
	}
    
    private void propagate(EntityPlayer player, AchievementType type, IBlockState state) {
    	Block block = state.getBlock();
    	ItemStack stack = new ItemStack(block, 1, block.getMetaFromState(state));
    	propagate(player, type, stack);
    }
    
    private void propagate(EntityPlayer player, AchievementType type, ItemStack stack) {
    	Collection<AchievementDesciptor> list = Achievements.achievementHandlers.get(type);
        for(AchievementDesciptor desc : list)
        	giveIfValid(player, desc, stack);
    }
    
    private void giveIfValid(EntityPlayer player, AchievementDesciptor desc, ItemStack stack) {
    	Achievement a = Achievements.achievementsDescriptors.get(desc);
    	if((desc.ignoreMeta && stack.getItem() == a.theItemStack.getItem()) || ItemStack.areItemsEqual(stack, a.theItemStack))
    		Achievements.trigger(a, player);
    }
    
}
