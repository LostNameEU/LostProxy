package eu.lostname.lostproxy.manager;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.client.model.Filters;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.interfaces.bkms.IBan;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

import java.util.function.Consumer;

public class BanManager {

    public void getBan(String uniqueId, Consumer<IBan> consumer) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_BANS).find(Filters.eq("_id", uniqueId)).first((document, throwable) -> {
            if (document != null) {
                consumer.accept(LostProxy.getInstance().getGson().fromJson(document.toJson(), IBan.class));
            } else {
                consumer.accept(null);
            }
        });
    }

    public void insertBan(IBan iBan, SingleResultCallback<Void> voidSingleResultCallback) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_BANS).insertOne(LostProxy.getInstance().getGson().fromJson(LostProxy.getInstance().getGson().toJson(iBan), Document.class), voidSingleResultCallback);
    }
}