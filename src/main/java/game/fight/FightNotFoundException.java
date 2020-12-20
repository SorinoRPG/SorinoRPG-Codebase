package game.fight;

import data.files.Logger;

public class FightNotFoundException extends Exception{
    String fightCauses;

    FightNotFoundException(String fightCauses, Exception ... e){
        StringBuilder fightCausesBuilder = new StringBuilder(fightCauses + "\n");
        for (Exception exception : e)
            fightCausesBuilder.append(Logger.exceptionAsString(exception)).append("\n");
        this.fightCauses = fightCausesBuilder.toString();
    }

    @Override
    public String toString() {
        return fightCauses;
    }
}
