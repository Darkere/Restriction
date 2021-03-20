package com.davqvist.restriction.utility;

import com.davqvist.restriction.Restriction;
import com.davqvist.restriction.RestrictionReader;

public class MessageHelper {

    public static String getNot(RestrictionReader.Descriptor desc) {
        return desc.IsReversed() ? "not" : "";
    }

    public static String getName(RestrictionReader.Descriptor desc) {
        switch (desc.getApplicator()) {
            case NAME: return getBlockItem(desc) + "called " + desc.name;
            case TAG: return getBlockItem(desc) + "in Tag " + desc.tag;
            case MOD: return getBlockItem(desc) + "in Mod" + desc.mod;
        }
        return "";
    }

    public static String getName2(RestrictionReader.Descriptor desc) {
        switch (desc.getApplicator()) {
            case NAME: return getBlockItem(desc) + "called " + desc.name;
            case TAG: return getBlockItem(desc) + "in Tag " + desc.tag;
            case MOD: return getBlockItem(desc) + "in Mod" + desc.mod;
        }
        return "";
    }

    public static String getBlockItem(RestrictionReader.Descriptor desc) {
        if (desc.isBlockRestriction()) {
            if (desc.isItemRestriction()) {
                return "Block/Item";
            } else {
                return "Block";
            }
        } else {
            if (desc.isItemRestriction()) {
                return "Item";
            }
        }
        return "";
    }
}
