package main.tasks;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import data.Profile;
import main.Task;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.bson.Document;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

        }
    }

    @Override
    public void printTaskStatus() {

    }
}
