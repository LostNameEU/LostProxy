package eu.lostname.lostproxy.manager;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanHistory;
import eu.lostname.lostproxy.interfaces.historyandentries.kick.IKickEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.kick.IKickHistory;
import eu.lostname.lostproxy.interfaces.historyandentries.mute.IMuteHistory;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Consumer;

public class HistoryManager {

    private final ArrayList<String> kickHistoryClearCommandProcess;
    private final ArrayList<String> banHistoryClearCommandProcess;
    private final ArrayList<String> muteHistoryClearCommandProcess;

    public HistoryManager() {
        this.kickHistoryClearCommandProcess = new ArrayList<>();
        this.banHistoryClearCommandProcess = new ArrayList<>();
        this.muteHistoryClearCommandProcess = new ArrayList<>();
    }

    public void getKickHistory(UUID uniqueId, Consumer<IKickHistory> consumer) {
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

    public void saveKickHistory(IKickHistory iKickHistory, Consumer<Boolean> consumer) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.KICK_HISTORIES).replaceOne(Filters.eq("_id", iKickHistory.getUniqueId()),
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

    public void getBanHistory(UUID uniqueId, Consumer<IBanHistory> consumer) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.BAN_HISTORIES).find(Filters.eq("_id", uniqueId)).first((document, throwable) -> {
            if (document == null) {
                IBanHistory iBanHistory = new IBanHistory(uniqueId, new ArrayList<>());
                document = LostProxy.getInstance().getGson().fromJson(LostProxy.getInstance().getGson().toJson(iBanHistory), Document.class);

                LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.BAN_HISTORIES).insertOne(document, (unused, throwable1) -> {
                    throwable1.printStackTrace(System.out);
                });
            }

            consumer.accept(LostProxy.getInstance().getGson().fromJson(document.toJson(), IBanHistory.class));
        });
    }

    public void saveBanHistory(IBanHistory iBanHistory, Consumer<Boolean> consumer) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.BAN_HISTORIES).replaceOne(Filters.eq("_id", iBanHistory.getUniqueId()),
                LostProxy.getInstance().getGson().fromJson(LostProxy.getInstance().getGson().toJson(iBanHistory), Document.class),
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

    public void getMuteHistory(UUID uniqueId, Consumer<IMuteHistory> consumer) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.MUTE_HISTORIES).find(Filters.eq("_id", uniqueId)).first((document, throwable) -> {
            if (document == null) {
                IMuteHistory iMuteHistory = new IMuteHistory(uniqueId, new ArrayList<>());
                document = LostProxy.getInstance().getGson().fromJson(LostProxy.getInstance().getGson().toJson(iMuteHistory), Document.class);

                LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.MUTE_HISTORIES).insertOne(document, (unused, throwable1) -> {
                    throwable1.printStackTrace(System.out);
                });
            }

            consumer.accept(LostProxy.getInstance().getGson().fromJson(document.toJson(), IMuteHistory.class));
        });
    }

    public void saveMuteHistory(IMuteHistory iMuteHistory, Consumer<Boolean> consumer) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.MUTE_HISTORIES).replaceOne(Filters.eq("_id", iMuteHistory.getUniqueId()),
                LostProxy.getInstance().getGson().fromJson(LostProxy.getInstance().getGson().toJson(iMuteHistory), Document.class),
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

    public ArrayList<String> getKickHistoryClearCommandProcess() {
        return kickHistoryClearCommandProcess;
    }

    public ArrayList<String> getBanHistoryClearCommandProcess() {
        return banHistoryClearCommandProcess;
    }

    public ArrayList<String> getMuteHistoryClearCommandProcess() {
        return muteHistoryClearCommandProcess;
    }
}
