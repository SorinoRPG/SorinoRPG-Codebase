package data;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class Mongo {
    private static MongoClient mongoClient;

    public static void initMongo(){

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        rootLogger.setLevel(Level.OFF);

        MongoClientURI uri = new MongoClientURI("mongodb+srv://SorinoRPG:wYPwojBYeIjj6YDX" +
                "@sorinorpgcluster.huchg.mongodb.net/user?retryWrites=true&w=majority");
        mongoClient = new MongoClient(uri);

    }

    public static MongoClient mongoClient(){
        return mongoClient;
    }
}
