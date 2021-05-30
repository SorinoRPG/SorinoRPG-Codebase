package game.heist;

import data.Profile;

public interface Reward {
    String desc();
    void award(Profile profile);
}
