package game.fight;

import data.logging.Logger;

public class FightNotFoundException extends Exception{
    String fightCauses;

    public FightNotFoundException(Exception... e){
        StringBuilder fightCausesBuilder = new StringBuilder("Fight was not created" + "\n");
        for (Exception exception : e)
            fightCausesBuilder.append(Logger.exceptionAsString(exception)).append("\n");
        this.fightCauses = fightCausesBuilder.toString();
    }

    @Override
    public String toString() {
        return fightCauses;
    }
}
