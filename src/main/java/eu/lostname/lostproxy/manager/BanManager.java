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

import java.util.UUID;
import java.util.function.Consumer;

public class BanManager {


    /**
     * Returns a ban when the given player is banned
     *
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
     * Saves the given ban in the database
     *
     * @param iBan                             the ban which has to be saved
     * @param updateResultSingleResultCallback returns a UpdateResult of the insert
     */
    public void saveBan(IBan iBan, SingleResultCallback<UpdateResult> updateResultSingleResultCallback) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_BANS).replaceOne(Filters.eq("_id", iBan.getUniqueId()), LostProxy.getInstance().getGson().fromJson(LostProxy.getInstance().getGson().toJson(iBan), Document.class), new ReplaceOptions().upsert(true), updateResultSingleResultCallback);
    }

    /**
     * Inserts the given ban in the database
     *
     * @param iBan                     that is gonna be inserted into the database
     * @param voidSingleResultCallback returns the callback from the database
     */
    public void insertBan(IBan iBan, SingleResultCallback<Void> voidSingleResultCallback) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_BANS).insertOne(LostProxy.getInstance().getGson().fromJson(LostProxy.getInstance().getGson().toJson(iBan), Document.class), voidSingleResultCallback);
    }

    /**
     * Deletes the given ban in the database
     * @param iBan                             the ban which has to be deleted
     * @param deleteResultSingleResultCallback returns the callback from the database
     */
    public void deleteBan(IBan iBan, SingleResultCallback<DeleteResult> deleteResultSingleResultCallback) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_BANS).deleteOne(Filters.eq("_id", iBan.getUniqueId()), deleteResultSingleResultCallback);
    }

    /**
     * Returns a string which displays the remaining time to a given end
     * @param end the end time
     * @return a string with the display
     */

    @SuppressWarnings("deprecation")
    public String calculateRemainingTime(long end) {
        long millis = end - System.currentTimeMillis();
        int seconds = 0, minutes = 0, hours = 0, days = 0;

        while (millis >= 1000) {
            millis -= 1000;
            seconds++;
        }

        while (seconds >= 60) {
            seconds -= 60;
            minutes++;
        }

        while (minutes >= 60) {
            minutes -= 60;
            hours++;
        }

        while (hours >= 24) {
            hours -= 24;
            days++;
        }

        String estimatedTime = "";

        if (days == 1) {
            estimatedTime = "ein §7Tag§8, ";
        } else if (days > 1) {
            estimatedTime = days + " §7Tage§8, ";
        }

        if (hours == 1) {
            estimatedTime = "§ceine §7Stunde§8, ";
        } else if (hours > 1) {
            estimatedTime = "§c" + hours + " §7Stunden§8, ";
        }

        if (minutes == 1) {
            estimatedTime = "§ceine §7Minute und ";
        } else if (minutes > 1) {
            estimatedTime = "§c" + minutes + " §7Minuten und ";
        }

        if (seconds == 1) {
            estimatedTime = "§ceine §7Sekunde";
        } else if (seconds > 1) {
            estimatedTime = "§c" + seconds + " §7Sekunden";
        }

        return estimatedTime;
    }
}