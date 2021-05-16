package main.tasks;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import main.MainBot;
import main.Task;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DatabaseRecycler implements Task {
    JDA jda;
    int dayOfWeek;
    MongoCollection<Document> userCollection;
    MongoCollection<Document> channelCollection;
    MongoCollection<Document> prefixCollection;

    int documentsDeleted;
    int guildPrefixesDeleted;
    int guildChannelDeleted;

    DatabaseRecycler(JDA jda, MongoCollection<Document> userCollection, MongoCollection<Document> channelCollection,
                     MongoCollection<Document> prefixCollection, int dayOfWeek){
        this.jda = jda;
        this.userCollection = userCollection;
        this.channelCollection = channelCollection;
        this.prefixCollection = prefixCollection;
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public void doTask() {
        DayOfWeek day = LocalDate.now().getDayOfWeek();
        if(day.getValue() == 7){
            // First make all Lists
            List<Guild> guilds = this.jda.getGuilds();
            FindIterable<Document> userDocuments = this.userCollection.find();
            FindIterable<Document> guildPrefixes = this.prefixCollection.find();
            FindIterable<Document> guildChannels = this.channelCollection.find();

            // Second recycle users
            for(Document user : userDocuments){
                boolean isInGuild = false;
                AtomicReference<User> user1 = new AtomicReference<>();
                jda.retrieveUserById(user.getString("userID")).queue(user1::set);
                for(Guild guild : guilds)
                    if(guild.isMember(user1.get())) isInGuild = true;
                if(!isInGuild) {
                    this.userCollection.deleteOne(user);
                    documentsDeleted++;
                }
            }

            // Third recycle channels
            for(Document guildChannel : guildChannels){
                boolean isGuild = false;
                for(Guild guild : guilds)
                    if(guild.getId().equals(guildChannel.getString("guildID"))) isGuild = true;

                if(!isGuild) {
                    this.channelCollection.deleteOne(guildChannel);
                    guildChannelDeleted++;
                }
            }

            // Fourth recycle prefixes
            for(Document guildPrefix : guildPrefixes){
                boolean isGuild = false;
                for(Guild guild : guilds)
                    if(guild.getId().equals(guildPrefix.getString("guildID"))) isGuild = true;

                if(!isGuild) {
                    this.prefixCollection.deleteOne(guildPrefix);
                    guildPrefixesDeleted++;
                }
            }
        }
    }

    @Override
    public void printTaskStatus() {
        MainBot.logger.info("Users deleted: " + documentsDeleted);
        MainBot.logger.info("Channels deleted: " + guildChannelDeleted);
        MainBot.logger.info("Prefixes deleted: " + guildPrefixesDeleted);

        documentsDeleted = 0;
        guildPrefixesDeleted = 0;
        guildChannelDeleted = 0;
    }
}
