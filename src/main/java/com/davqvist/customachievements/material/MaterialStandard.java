package com.davqvist.customachievements.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialStandard extends Material {

    public static final MaterialStandard STANDARD = new MaterialStandard( MapColor.GOLD );

    public MaterialStandard( MapColor color ){ super( color ); }

    @Override
    public boolean blocksMovement()
    {
        return false;
    }
}