package eu.lostname.lostproxy.manager;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.interfaces.bkms.IBan;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

import java.sql.Time;
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

    public void saveBan(IBan iBan, SingleResultCallback<UpdateResult> updateResultSingleResultCallback) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_BANS).replaceOne(Filters.eq("_id", iBan.getUniqueId()), LostProxy.getInstance().getGson().fromJson(LostProxy.getInstance().getGson().toJson(iBan), Document.class), new ReplaceOptions().upsert(true), updateResultSingleResultCallback);
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
     * @param end the end time
     * @return a string with the display
     */

    @SuppressWarnings("deprecation")
    public String calculateRemainingTime(long end) {
        Time time = new Time(end - System.currentTimeMillis());
        String estimatedTime = "";

        if (time.getDay() == 1) {
            estimatedTime = "ein §7Tag§8, ";
        } else if (time.getDay() > 1) {
            estimatedTime = time.getDay() + " §7Tage§8, ";
        }

        if (time.getHours() == 1) {
            estimatedTime = "§ceine §7Stunde§8, ";
        } else if (time.getHours() > 1) {
            estimatedTime = "§c" + time.getHours() + " §7Stunden§8, ";
        }

        if (time.getMinutes() == 1) {
            estimatedTime = "§ceine §7Minute und ";
        } else if (time.getMinutes() > 1) {
            estimatedTime = "§c" + time.getMinutes() + " §7Minuten und ";
        }

        if (time.getSeconds() == 1) {
            estimatedTime = "§ceine §7Sekunde";
        } else if (time.getSeconds() > 1) {
            estimatedTime = "§c" + time.getSeconds() + " §7Sekunden";
        }

        return estimatedTime;
    }
}