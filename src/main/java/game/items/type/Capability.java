package game.items.type;

import java.util.ArrayList;
import java.util.EnumSet;

public enum Capability {
    HIGH_EXPLOSIVE(100),
    MEDIUM_EXPLOSIVE(50),
    LOW_EXPLOSIVE(25),
    HIGH_TECHNICAL(100),
    MEDIUM_TECHNICAL(50),
    LOW_TECHNICAL(25),
    HIGH_VEHICLE(100),
    MEDIUM_VEHICLE(50),
    LOW_VEHICLE(25),
    HIGH_WEAPON(100),
    MEDIUM_WEAPON(50),
    LOW_WEAPON(25),
    HIGH_DEFENSIVE(100),
    MEDIUM_DEFENSIVE(50),
    LOW_DEFENSIVE(25),
    NULL_CAPABILITY(0);

    int effect;

    Capability(int effect){
        this.effect = effect;
    }

    public int getEffect() {
        return effect;
    }

    public static Capability getCapability(String str){
        ArrayList<Capability> capabilityArrayList = new ArrayList<>(EnumSet.allOf(Capability.class));
        for(Capability capability : capabilityArrayList){
            if(capability.toString().equals(str)) return capability;
        }
        return Capability.NULL_CAPABILITY;
    }
}
