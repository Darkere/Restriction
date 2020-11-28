package com.davqvist.restriction.utility;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Stack;

public class RestrictionHelper {

    public static enum LAST_IN_ROOM_ERROR_TYPE {CLOSED, SIZE, BLOCKS}

    ;
    public static LAST_IN_ROOM_ERROR_TYPE LAST_IN_ROOM_ERROR;
    public static int LAST_IN_ROOM_ERROR_AMOUNT = 0;

    public static boolean canSeeSky(BlockPos pos, IWorld world) {
        return world.canBlockSeeSky(pos);
    }

    public static boolean isInRoom(BlockPos pos, World world, int minSize, String blockString, boolean ignoreMeta, int meta, int minAmount) {
        Stack<BlockPos> stack = new Stack<BlockPos>();
        stack.push(pos);
        final int maxSize = 10000;
        final HashSet<BlockPos> addableBlocks = new HashSet<BlockPos>();
        final HashSet<BlockPos> foundBlocks = new HashSet<BlockPos>();

        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockString));

        while (!stack.isEmpty()) {
            BlockPos stackElement = stack.pop();
            addableBlocks.add(stackElement);
            for (Direction direction : Direction.values()) {
                BlockPos searchNextPosition = stackElement.offset(direction);
                BlockState state = world.getBlockState(searchNextPosition);
                if (!addableBlocks.contains(searchNextPosition)) {
                    if (addableBlocks.size() <= maxSize) {
                        if (!state.isSolidSide(world, searchNextPosition, direction.getOpposite())
                                && !state.isSolidSide(world, searchNextPosition, direction)
                                && state.isSolid()
                                && !(state.getBlock() instanceof DoorBlock)
                                && ((DoorBlock) state.getBlock()).isOpen(state)
                                && state.get(BlockStateProperties.FACING) == direction
                                || state.get(BlockStateProperties.FACING) == direction.getOpposite()) {

                            stack.push(searchNextPosition);
                        } else if (block != null && (world.getBlockState(searchNextPosition).getBlock() == block) && !foundBlocks.contains(searchNextPosition)) {
                            foundBlocks.add(searchNextPosition);
                        }
                    } else {
                        LAST_IN_ROOM_ERROR = LAST_IN_ROOM_ERROR_TYPE.CLOSED;
                        return false;
                    }
                }
            }
        }
        if (foundBlocks.size() < minAmount) {
            LAST_IN_ROOM_ERROR = LAST_IN_ROOM_ERROR_TYPE.BLOCKS;
            LAST_IN_ROOM_ERROR_AMOUNT = minAmount - foundBlocks.size();
        }
        if (addableBlocks.size() < minSize) {
            LAST_IN_ROOM_ERROR = LAST_IN_ROOM_ERROR_TYPE.SIZE;
            LAST_IN_ROOM_ERROR_AMOUNT = minSize - addableBlocks.size();
        }
        return (addableBlocks.size() >= minSize && foundBlocks.size() >= minAmount);
    }

    public static boolean isNearby(BlockPos pos, World world, int range, String blockString, boolean ignoreMeta, int meta, int minAmount) {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockString));
        if (block == null) return false;
        int actualRange = Math.min(5, range);
        int count = 0;
        for (int x = -actualRange; x <= actualRange; x++) {
            for (int y = -actualRange; y <= actualRange; y++) {
                for (int z = -actualRange; z <= actualRange; z++) {
                    if (block.equals(world.getBlockState(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z)).getBlock())) {
                        count++;
                    }
                    if (count >= minAmount) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasLevels(PlayerEntity player, int minAmount) {
        if (minAmount > 0) {
            return (player != null && player.experienceLevel >= minAmount);
        }
        return true;
    }

    public static boolean hasMinHeight(BlockPos pos, int minAmount) {
        if (minAmount >= 0) {
            return (pos.getY() >= minAmount);
        }
        return true;
    }

    public static boolean isInDimension(ServerWorld world, String dim) {
        return world.getDimensionKey().getRegistryName().equals(new ResourceLocation(dim));
    }
}
