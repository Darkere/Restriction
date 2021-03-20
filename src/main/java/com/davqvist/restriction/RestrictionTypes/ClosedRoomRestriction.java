package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.Restriction;
import com.davqvist.restriction.RestrictionReader;
import com.davqvist.restriction.utility.MatchHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Stack;

import static com.davqvist.restriction.utility.MessageHelper.*;

public class ClosedRoomRestriction extends RestrictionType {
    public static final ResourceLocation ID = new ResourceLocation(Restriction.MOD_ID, "closedroom");

    public ClosedRoomRestriction(RestrictionReader.Descriptor descriptor) {
        super(descriptor);
    }


    @Override
    public boolean test(World world, BlockPos pos, PlayerEntity player) {
        int minSize = descriptor.getAmount();
        int minAmount = descriptor.getAmount();
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
                        } else if (MatchHelper.matches(world, descriptor, world.getBlockState(searchNextPosition).getBlock())) {
                            foundBlocks.add(searchNextPosition);
                        }
                    } else {
                        player.sendStatusMessage(new StringTextComponent("Block must be placed in a closed room!"), true);
                        return false;
                    }
                }
            }
        }

        if (foundBlocks.size() < minAmount) {
            player.sendStatusMessage(new StringTextComponent("Room must be " + (minAmount - foundBlocks.size()) + " blocks bigger"), true);
        }

        if (addableBlocks.size() < minSize) {
            player.sendStatusMessage(new StringTextComponent("Room must be " + (minSize - addableBlocks.size()) + " blocks smaller"), true);
        }

        return (addableBlocks.size() >= minSize && foundBlocks.size() >= minAmount);
    }

    private static boolean isWallBlock(World world, BlockPos pos, BlockState state, Direction direction) {
        if (state.getBlock() instanceof DoorBlock) return true;
        return state.isSolidSide(world, pos, direction.getOpposite())
                && state.isSolidSide(world, pos, direction)
                && state.isSolid();
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public String getMessage() {
        return getBlockItem(descriptor) + " must " + getNot(descriptor) + " be in closed room with a space of at least " + descriptor.getAmount() + " blocks" + ( descriptor.getExposed() > 0  ? ( " and at least " + descriptor.getExposed() + " exposed " + descriptor.name2 + "" ) : "" ) + ".";
    }
}
