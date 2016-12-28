package com.davqvist.customachievements.utility;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBTHelper {

    public static NBTTagCompound getTagCompound(ItemStack itemstack ){
        NBTTagCompound nbtTagCompound = itemstack.getTagCompound();
        if( nbtTagCompound == null ){
            nbtTagCompound = new NBTTagCompound();
            itemstack.setTagCompound( nbtTagCompound );
        }
        return nbtTagCompound;
    }
}