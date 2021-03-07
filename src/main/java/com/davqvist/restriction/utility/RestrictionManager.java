package com.davqvist.restriction.utility;

import com.davqvist.restriction.Restriction;
import com.davqvist.restriction.RestrictionTypes.RestrictionType;
import com.davqvist.restriction.config.RestrictionReader;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class RestrictionManager {
    public static final RestrictionManager INSTANCE = new RestrictionManager();
    //TODO mod restictions?
    //TODO as Datapack???
    private final Map<ResourceLocation, RestrictionType> USEMAP = new HashMap<>();
    private final Map<ResourceLocation, RestrictionType> PLACEMAP = new HashMap<>();

    public void addRestriction(RestrictionReader.RestrictionDescriptor descriptor) {

    }
}
