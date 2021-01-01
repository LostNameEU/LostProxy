package eu.lostname.lostproxy.manager;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.interfaces.historyandentries.kick.IKickEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.kick.IKickHistory;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.function.Consumer;

public class HistoryManager {

    public void getKickHistory(String uniqueId, Consumer<IKickHistory> consumer) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.KICK_HISTORIES).find(Filters.eq("_id", uniqueId)).first((document, throwable) -> {
            if (document == null) {
                IKickHistory iKickHistory = new IKickHistory(uniqueId, new ArrayList<IKickEntry>());
                document = LostProxy.getInstance().getGson().fromJson(LostProxy.getInstance().getGson().toJson(iKickHistory), Document.class);

                LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.KICK_HISTORIES).insertOne(document, (unused, throwable1) -> {
                    throwable1.printStackTrace(System.out);
                });
            }

            consumer.accept(LostProxy.getInstance().getGson().fromJson(document.toJson(), IKickHistory.class));
        });
    }

    public void saveKickHistory(String uniqueId, IKickHistory iKickHistory, Consumer<Boolean> consumer) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.KICK_HISTORIES).replaceOne(Filters.eq("_id", uniqueId),
                LostProxy.getInstance().getGson().fromJson(LostProxy.getInstance().getGson().toJson(iKickHistory), Document.class),
                new ReplaceOptions().upsert(true),
                (updateResult, throwable) -> {
                    if (updateResult.wasAcknowledged()) {
                        consumer.accept(true);
                    } else {
                        consumer.accept(false);
                        throwable.printStackTrace(System.out);
                    }
                });
    }
}
