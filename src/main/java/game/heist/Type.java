package game.heist;

public enum Type {
    PREP_STEALTH("Stealth Preparation",
            "A decent amount of low explosives, a few medium technicals, at least 1 medium " +
                    "defensive and a couple of medium weapons."),
    PREP_AGGRESSIVE("Aggressive Preparation",
            "A plethora of medium explosives is highly recommended, at least 2 medium " +
                    "weapons, and a few low defensive."),
    PREP_SWINDLE("Swindle Preparation","A good amount of medium technicals," +
            " a decent amount of medium defensive, a medium vehicle and at least 2 low weapons"),
    HEIST_STEALTH("Stealth Heist",
            "At least 1 low explosive per participant, a plethora of high technicals, at least " +
                    "2 high defensive, a high vehicle and 1 high weapon."),
    HEIST_AGGRESSIVE("Aggressive Heist",
            "At least 1 high explosive per participant, plenty of high weapons, a high vehicle, " +
                    "a high defensive and a few medium technicals."),
    HEIST_SWINDLE("Swindle Heist",
            "At least 2 high vehicles, a high weapon and some low explosives.");

    String recommendedItemSet;
    String name;

    Type(String name, String recommendedItemSet){
        this.name = name;
        this.recommendedItemSet = recommendedItemSet;
    }

    public String getName() {
        return name;
    }

    public String getRecommendedItemSet() {
        return recommendedItemSet;
    }
}
