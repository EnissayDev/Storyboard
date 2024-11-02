package org.enissay.osu.data.curves;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum HitObjectType {
    HITCIRCLE(1),
    SLIDER(2),
    NEW_COMBO(4),
    SPINNER(8),
    HOLD_NOTE(128);

    private int id;

    HitObjectType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static boolean hasFlag(int access, int flag) {
        return (access & flag) == flag;
    }

    // Method to get a set of HitObjectTypes based on the id flags
    // Method to get one HitObjectType based on the id flags
    public static HitObjectType fromID(int id) {
        // Loop through each HitObjectType and return the first one whose flag is present in the id
        for (HitObjectType type : HitObjectType.values()) {
            if (hasFlag(id, type.getId())) {
                return type;  // Return the first match
            }
        }
        return null;  // Return null if no match is found
    }
}