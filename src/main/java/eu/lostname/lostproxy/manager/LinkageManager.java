package eu.lostname.lostproxy.manager;

import com.google.gson.Gson;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.interfaces.linkages.ITeamSpeakLinkage;
import org.bson.Document;

import java.util.UUID;
import java.util.function.Consumer;

public class LinkageManager {

    private final Gson gson;

    public LinkageManager(Gson gson) {
        this.gson = gson;
    }

    public void createTeamSpeakLinkage(UUID uuid, String playerName, String identity, Consumer<ITeamSpeakLinkage> consumer) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection("teamspeakLinkages").find(Filters.eq("_id", uuid.toString())).first((document, throwable) -> {
            if (document == null) {
                ITeamSpeakLinkage iTeamSpeakLinkage = new ITeamSpeakLinkage(playerName, uuid, System.currentTimeMillis(), identity);

                document = gson.fromJson(gson.toJson(iTeamSpeakLinkage), Document.class);

                LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection("teamspeakLinkages").insertOne(document, (unused, throwable1) -> {
                    consumer.accept(iTeamSpeakLinkage);
                });
            } else {
                consumer.accept(null);
            }
        });
    }

    public void isTeamSpeakIdentityInUse(String identity, Consumer<Boolean> consumer) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection("teamspeakLinkages").find(Filters.eq("identity", identity)).first((document, throwable) -> {
            consumer.accept(document != null);
        });
    }

    public void getTeamSpeakLinkage(UUID uuid, Consumer<ITeamSpeakLinkage> consumer) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection("teamspeakLinkages").find(Filters.eq("_id", uuid.toString())).first(((document, throwable) -> consumer.accept(document != null ? gson.fromJson(document.toJson(), ITeamSpeakLinkage.class) : null)));
    }

    public void getTeamSpeakLinkage(String identity, Consumer<ITeamSpeakLinkage> consumer) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection("teamspeakLinkages").find(Filters.eq("identity", identity)).first(((document, throwable) -> consumer.accept(document != null ? gson.fromJson(document.toJson(), ITeamSpeakLinkage.class) : null)));
    }

    public void deleteTeamSpeakLinkage(ITeamSpeakLinkage iTeamSpeakLinkage, SingleResultCallback<DeleteResult> callback) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection("teamspeakLinkages").deleteOne(Filters.eq("_id", iTeamSpeakLinkage.getUuid().toString()), callback);
    }
}
