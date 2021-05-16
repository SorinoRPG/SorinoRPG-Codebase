package data;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class ProfileUpdater {
    public static void main(String[] args) throws ProfileNotFoundException {
        Mongo.initMongo();

        MongoCollection<Document> docs =
                Mongo.mongoClient().getDatabase("user").getCollection("account");
        int profiles = 0;
        for(Document document : docs.find()){
            Profile profile = Profile.toProfile(document);
            profile.recreate();
            profiles++;
        }

        System.out.println(profiles);
    }
}
