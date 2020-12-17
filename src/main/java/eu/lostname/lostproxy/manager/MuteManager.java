package eu.lostname.lostproxy.manager;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

import java.sql.Time;
import java.util.UUID;
import java.util.function.Consumer;

public class MuteManager {


    /**
     * Returns a mute when the given player is muted
     *
     * @param uniqueId the uniqueId of the player who has to be checked
     * @param consumer returns the imute when ban is active
     */
    public void getMute(UUID uniqueId, Consumer<IMute> consumer) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_MUTES).find(Filters.eq("_id", uniqueId)).first((document, throwable) -> {
            if (document != null) {
                consumer.accept(LostProxy.getInstance().getGson().fromJson(document.toJson(), IMute.class));
            } else {
                consumer.accept(null);
            }
        });
    }

    /**
     * Saves the given mute in the database
     *
     * @param iMute                            the mute which has to be saved
     * @param updateResultSingleResultCallback returns a UpdateResult of the insert
     */
    public void saveMute(IMute iMute, SingleResultCallback<UpdateResult> updateResultSingleResultCallback) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_MUTES).replaceOne(Filters.eq("_id", iMute.getUniqueId()), LostProxy.getInstance().getGson().fromJson(LostProxy.getInstance().getGson().toJson(iMute), Document.class), new ReplaceOptions().upsert(true), updateResultSingleResultCallback);
    }

    /**
     * Inserts the given mute in the database
     *
     * @param iMute                     that is gonna be inserted into the database
     * @param voidSingleResultCallback returns the callback from the database
     */
    public void insertMute(IMute iMute, SingleResultCallback<Void> voidSingleResultCallback) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_MUTES).insertOne(LostProxy.getInstance().getGson().fromJson(LostProxy.getInstance().getGson().toJson(iMute), Document.class), voidSingleResultCallback);
    }

    /**
     * Deletes the given mute in the database
     *
     * @param iMute                             the mute which has to be deleted
     * @param deleteResultSingleResultCallback returns the callback from the database
     */
    public void deleteMute(IMute iMute, SingleResultCallback<DeleteResult> deleteResultSingleResultCallback) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_MUTES).deleteOne(Filters.eq("_id", iMute.getUniqueId()), deleteResultSingleResultCallback);
    }

    /**
     * Returns a string which displays the remaining time to a given end
     *
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