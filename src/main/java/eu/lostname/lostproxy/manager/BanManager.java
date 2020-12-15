package eu.lostname.lostproxy.manager;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.interfaces.bkms.IBan;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

import java.util.UUID;
import java.util.function.Consumer;

public class BanManager {


    /**
     * @param uniqueId the uniqueId of the player who has to be checked
     * @param consumer returns the iban when ban is active
     */
    public void getBan(UUID uniqueId, Consumer<IBan> consumer) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_BANS).find(Filters.eq("_id", uniqueId)).first((document, throwable) -> {
            if (document != null) {
                consumer.accept(LostProxy.getInstance().getGson().fromJson(document.toJson(), IBan.class));
            } else {
                consumer.accept(null);
            }
        });
    }

    /**
     * @param iBan                     that is gonna be inserted into the database
     * @param voidSingleResultCallback returns the callback from the database
     */
    public void insertBan(IBan iBan, SingleResultCallback<Void> voidSingleResultCallback) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_BANS).insertOne(LostProxy.getInstance().getGson().fromJson(LostProxy.getInstance().getGson().toJson(iBan), Document.class), voidSingleResultCallback);
    }

    /**
     * @param iBan                             the ban which has to be deleted
     * @param deleteResultSingleResultCallback returns the callback from the database
     */
    public void deleteBan(IBan iBan, SingleResultCallback<DeleteResult> deleteResultSingleResultCallback) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_BANS).deleteOne(Filters.eq("_id", iBan.getUniqueId()), deleteResultSingleResultCallback);
    }

    /**
     * @param uniqueId                         the uuid of the ban which has to be deleted
     * @param deleteResultSingleResultCallback returns the callback from the database
     */
    public void deleteBan(UUID uniqueId, SingleResultCallback<DeleteResult> deleteResultSingleResultCallback) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_BANS).deleteOne(Filters.eq("_id", uniqueId), deleteResultSingleResultCallback);
    }
}