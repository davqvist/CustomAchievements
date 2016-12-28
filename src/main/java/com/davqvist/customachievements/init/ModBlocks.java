package com.davqvist.customachievements.init;

import com.davqvist.customachievements.block.BlockTrophy;
import com.davqvist.customachievements.reference.Reference;
import com.davqvist.customachievements.tileentity.TileEntityTrophy;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@GameRegistry.ObjectHolder( Reference.MOD_ID )
public class ModBlocks {
    public static final BlockTrophy trophy = new BlockTrophy();

    public static void registerBlocks(){
        GameRegistry.register( trophy );
        GameRegistry.register( new ItemBlock( trophy ), trophy.getRegistryName() );
        GameRegistry.registerTileEntity( TileEntityTrophy.class, Reference.MOD_ID + "_tileentitytrophy" );
    }

    @SideOnly(Side.CLIENT)
    public static void loadTextures(){
        ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( trophy ), 0, new ModelResourceLocation( trophy.getUnlocalizedName().substring(5), "inventory"));
    }
}
