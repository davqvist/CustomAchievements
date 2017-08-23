package com.davqvist.customachievements.render;

import com.davqvist.customachievements.tileentity.TileEntityTrophy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class RenderTrophy extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt( TileEntity te, double x, double y, double z, float partialTick, int destroyStage ){
        if( ( te instanceof TileEntityTrophy) ){
            ItemStack stack = ((TileEntityTrophy) te).getItemStack();
            if( stack != null ){
                GlStateManager.pushMatrix();
                GlStateManager.translate( (float) x + 0.5f, (float) y + 0.75f, (float) z + 0.5f );
                double time = Minecraft.getSystemTime();
                GlStateManager.rotate( (float)( ( time / 20d ) % 360 ), 0, 1, 0 );
                if( !( stack.getItem() instanceof ItemBlock ) ){
                    float scaleFactor = 0.7f;
                    GlStateManager.scale( scaleFactor, scaleFactor, scaleFactor );
                }
                Minecraft.getMinecraft().getRenderItem().renderItem( stack, ItemCameraTransforms.TransformType.FIXED );
                GlStateManager.popMatrix();
            }
            String player = ((TileEntityTrophy) te).getPlayer();
            if( player != null && !player.isEmpty() ){
                GlStateManager.pushMatrix();
                GlStateManager.translate( (float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f );
                GlStateManager.rotate( 180, 0, 0, 1 );
                EnumFacing facing = ((TileEntityTrophy) te).getFacing();
                GlStateManager.rotate( facing.getHorizontalIndex()*90 + 180, 0, 1, 0 );
                GlStateManager.translate( 0, 0.03f, -0.36f );
                GlStateManager.rotate( -45, 1, 0, 0 );
                GlStateManager.scale( 0.008f, 0.008f, 1 );
                getFontRenderer().drawString( player, - getFontRenderer().getStringWidth( player ) / 2, 0, 0 );
                GlStateManager.popMatrix();
            }
        }
    }
}
