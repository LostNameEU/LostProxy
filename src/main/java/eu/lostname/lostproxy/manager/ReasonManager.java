package eu.lostname.lostproxy.manager;

import com.google.gson.Gson;
import com.mongodb.async.SingleResultCallback;
import eu.lostname.lostproxy.databases.LostProxyDatabase;
import eu.lostname.lostproxy.interfaces.bkms.IBanReason;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;

public class ReasonManager {

    private final Gson gson;
    private final LostProxyDatabase database;

    private final ArrayList<IBanReason> registedBanReasons;

    public ReasonManager(Gson gson, LostProxyDatabase database) {
        this.gson = gson;
        this.database = database;
        this.registedBanReasons = new ArrayList<>();
    }

    public void loadBanReasons() {
        registedBanReasons.clear();

        database.getMongoDatabase().getCollection(MongoCollection.BAN_REASONS).find().forEach(document -> this.registedBanReasons.add(gson.fromJson(document.toJson(), IBanReason.class)), (unused, throwable) -> {
            throwable.printStackTrace();
        });
    }

    public void saveBanReason(IBanReason iBanReason, SingleResultCallback<Void> voidSingleResultCallback) {
        database.getMongoDatabase().getCollection(MongoCollection.BAN_REASONS).insertOne(gson.fromJson(gson.toJson(iBanReason), Document.class), voidSingleResultCallback);
    }

    public ArrayList<IBanReason> getRegistedBanReasons() {
        return registedBanReasons;
    }
}
