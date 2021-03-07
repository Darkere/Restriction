package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.config.RestrictionReader;
import com.davqvist.restriction.reference.Reference;
import com.davqvist.restriction.utility.UtilityHelper;
import com.sun.org.apache.regexp.internal.RE;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class NearbyRestriction implements RestrictionType{
    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID,"nearby");

    @Override
    public boolean test(World world, BlockPos pos, RestrictionReader.RestrictionDescriptor descriptor, PlayerEntity player) {
        int range = descriptor.getAmount();
        String blockString = descriptor.block.name;
        int minAmount = descriptor.block.getCount();
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockString));
        if (block == null) return false;
        int actualRange = Math.min(5, range);
        int count = 0;
        for (int x = -actualRange; x <= actualRange; x++) {
            for (int y = -actualRange; y <= actualRange; y++) {
                for (int z = -actualRange; z <= actualRange; z++) {
                    if (UtilityHelper.matches(world,descriptor.block,world.getBlockState(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z)).getBlock())) {
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

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public String getMessage() {
        boolean reverse = descriptor.getIsReversed();
        int size = descriptor.getAmount();
        int amount = descriptor.block.getCount();
        String blockname = UtilityHelper.getBlockOrTagName(descriptor);
        return "Block must " + ( reverse ? "not " : "" ) + "be surrounded by at least " + amount + " " + blockname + " in a range of " + Math.min( 5, size ) + " blocks.";
    }
}
