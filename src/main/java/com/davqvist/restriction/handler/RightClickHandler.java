package com.davqvist.restriction.handler;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Stack;

public class RightClickHandler {

    @SubscribeEvent
    public void onRightClick( RightClickBlock event ){
        EntityPlayer player = event.getEntityPlayer();
        BlockPos pos = event.getPos();
        World world = event.getWorld();
        if( player != null && !player.isCreative() && !event.isCanceled() && pos != null ){
            IBlockState state = world.getBlockState( event.getPos() );
            if( state != null ){
                if( state.getBlock() == Blocks.BED ){
                    if( canSeeSky( pos, world ) ) {
                        cancelRightClick( event, "Bed isn't allowed to see the sky.");
                    }
                }
                if( state.getBlock() == Blocks.FURNACE || state.getBlock() == Blocks.LIT_FURNACE ){
                    if( !isInRoom( pos, world, Blocks.STONE, 40 ) ){
                        cancelRightClick( event, "Furnace must be in closed room made out of at least 40 Stone.");
                    }
                }
            }
        }
    }

    private void cancelRightClick( RightClickBlock event, String message ){
        event.setUseBlock( Event.Result.DENY );
        event.setCanceled( true );
        if( event.getEntityPlayer().worldObj.isRemote ) {
            event.getEntityPlayer().addChatComponentMessage( new TextComponentString( message ) );
        }
    }

    private boolean canSeeSky( BlockPos pos, World world ){
        boolean solid = false;
        boolean reachedtop = false;
        while( !solid && !reachedtop ) {
            pos = pos.up();
            if( world.getHeight() < pos.getY() ){ reachedtop = true; }
            if( world.getBlockState( pos ).getBlock().isBlockSolid( world, pos, EnumFacing.DOWN ) ){ solid = true; }
        }
        return !solid;
    }

    private boolean isInRoom( BlockPos pos, World world, int minSize, Block block, int minBlock ){
        Stack<BlockPos> stack = new Stack<BlockPos>();
        stack.push( pos );
        final int maxSize = 10000;
        int blockCount = 0;
        final HashSet<BlockPos> addableBlocks = new HashSet<BlockPos>();

        while( !stack.isEmpty() ) {
            BlockPos stackElement = stack.pop();
            addableBlocks.add( stackElement );
            for( EnumFacing direction : EnumFacing.values() ){
                BlockPos searchNextPosition = stackElement.offset( direction );
                if( !addableBlocks.contains( searchNextPosition ) ) {
                    if( addableBlocks.size() <= maxSize ){
                        if( !world.getBlockState( searchNextPosition ).getBlock().isBlockSolid( world, searchNextPosition, direction.getOpposite() )) {
                            stack.push( searchNextPosition );
                        } else if( block != null && world.getBlockState( searchNextPosition ).getBlock() == block ){
                            blockCount++;
                        }
                    }
                    else{ return false; }
                }
            }
        }
        return( addableBlocks.size() > minSize && blockCount >= minBlock );
    }

    private boolean isInRoom( BlockPos pos, World world ){
        return isInRoom( pos, world, 0 );
    }

    private boolean isInRoom( BlockPos pos, World world, int minSize ){
        return isInRoom( pos, world, minSize, null, 0 );
    }

    private boolean isInRoom( BlockPos pos, World world, Block block, int minBlock ){
        return isInRoom( pos, world, 0, block, minBlock );
    }
}
