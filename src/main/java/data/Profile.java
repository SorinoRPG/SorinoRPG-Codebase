package data;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import data.logging.Logger;
import game.SorinoNotFoundException;
import game.characters.Sorino;
import main.userinterface.Prefix;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.*;
import java.util.*;


import org.bson.Document;

public class Profile {
    private final ArrayList<Sorino> userSorino;
    private int coins;
    private final String ID;
    private String name;
    private String imageUrl;
    private final String guildID;
    private int wins;
    private int loses;
    private int level;
    private int xpLevelThresh;
    private int xp;

    public Profile(ArrayList<Sorino> sorino, int coins,
                   String id, String name, int wins, int loses, String imageUrl, String guildID) {
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

    public Profile(ArrayList<Sorino> sorino, int coins,
                   String id, String name, int wins, int loses, String imageUrl, String guildID, int xp,
                   int xpLevelThresh, int level) {
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
        for (Sorino sorino : userSorino) {
            if (sorino.getSorino(sorinoStr).isPresent())
                return sorino;

        }
        throw new SorinoNotFoundException(
                sorinoStr + " was not found in the Profile!");
    }

    public ArrayList<Sorino> getSorinoAsList() {
        return userSorino;
    }

    public void addSorino(Sorino sorino) {
        for (Sorino sor :
                userSorino) {
            if (sor.getName().equals(sorino.getName())) return;
        }
        this.userSorino.add(sorino);
    }

    public void removeSorino(Sorino sorino) {
        for (int i = 0; i < this.userSorino.size(); i++) {
            Sorino posSorino = this.userSorino.get(i);

            if (posSorino.getName().equalsIgnoreCase(sorino.getName())) {
                this.userSorino.remove(i);
                return;
            }
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getGuildID() {
        return guildID;
    }

    public void setCoins(int amount) {
        coins += amount;
    }

    public boolean spend(int amount) {
        if(coins - amount < 0) return false;
        coins -= amount;
        return true;
    }

    public void incrementWin() {
        this.wins++;
    }

    public void incrementLoss() {
        this.loses++;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return this.name;
    }

    public void incrementXP(int increment, GuildMessageReceivedEvent event) {
        xp += increment;
        try {
            if (xp >= xpLevelThresh) {
                TextChannel channel = event.getChannel();
                level++;
                xpLevelThresh = (int) Math.floor(xpLevelThresh * 1.25);
                xp = 0;
                MongoDatabase database = Mongo.mongoClient().getDatabase("guild");
                MongoCollection<Document> collection = database.getCollection("channel");
                if (collection.find(new Document("guildID", event.getGuild().getId())).iterator().hasNext()) {
                    Document guildLevelChannel =
                            collection.find(new Document("guildID", event.getGuild().getId())).first();

                    String channelID = guildLevelChannel.getString("channel");
                    if (channelID.equals("OFF"))
                        return;

                    channel = event.getJDA().getTextChannelById(channelID);
                }

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(0x000dff);
                embedBuilder.setTitle(this.name + " has advanced to trainer level " + level + "!");
                embedBuilder.setDescription("XP: " + xp + "/" + xpLevelThresh);
                embedBuilder.setImage(imageUrl);

                //noinspection ConstantConditions
                if (channel.getId().equals(event.getChannel().getId()))
                    embedBuilder.setFooter("Don't like level up messages in this channel? " +
                            "Enter " + Prefix.guildPrefix(event.getGuild().getId()) + "setlevel OFF " +
                            "to turn them off, or replace OFF with a channel mention, e.g setlevel #level");

                channel.sendMessage(embedBuilder.build()).queue();
            }
            recreate();
        } catch (Exception ioException) {
            Logger logger =
                    new Logger("Error: " +
                            Logger.exceptionAsString(ioException));
            try {
                logger.logError();
            } catch (IOException excI) {
                excI.printStackTrace();
            }
        }
    }

    public MessageEmbed showLevel() {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(0x000dff);
        builder.setTitle("XP: " + xp + "/" + xpLevelThresh + "\t Trainer Level: " + level);
        builder.setImage(imageUrl);

        return builder.build();
    }

    public void createProfile(){
        MongoDatabase database = Mongo.mongoClient().getDatabase("user");
        MongoCollection<Document> collection = database.getCollection("account");

        if(collection.find(new Document("userID", this.ID)).iterator().hasNext()) return;

        collection.insertOne(this.toDocument());
    }

    public void recreate() throws ProfileNotFoundException {
        MongoDatabase database = Mongo.mongoClient().getDatabase("user");
        MongoCollection<Document> collection = database.getCollection("account");

        if(!collection.find(new Document("userID", this.ID)).iterator().hasNext())
            throw new ProfileNotFoundException();

        collection.replaceOne(new Document("userID", this.ID), this.toDocument());
    }

    public static Profile getProfile(User user) throws ProfileNotFoundException {
        MongoCollection<Document> collection = Mongo.mongoClient()
                .getDatabase("user")
                .getCollection("account");
        if(!collection.find(new Document("userID", user.getId())).iterator().hasNext())
            throw new ProfileNotFoundException();

        Document document = collection.find(new Document("userID", user.getId())).first();
        Profile profile = Profile.toProfile(document);
        profile.name = user.getName();
        profile.imageUrl = user.getAvatarUrl();

        return profile;
    }

    public static boolean profileExists(String userID) {
        return Mongo.mongoClient()
                .getDatabase("user")
                .getCollection("account")
                .find(new Document("userID", userID)).iterator().hasNext();
    }

    public Document toDocument(){
        ArrayList<String> sorinoStr = new ArrayList<>();

        for(Sorino sorino : userSorino)
            sorinoStr.add(sorino.getName());

        return new Document("coins", coins)
                .append("wins", wins)
                .append("loses", loses)
                .append("level", level)
                .append("xpLevelThresh", xpLevelThresh)
                .append("xp", xp)
                .append("guildID", guildID)
                .append("userID", ID)
                .append("name", name)
                .append("imageUrl", imageUrl)
                .append("userSorino", sorinoStr);
    }

    public static Profile toProfile(Document document){
        int coins = document.getInteger("coins"),
            wins = document.getInteger("wins"),
            loses = document.getInteger("loses"),
            level = document.getInteger("level"),
            xp = document.getInteger("xp"),
            xpLevelThresh = document.getInteger("xpLevelThresh");

        String guildID = document.getString("guildID"),
               userID = document.getString("userID"),
               name = document.getString("name"),
               imageUrl = document.getString("imageUrl");

        ArrayList<Sorino> userSorino = new ArrayList<>();
        ArrayList<String> sorinoStr = new ArrayList<>(document.getList("userSorino", String.class));

        try {
            for(String string : sorinoStr) {
                String newStr = string.substring(0, string.indexOf(":"));
                Sorino sorino = Sorino.AllSorino.getSorino(newStr);
                userSorino.add(sorino);
            }
        } catch (SorinoNotFoundException e) {
            e.printStackTrace();
        }
        return new Profile(userSorino, coins, userID, name, wins, loses, imageUrl, guildID, xp, xpLevelThresh, level);
    }

    @Override
    public String toString() {
        return "Sorino: " + userSorino.toString() + "\n" +
                "Coins: " + coins + "\n" +
                "Wins: " + wins + "\n" +
                "Loses: " + loses + "\n" +
                "Trainer Level: " + level + "\n XP -- " + xp + "/" + xpLevelThresh;
    }
}
