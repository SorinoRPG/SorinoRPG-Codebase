package data;

import data.files.Logger;
import game.Coins;
import game.IgnatiamonNotFoundException;
import game.characters.Ignatiamon;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Profile implements Serializable {
    private final ArrayList<Ignatiamon> userIgnatiamon;
    private final Coins coins;
    private final String ID;
    private final String imageUrl;
    private final Guild guild;
    private int wins;
    private int loses;
    private int level = 0;
    private int xpLevelThresh = 100;
    private int xp = 0;


    public Profile(ArrayList<Ignatiamon> ignatiamon, Coins coins,
                   String name, int wins, int loses, String imageUrl, Guild guild){
        this.userIgnatiamon = ignatiamon;
        this.coins = coins;
        this.ID = name;
        this.wins = wins;
        this.loses = loses;
        this.imageUrl = imageUrl;
        this.guild = guild;
    }

    public Ignatiamon getSpecificIgnatiamon(String ignatiamonStr) throws IgnatiamonNotFoundException {
        for(Ignatiamon ignatiamon:
                userIgnatiamon){
            if(ignatiamon.getIgnatiamon(ignatiamonStr).isPresent())
                return ignatiamon;
        }
        throw new IgnatiamonNotFoundException(
                ignatiamonStr + " was not found in the Profile!");
    }
    public String getIgnatiamon(){
        return userIgnatiamon.toString();
    }
    public ArrayList<Ignatiamon> getIgnatiamonAsList(){
        return userIgnatiamon;
    }
    public void addIgnatiamon(Ignatiamon ignatiamon){
        if(userIgnatiamon.contains(ignatiamon))
            return;
        this.userIgnatiamon.add(ignatiamon);
    }
    public void decreaseCoins(int amount){
        coins.decreaseCoins(amount);
    }
    public void increaseCoins(int amount){
        coins.increaseCoins(amount);
    }
    public void incrementWin(){
        this.wins++;
    }
    public void incrementLoss(){
        this.loses++;
    }
    public int getLevel(){
        return level;
    }
    public String getName(){
        return this.ID;
    }

    public void incrementXP(int increment, TextChannel channel){
        xp += increment;

        if(xp >= xpLevelThresh){
            level++;
            xpLevelThresh = (int) Math.floor(xpLevelThresh * 1.5);

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(this.ID + " has advanced to level " + level + "!");
            embedBuilder.setDescription("XP: " + xp + "/" + xpLevelThresh);
            embedBuilder.setImage(imageUrl);

            channel.sendMessage(embedBuilder.build()).queue();
        }
        try {
            recreateProfile();
        } catch (IOException ioException) {
            Logger logger =
                    new Logger("Error in recreating profile \n" +
                            Logger.exceptionAsString(ioException));
            channel.sendMessage(
                    "Could not find profile due to IO and Classes "
            ).queue();
            try{
                logger.logError();
                logger.closeLogger();
            } catch (IOException excI){
                channel.sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        }
    }

    public static Profile[] allProfiles(int size) throws IOException, ClassNotFoundException {
        File directory = new File("/Users/Emman/IdeaProjects/Ignatiamon!/src/main/java/data/files");
        File[] files = directory.listFiles();
        Profile[] profiles = new Profile[size];
        for (int i = 0; i == files.length-1; i++) {
            File file = files[i];
            if(file.getPath().substring(63).startsWith("@@"))
                profiles[i] = readFromFile(file);
        }
        return profiles;
    }

    public static int getUserSize(Guild guild){
        File directory = new File("/Users/Emman/IdeaProjects/Ignatiamon!/src/main/java/data/files");
        File[] files = directory.listFiles();
        int occurrences = 0;
        for (File file : files)
            if (file.getPath().substring(63).startsWith("@@" + guild.getId()))
                occurrences++;
        return occurrences;
    }

    public MessageEmbed showLevel(TextChannel channel){
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("XP: " + xp + "/" + xpLevelThresh + "\t Level: " + level);
        builder.setImage(imageUrl);

        try {
            recreateProfile();
        } catch (IOException ioException) {
            Logger logger =
                    new Logger("Error in recreating profile \n" +
                            Logger.exceptionAsString(ioException));
            channel.sendMessage(
                    "Could not find profile due to IO and Classes "
            ).queue();
            try{
                logger.logError();
                logger.closeLogger();
            } catch (IOException excI){
                channel.sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        }

        return builder.build();
    }

    private static Profile readFromFile(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new
                ObjectInputStream(new FileInputStream(file));
        return (Profile) objectInputStream.readObject();
    }

    public void createProfile() throws IOException {
        FileWriter fileWriter =
                new FileWriter(
                        new File(
                                "/Users/Emman/IdeaProjects/Ignatiamon!/src/main/java/data/files/@" +
                                        guild.getId() + ID + ".txt"
                        )
                );
        fileWriter.write("Ignatiamon: " + userIgnatiamon.toString() + "\n" +
                "Coins: " + coins.getCoins() + "\n" +
                "Wins: " + wins + "\n" +
                "Loses: " + loses + "\n" +
                "Level: " + level + "\t XP -- " + xp + "/" + xpLevelThresh);
        ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(
                        new FileOutputStream(
                                new File(
                                        "/Users/Emman/IdeaProjects/Ignatiamon!/src/main/java/data/files/@@" +
                                        guild.getId()
                                        + ID+ ".txt")
                        )
                );
        try {
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            fileWriter.close();
        } catch (IOException e){
            objectOutputStream.close();
            throw e;
        }
    }
    public void recreateProfile() throws IOException {
        Logger logger = new Logger("Re-Created profile");
        FileWriter fileWriter =
                new FileWriter(
                        new File(
                                "/Users/Emman/IdeaProjects/Ignatiamon!/src/main/java/data/files/@" +
                                        guild.getId()
                                        + ID + ".txt"
                        ),
                        false
                );
        fileWriter.write("Ignatiamon: " + userIgnatiamon.toString() + "\n" +
                "Coins: " + coins.getCoins() + "\n" +
                "Wins: " + wins + "\n" +
                "Loses: " + loses + "\n" +
                "Level: " + level + "\t XP -- " + xp + "/" + xpLevelThresh);
        logger.logAction();
        ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(
                        new FileOutputStream(
                                new File("/Users/Emman/IdeaProjects/Ignatiamon!/src/main/java/data/files/@@"+
                                        guild.getId()
                                        + ID+ ".txt")
                        )
                );
        assert new File("/Users/Emman/IdeaProjects/Ignatiamon!/src/main/java/data/files/@@" + guild.getId()
                + ID+ ".txt").delete();
        try {
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            logger.closeLogger();
            fileWriter.close();
        } catch (IOException ioException){
            objectOutputStream.close();
            throw ioException;
        }

    }
    public static Profile getProfile(GuildMessageReceivedEvent event)
            throws IOException,
            ProfileNotFoundException, ClassNotFoundException {
        ObjectInputStream objectInputStream =
                new ObjectInputStream(new FileInputStream(
                        new File("/Users/Emman/IdeaProjects/Ignatiamon!/src/main/java/data/files/@@"+
                                event.getGuild().getId()
                                + event.getAuthor().getName() + ".txt")
                ));
        if(!new File("/Users/Emman/IdeaProjects/Ignatiamon!/src/main/java/data/files/@@"+
                event.getGuild().getId()
                + event.getAuthor().getName() + ".txt").exists()){
            objectInputStream.close();
            throw new ProfileNotFoundException(" The user does not have a created profile");
        }

        try {
            return (Profile) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
            objectInputStream.close();
            throw e;
        }
    }
    public static Profile getProfile(User author, GuildMessageReceivedEvent event)throws IOException,
            ProfileNotFoundException, ClassNotFoundException {
        ObjectInputStream objectInputStream =
                new ObjectInputStream(new FileInputStream(
                        new File("/Users/Emman/IdeaProjects/Ignatiamon!/src/main/java/data/files/@@"+
                                event.getGuild().getId()
                                + author.getName() + ".txt"
                )));
        if (!new File("/Users/Emman/IdeaProjects/Ignatiamon!/src/main/java/data/files/@@"+
                event.getGuild().getId()
                + author.getName() + ".txt").exists()) {
            objectInputStream.close();
            throw new ProfileNotFoundException(" The user does not have a created profile");
        }

        try {
            return (Profile) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
            objectInputStream.close();
            throw e;
        }
    }

    public static void eraseProfile(User erasedUser, GuildMessageReceivedEvent event){
        File toBeDeleted = new File("/Users/Emman/IdeaProjects/Ignatiamon!/src/main/java/data/files/@"+
                event.getGuild().getId()
                + erasedUser.getName() + ".txt");
        if (toBeDeleted.delete()){
            toBeDeleted = new File("/Users/Emman/IdeaProjects/Ignatiamon!/src/main/java/data/files/@@"+
                    event.getGuild().getId()
                    + erasedUser.getName() + ".txt");
            assert toBeDeleted.delete();
        }
    }


    // TODO Fix algorithm
    public static ArrayList<String> getProfileAsString(User author, GuildMessageReceivedEvent event)
            throws ProfileNotFoundException {
        try {
            ArrayList<String> profileInfo = new ArrayList<>();
            File myObj = new File("/Users/Emman/IdeaProjects/Ignatiamon!/src/main/java/data/files/@"+
                    event.getGuild().getId()
                    + author.getName() + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) profileInfo.add(myReader.nextLine());
            profileInfo.set(0, profileInfo.get(0).strip().substring(0, profileInfo.get(0).length()-1));
            System.out.println(profileInfo.get(0).strip().substring(0, profileInfo.get(0).length()-1));

            return profileInfo;
        } catch (FileNotFoundException e) {
            throw new ProfileNotFoundException("Profile not found");
        }
    }
    @Override
    public String toString() {
        return "Ignatiamon: " + userIgnatiamon.toString() + "\n" +
                "Coins: " + coins.getCoins() + "\n" +
                "Wins: " + wins + "\n" +
                "Loses: " + loses + "\n" +
                "Level: " + level + "\t XP -- " + xp + "/" + xpLevelThresh;
    }
}
