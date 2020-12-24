package eu.lostname.lostproxy.manager;

import com.google.gson.Gson;
import com.mongodb.client.model.Filters;
import eu.lostname.lostproxy.databases.LostProxyDatabase;
import eu.lostname.lostproxy.interfaces.IFriendData;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

import java.util.HashMap;
import java.util.UUID;

public class FriendManager {

    private final LostProxyDatabase database;
    private final Gson gson;

    public FriendManager(LostProxyDatabase database, Gson gson) {
        this.database = database;
        this.gson = gson;
    }

    public IFriendData getFriendData(UUID uniqueId) {
        Document d = database.getMongoDatabase().getCollection(MongoCollection.FRIEND_DATA).find(Filters.eq("_id", uniqueId.toString())).first();

        if (d == null) {
            d = gson.fromJson(gson.toJson(new IFriendData(uniqueId.toString(), System.currentTimeMillis(), new HashMap<>(), new HashMap<>(), true, true, true, true)), Document.class);

            database.getMongoDatabase().getCollection(MongoCollection.FRIEND_DATA).insertOne(d);
        }

        return gson.fromJson(d.toJson(), IFriendData.class);
    }
}
