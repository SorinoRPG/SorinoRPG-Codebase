package game.value.transfer;

import com.mongodb.client.MongoCollection;
import data.Mongo;
import data.Profile;
import main.MainBot;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bson.Document;

public class SellTask implements Runnable{
    String listingID;

    SellTask(String listingID){
        this.listingID = listingID;
    }

    @Override
    public void run() {
        MongoCollection<Document> collection = Mongo.mongoClient()
                .getDatabase("game")
                .getCollection("market");
        Document document = collection.find(new Document("listingID", this.listingID)).first();
        if(document.containsKey("sorino")){
            Listing sorinoListing = SorinoListing.toListing(document);
            if(sorinoListing.highestBidderID().equals(sorinoListing.sellerID())){

                MainBot.getJda().retrieveUserById(sorinoListing.sellerID()).queue(
                    user -> {
                        if(user.isBot()) return;
                        user.openPrivateChannel().queue(channel -> {
                            EmbedBuilder embed = new EmbedBuilder();
                            embed.setColor(0x00dff);
                            embed.setThumbnail(user.getAvatarUrl());

                            embed.setTitle("Unfortunately, nobody bid on your listing");
                            embed.setDescription(sorinoListing.descOfItem());
                            embed.setFooter("Try to set the price lower, or sell a more wanted Sorino");
                            channel.sendMessage(embed.build()).queue();
                        });
                    });
            }

            MainBot.getJda().retrieveUserById(sorinoListing.sellerID()).queue(user ->{
                try{
                    if(user.isBot()) return;
                    Profile profile = Profile.getProfile(user);
                    sorinoListing.removeItem(profile);
                    profile.setCoins(sorinoListing.price());
                    profile.recreate();

                    user.openPrivateChannel().queue(channel -> {
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setColor(0x00dff);
                        embed.setThumbnail(user.getAvatarUrl());

                        embed.setTitle("Congratulations! You made " + sorinoListing.price() + " coins on your post!");
                        embed.setDescription(sorinoListing.descOfItem());
                        embed.setFooter("Amazing!");
                        channel.sendMessage(embed.build()).queue();
                    });
                } catch(Exception ignored){}
            });

            MainBot.getJda().retrieveUserById(sorinoListing.highestBidderID()).queue(user -> {
                try{
                    Profile profile = Profile.getProfile(user);
                    sorinoListing.awardItem(profile);
                    profile.setCoins(-sorinoListing.price());
                    profile.recreate();

                    user.openPrivateChannel().queue(channel -> {
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setColor(0x00dff);
                        embed.setThumbnail(user.getAvatarUrl());

                        embed.setTitle("Congratulations! You won the biding race!");
                        embed.setDescription(sorinoListing.descOfItem());
                        embed.setFooter("Amazing!");
                        channel.sendMessage(embed.build()).queue();
                    });
                } catch(Exception ignored){}
            });
        }

        collection.deleteOne(document);
    }
}
