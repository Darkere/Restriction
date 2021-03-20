package com.davqvist.restriction.RestrictionTypes;

import com.davqvist.restriction.Restriction;
import com.davqvist.restriction.RestrictionReader;
import com.davqvist.restriction.utility.MatchHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class NearbyRestriction extends RestrictionType {
    public static final ResourceLocation ID = new ResourceLocation(Restriction.MOD_ID,"nearby");

    public NearbyRestriction(RestrictionReader.Descriptor descriptor) {
        super(descriptor);
    }

    @Override
    public boolean test(World world, BlockPos pos, PlayerEntity player) {
        int range = descriptor.getAmount();
        String blockString = descriptor.name;
        int minAmount = descriptor.getAmount();
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockString));
        if (block == null) return false;
        int actualRange = Math.min(5, range);
        int count = 0;
        for (int x = -actualRange; x <= actualRange; x++) {
            for (int y = -actualRange; y <= actualRange; y++) {
                for (int z = -actualRange; z <= actualRange; z++) {
                    if (MatchHelper.matches(world,descriptor,world.getBlockState(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z)).getBlock())) {
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
        boolean reverse = descriptor.IsReversed();
        int size = descriptor.getAmount();
        int amount = descriptor.getExposed();
        String blockname = descriptor.getApplicatorString();
        return "Block must " + ( reverse ? "not " : "" ) + "be surrounded by at least " + amount + " " + blockname + " in a range of " + Math.min( 5, size ) + " blocks.";
    }
}
