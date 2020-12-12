package data;

import data.files.Logger;
import game.Coins;
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
import java.util.stream.Collectors;

public class Profile implements Serializable {
    private final ArrayList<Sorino> userSorino;
    private final Coins coins;
    private final String ID;
    private final String name;
    private final String imageUrl;
    private final String guildID;
    private int wins;
    private int loses;
    private int level = 0;
    private int xpLevelThresh = 250;
    private int xp = 0;


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
    public String getSorino(){
        return userSorino.toString();
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

    public void incrementXP(int increment, TextChannel channel){
        xp += increment;

        if(xp >= xpLevelThresh){
            level++;
            xpLevelThresh = (int) Math.floor(xpLevelThresh * 1.5);
            xp = 0;

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(this.name + " has advanced to trainer level " + level + "!");
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
            } catch (IOException excI){
                channel.sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        }
    }

    public static ArrayList<Profile> getProfiles(String guildID) throws IOException, ClassNotFoundException {
        File directory = new File
                ("/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                "/src/main/java/data/files/" + guildID);

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
                            " most likely in a fight. You can end a fight with this command, (-=END) so" +
                            " you can use your account.\n\n" +
                            "2. Your account is currently being reviewed or processed, this could be due" +
                            " to suspicious activity or receiving an award of some sort. For the latter," +
                            " you will be able to use your account in a minute or so. For the former," +
                            " this may happen regularly on and off until we can take further action.\n\n" +
                            "3. There was a server issue, please report this to our email " +
                            "SorinoRPG@gmail.com or mention us on twitter @Rpgsorino"
            ).queue();
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
        FileWriter fileWriter =
                new FileWriter(
                        new File(
                                "/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                                        "/src/main/java/data/files/" +
                                        guildID + "/@" + ID + ".txt"
                        )
                );
        fileWriter.write("Sorino: " + userSorino.toString() + "\n" +
                "Coins: " + coins.getCoins() + "\n" +
                "Wins: " + wins + "\n" +
                "Loses: " + loses + "\n" +
                "Trainer Level: " + level + "\n XP -- " + xp + "/" + xpLevelThresh);
        ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(
                        new FileOutputStream(
                                new File(
                                        "/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                                                "/src/main/java/data/files/" +
                                        guildID + "/@@"
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
                                "/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                                        "/src/main/java/data/files/" +
                                        guildID + "/@" + ID + ".txt"
                        ),
                        false
                );
        fileWriter.write("Sorino: " + userSorino.toString() + "\n" +
                "Coins: " + coins.getCoins() + "\n" +
                "Wins: " + wins + "\n" +
                "Loses: " + loses + "\n" +
                "Trainer Level: " + level + "\n XP -- " + xp + "/" + xpLevelThresh);
        logger.logAction();
        ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(
                        new FileOutputStream(
                                new File("/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                                        "/src/main/java/data/files/" +
                                        guildID + "/@@"
                                        + ID+ ".txt")
                        )
                );
        assert new File("/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                "/src/main/java/data/files/" + guildID + "/@@"
                + ID+ ".txt").delete();
        try {
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
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
                        new File("/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                                "/src/main/java/data/files/" +
                                event.getGuild().getId() + "/@@"
                                + event.getAuthor().getId() + ".txt")
                ));
        if(!new File("/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                "/src/main/java/data/files/" +
                event.getGuild().getId() + "/@@"
                + event.getAuthor().getId() + ".txt").exists()){
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
                        new File("/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                                "/src/main/java/data/files/" +
                                event.getGuild().getId() + "/@@"
                                + author.getId() + ".txt"
                )));
        if (!new File("/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                "/src/main/java/data/files/" +
                event.getGuild().getId() + "/@@"
                + author.getId() + ".txt").exists()) {
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
        File toBeDeleted = new File("/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                "/src/main/java/data/files/" +
                event.getGuild().getId() + "/@"
                + erasedUser.getId() + ".txt");
        if (toBeDeleted.delete()){
            toBeDeleted = new File("/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                    "/src/main/java/data/files/" +
                    event.getGuild().getId() + "/@@"
                    + erasedUser.getId() + ".txt");
            assert toBeDeleted.delete();
        }
    }


    // TODO Fix algorithm
    public static ArrayList<String> getProfileAsString(User author, GuildMessageReceivedEvent event)
            throws ProfileNotFoundException {
        try {
            ArrayList<String> profileInfo = new ArrayList<>();
            File myObj = new File("/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                    "/src/main/java/data/files/" +
                    event.getGuild().getId() + "/@"
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
        return "Sorino: " + userSorino.toString() + "\n" +
                "Coins: " + coins.getCoins() + "\n" +
                "Wins: " + wins + "\n" +
                "Loses: " + loses + "\n" +
                "Trainer Level: " + level + "\n XP -- " + xp + "/" + xpLevelThresh;
    }
}
