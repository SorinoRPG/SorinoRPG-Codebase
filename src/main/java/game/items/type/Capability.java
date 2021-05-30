package game.items.type;

import java.util.ArrayList;
import java.util.EnumSet;

public enum Capability {
    HIGH_EXPLOSIVE(100, 0),
    MEDIUM_EXPLOSIVE(50, 50),
    LOW_EXPLOSIVE(25, 100),
    HIGH_TECHNICAL(100, 0),
    MEDIUM_TECHNICAL(50, 50),
    LOW_TECHNICAL(25, 100),
    HIGH_VEHICLE(100, 0),
    MEDIUM_VEHICLE(50, 50),
    LOW_VEHICLE(25, 100),
    HIGH_WEAPON(100, 0),
    MEDIUM_WEAPON(50, 50),
    LOW_WEAPON(25, 100),
    HIGH_DEFENSIVE(100, 0),
    MEDIUM_DEFENSIVE(50, 50),
    LOW_DEFENSIVE(25, 100),
    NULL_CAPABILITY(0, 0);

    int effect;
    int rarity;

    Capability(int effect, int rarity){
        this.effect = effect;
        this.rarity = rarity;
    }

    public int getEffect() {
        return effect;
    }
    public int getRarity() {
        return rarity;
    }

    public static Capability getCapability(String str){
        ArrayList<Capability> capabilityArrayList = new ArrayList<>(EnumSet.allOf(Capability.class));
        for(Capability capability : capabilityArrayList){
            if(capability.toString().equals(str)) return capability;
        }
        return Capability.NULL_CAPABILITY;
    }
}
