package eu.lostname.lostproxy.interfaces;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class IDatabaseConnection {

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;

    public IDatabaseConnection(String host, String port, String username, String password, String database) {
        mongoClient = MongoClients.create("mongodb://" + username + ":" + password + "@" + host + ":" + port + "/?authSource=" + database);
        mongoDatabase = mongoClient.getDatabase(database);
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }
}
