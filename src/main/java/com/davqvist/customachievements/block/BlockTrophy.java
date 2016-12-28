package com.davqvist.customachievements.block;

import com.davqvist.customachievements.material.MaterialStandard;
import com.davqvist.customachievements.reference.Reference;
import com.davqvist.customachievements.tileentity.TileEntityTrophy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class BlockTrophy extends Block implements ITileEntityProvider {

    protected AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB( 0, 0, 0, 1, 0.0625 * 6, 1 );
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockTrophy(){
        super( MaterialStandard.STANDARD );
        this.setCreativeTab( CreativeTabs.MISC );
        setHardness( 1.5F );
        this.setDefaultState( this.blockState.getBaseState().withProperty( FACING, EnumFacing.SOUTH ) );
        setRegistryName( "trophy" );
        this.setUnlocalizedName( "trophy" );
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation( ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced ){
        NBTTagCompound compound = stack.getTagCompound();
        if( compound != null ){
            if( compound.hasKey( "item" ) ){
                ItemStack is = ItemStack.loadItemStackFromNBT( (NBTTagCompound) compound.getTag( "item" ) );
                if( is != null ){
                    tooltip.add( "Item: " + is.getDisplayName() );
                }
            }
            if( compound.hasKey( "player" ) ){
                String splayer = compound.getString( "player" );
                if( !splayer.isEmpty() ){
                    tooltip.add( "Player: " + splayer );
                }
            }
        }
    }

    @Override
    public void onBlockPlacedBy( World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack ){
        NBTTagCompound compound = stack.getTagCompound();
        if( compound != null ){
            TileEntity te = worldIn.getTileEntity( pos );
            if( te instanceof TileEntityTrophy ){
                if( compound.hasKey( "item" ) ){
                    ItemStack is = ItemStack.loadItemStackFromNBT( (NBTTagCompound) compound.getTag( "item" ) );
                    if( is != null ){
                        ((TileEntityTrophy) te).setItemStack( is );
                    }
                }
                if( compound.hasKey( "player" ) ){
                    String player = compound.getString( "player" );
                    if( !player.isEmpty() ){
                        ((TileEntityTrophy) te).setPlayer( player );
                    }
                }
                ((TileEntityTrophy) te).setFacing( placer.getHorizontalFacing().getOpposite() );
            }
        }
    }

    @Override
    public IBlockState getStateForPlacement( World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, ItemStack stack ){
        return this.getDefaultState().withProperty( FACING, placer.getHorizontalFacing().getOpposite() );
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer( this, new IProperty[] {FACING} );
    }

    @Override
    public int getMetaFromState( IBlockState state ){
        EnumFacing facing = (EnumFacing)state.getValue( FACING );
        return facing.getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta( int meta ){
        return this.getDefaultState().withProperty( FACING, EnumFacing.getHorizontal( meta ) );
    }

    @Override
    public List<ItemStack> getDrops( IBlockAccess world, BlockPos pos, IBlockState state, int fortune ){
        TileEntity te = world.getTileEntity( pos );
        ArrayList<ItemStack> result = new ArrayList<ItemStack>();
        if( te instanceof TileEntityTrophy ){
            ItemStack stack = new ItemStack( Item.getItemFromBlock( this ) );
            NBTTagCompound compound = new NBTTagCompound();
            compound = te.writeToNBT( compound );
            stack.setTagCompound( compound );
            result.add( stack );
        }
        return result;
    }

    @Override
    public boolean isOpaqueCube( IBlockState blockState ){
        return false;
    }

    @Override
    public TileEntity createNewTileEntity( World worldIn, int meta ){
        return new TileEntityTrophy();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos ){
        return BOUNDING_BOX;
    }

    @Override
    public String getUnlocalizedName(){
        return String.format( "tile.%s%s", Reference.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName( super.getUnlocalizedName() ) );
    }

    protected String getUnwrappedUnlocalizedName( String unlocalizedName ){
        return unlocalizedName.substring( unlocalizedName.indexOf(".") + 1 );
    }
}
