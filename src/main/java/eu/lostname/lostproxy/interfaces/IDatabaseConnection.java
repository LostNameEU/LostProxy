package eu.lostname.lostproxy.interfaces;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoDatabase;

public class IDatabaseConnection {

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;

    public IDatabaseConnection(String host, String port, String username, String password, String database) {
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder().credential(MongoCredential.createCredential(username, database, password.toCharArray())).applicationName("LostProxy").applyConnectionString(new ConnectionString("mongodb://" + host + ":" + port)).build();
        mongoClient = MongoClients.create(mongoClientSettings);
        mongoDatabase = mongoClient.getDatabase(database);
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }
}
