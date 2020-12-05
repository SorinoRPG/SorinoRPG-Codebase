package game.fight;

import game.characters.Sorino;
import net.dv8tion.jda.api.entities.User;

import java.io.*;
import java.util.ArrayList;

public class Fight {
    public static ArrayList<Sorino> currentFighters = new ArrayList<>();
    public static ArrayList<Sorino.DeadSorino> deadSorino = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Opponent> opponents = new ArrayList<>();

    void saveFight() throws FileNotFoundException {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    new FileOutputStream(
                            new File("")
                    )
            );
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
