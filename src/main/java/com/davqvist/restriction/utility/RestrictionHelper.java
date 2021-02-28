package com.davqvist.restriction.utility;

import com.davqvist.restriction.config.RestrictionReader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.player.PlayerEntity;
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

    public static LAST_IN_ROOM_ERROR_TYPE LAST_IN_ROOM_ERROR;
    public static int LAST_IN_ROOM_ERROR_AMOUNT = 0;

    public static boolean isInRoom(BlockPos pos, World world, RestrictionReader.RestrictionDescriptor desc) {
        int minSize = desc.getAmount();
        int minAmount = desc.block.getCount();
        Stack<BlockPos> stack = new Stack<>();
        stack.push(pos);
        final int maxSize = 10000;
        final HashSet<BlockPos> addableBlocks = new HashSet<>();
        final HashSet<BlockPos> foundBlocks = new HashSet<>();

        while (!stack.isEmpty()) {
            BlockPos stackElement = stack.pop();
            addableBlocks.add(stackElement);
            for (Direction direction : Direction.values()) {
                BlockPos searchNextPosition = stackElement.offset(direction);
                BlockState state = world.getBlockState(searchNextPosition);
                if (!addableBlocks.contains(searchNextPosition)) {
                    if (addableBlocks.size() <= maxSize) {
                        if (!isWallBlock(world, searchNextPosition, state, direction)) {
                            stack.push(searchNextPosition);
                        } else if (UtilityHelper.matches(world, desc.block, world.getBlockState(searchNextPosition).getBlock())) {
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

    private static boolean isWallBlock(World world, BlockPos pos, BlockState state, Direction direction) {
        if (state.getBlock() instanceof DoorBlock) return true;
        return state.isSolidSide(world, pos, direction.getOpposite())
            && state.isSolidSide(world, pos, direction)
            && state.isSolid();
    }
}
