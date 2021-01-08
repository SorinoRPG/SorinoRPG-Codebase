package data;

import data.logging.Logger;
import game.value.Coins;
import game.SorinoNotFoundException;
import game.characters.Sorino;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Profile implements Serializable {
    private final ArrayList<Sorino> userSorino;
    private final Coins coins;
    private final String ID;
    private String name;
    private String imageUrl;
    private final String guildID;
    private int wins;
    private int loses;
    private int level;
    private int xpLevelThresh;
    private int xp;

    public Profile(ArrayList<Sorino> sorino, Coins coins,
                   String id, String name, int wins, int loses, String imageUrl, String guildID){
        this.userSorino = sorino;
        this.coins = coins;
        this.ID = id;
        this.name = name;
        this.wins = wins;
        this.loses = loses;
        this.imageUrl = imageUrl;
        this.guildID = guildID;
        this.level = 0;
        this.xpLevelThresh = 500;
        this.xp = 0;
    }
    public Profile(ArrayList<Sorino> sorino, Coins coins,
                   String id, String name, int wins, int loses, String imageUrl, String guildID,
                   int level, int xpLevelThresh, int xp){
        this.userSorino = sorino;
        this.coins = coins;
        this.ID = id;
        this.name = name;
        this.wins = wins;
        this.loses = loses;
        this.imageUrl = imageUrl;
        this.guildID = guildID;
        this.level = level;
        this.xpLevelThresh = xpLevelThresh;
        this.xp = xp;
    }

    public Sorino getSpecificSorino(String sorinoStr) throws SorinoNotFoundException {
        for(Sorino sorino :
                userSorino){
            if(sorino.getSorino(sorinoStr).isPresent())
                return sorino;
        }
        throw new SorinoNotFoundException(
                sorinoStr + " was not found in the Profile!");
    }
    public ArrayList<Sorino> getSorinoAsList(){
        return userSorino;
    }
    public void addSorino(Sorino sorino){
        for (Sorino sor:
             userSorino) {
            if(sor.getName().equals(sorino.getName())) return;
        }
        this.userSorino.add(sorino);
    }
    public void setCoins(int amount){
        coins.setCoins(amount);
    }
    public boolean spend(int amount){
        return !coins.spend(amount);
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
        return this.name;
    }

    public void silentXPIncrement(int increment){
        xp += increment;

        if(xp >= xpLevelThresh){
            level++;
            xpLevelThresh = (int) Math.floor(xpLevelThresh * 1.25);
            xp = 0;
        }
        try {
            recreateProfile();
        } catch (IOException ioException) {
            Logger logger =
                    new Logger("Error in recreating profile \n" +
                            Logger.exceptionAsString(ioException));
            try{
                logger.logError();
            } catch (IOException excI){
                excI.printStackTrace();
            }
        }
    }
    public void incrementXP(int increment, GuildMessageReceivedEvent event){
        xp += increment;
        try {
            if(xp >= xpLevelThresh){
                TextChannel channel = event.getChannel();
                ArrayList<File> guild =
                        (ArrayList<File>)
                        Arrays.asList(new File("/db/" + event.getGuild().getId()).listFiles());
                if(guild.contains(new File("/db/" + event.getGuild().getId() + "/CHANNEL.txt"))){
                    Scanner scanner = new Scanner(
                            new FileInputStream(
                                    new File("/db/" + event.getGuild().getId() + "/CHANNEL.txt")
                            )
                    );
                    channel = event.getJDA().getTextChannelById(scanner.nextLine());
                }

                level++;
                xpLevelThresh = (int) Math.floor(xpLevelThresh * 1.25);
                xp = 0;

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(0x000dff);
                embedBuilder.setTitle(this.name + " has advanced to trainer level " + level + "!");
                embedBuilder.setDescription("XP: " + xp + "/" + xpLevelThresh);
                embedBuilder.setImage(imageUrl);

                assert channel != null;
                channel.sendMessage(embedBuilder.build()).queue();
            }
            recreateProfile();
        } catch (IOException ioException) {
            Logger logger =
                    new Logger("Error: " +
                            Logger.exceptionAsString(ioException));
            try{
                logger.logError();
            } catch (IOException excI){
                excI.printStackTrace();
            }
        }
    }

    public static ArrayList<Profile> getProfiles(String guildID) throws IOException, ClassNotFoundException {
        File directory = new File
                ("/db/" + guildID);

        List<File> files = new ArrayList<>(Arrays.asList(directory.listFiles(File::isFile)));
        files = files.parallelStream().filter((file) -> file.getName().startsWith("@@"))
                .collect(Collectors.toList());
        ArrayList<Profile> profiles = new ArrayList<>();

        for (File file : files) {
            profiles.add(Profile.readFromFile(file));
        }
        return profiles;
    }

    public MessageEmbed showLevel(TextChannel channel){
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(0x000dff);
        builder.setTitle("XP: " + xp + "/" + xpLevelThresh + "\t Trainer Level: " + level);
        builder.setImage(imageUrl);

        try {
            recreateProfile();
        } catch (IOException ioException) {
            Logger logger =
                    new Logger("Error in recreating profile \n" +
                            Logger.exceptionAsString(ioException));
            channel.sendMessage(
                    "Could not find profile due to a SorinoRPG server error." +
                            " These could have been the causes:\n\n" +
                            "1. Your account is being used in something else," +
                            " most likely in a fight. You can end a fight with this command, (.FEND) so" +
                            " you can use your account.\n\n" +
                            "2. Your account is currently being reviewed or processed, this could be due" +
                            " to suspicious activity or receiving an award of some sort. For the latter," +
                            " you will be able to use your account in a minute or so. For the former," +
                            " this may happen regularly on and off until we can take further action.\n\n" +
                            "3. There was a server issue, please report this to our email " +
                            "SorinoRPG@gmail.com or mention us on twitter @Rpgsorino"
            ).queue(message ->
                    message.delete().queueAfter(25, TimeUnit.SECONDS)
            );
            try{
                logger.logError();
            } catch (IOException excI){
                excI.printStackTrace();
            }
        }

        return builder.build();
    }

    public static Profile readFromFile(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new
                ObjectInputStream(new FileInputStream(file));
        return (Profile) objectInputStream.readObject();
    }

    public void createProfile() throws IOException {
        ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(
                        new FileOutputStream(
                                new File(
                                        "/db/" +
                                        guildID + "/@@"
                                        + ID+ ".txt")
                        )
                );
        try {
            objectOutputStream.writeObject(this);

            objectOutputStream = new ObjectOutputStream(
                    new FileOutputStream(
                            new File("/db/" +
                                    guildID + "/UPDATE_STORE/$"
                                    + ID+ ".txt")
                    )
            );
            objectOutputStream.writeObject(new ProfileStore(userSorino, coins, ID, name, wins, loses,
                    imageUrl, guildID, xp, xpLevelThresh, level));
            objectOutputStream.close();
        } catch (IOException e){
            objectOutputStream.close();
            throw e;
        }
    }
    public void recreateProfile() throws IOException {
        Logger logger = new Logger("Re-Created profile");
        logger.logAction();
        ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(
                        new FileOutputStream(
                                new File("/db/" +
                                        guildID + "/@@"
                                        + ID+ ".txt"), false
                        )
                );
        try {
            objectOutputStream.writeObject(this);
            objectOutputStream = new ObjectOutputStream(
                    new FileOutputStream(
                            new File("/db/" +
                                    guildID + "/UPDATE_STORE/$"
                                    + ID+ ".txt")
                    )
            );
            objectOutputStream.writeObject(new ProfileStore(userSorino, coins, ID, name, wins, loses,
                    imageUrl, guildID, xp, xpLevelThresh, level));
            objectOutputStream.close();
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
                        new File("/db/" +
                                event.getGuild().getId() + "/@@"
                                + event.getAuthor().getId() + ".txt")
                ));
        if(!new File("/db/" +
                event.getGuild().getId() + "/@@"
                + event.getAuthor().getId() + ".txt").exists()){
            objectInputStream.close();
            throw new ProfileNotFoundException();
        }

        try {
            Profile profile = (Profile) objectInputStream.readObject();
            profile.name = event.getAuthor().getName();
            profile.imageUrl = event.getAuthor().getAvatarUrl();

            return profile;
        } catch (IOException | ClassNotFoundException e){
            objectInputStream.close();
            throw e;
        }
    }
    public static Profile getProfile(User author, GuildMessageReceivedEvent event) throws IOException,
            ProfileNotFoundException, ClassNotFoundException {
        ObjectInputStream objectInputStream =
                new ObjectInputStream(new FileInputStream(
                        new File("/db/" +
                                event.getGuild().getId() + "/@@"
                                + author.getId() + ".txt"
                )));
        if (!new File("/db/" +
                event.getGuild().getId() + "/@@"
                + author.getId() + ".txt").exists()) {
            objectInputStream.close();
            throw new ProfileNotFoundException();
        }

        try {
            Profile profile = (Profile) objectInputStream.readObject();
            profile.name = event.getAuthor().getName();
            profile.imageUrl = event.getAuthor().getAvatarUrl();

            return profile;
        } catch (IOException | ClassNotFoundException e){
            objectInputStream.close();
            throw e;
        }
    }

    public static Profile storeToProfile(ProfileStore profileStore){
        return new Profile(Sorino.AllSorino.strToList(profileStore.userSorino),
                new Coins(profileStore.coins), profileStore.ID,
                profileStore.name, profileStore.wins, profileStore.loses,
                profileStore.imageUrl,
                profileStore.guildID,
                profileStore.level,
                profileStore.xpLevelThresh,
                profileStore.xp);
    }


    @Override
    public String toString() {
        return "Sorino: " + userSorino.toString() + "\n" +
                "Coins: " + coins.getCoins() + "\n" +
                "Wins: " + wins + "\n" +
                "Loses: " + loses + "\n" +
                "Trainer Level: " + level + "\n XP -- " + xp + "/" + xpLevelThresh;
    }
}
