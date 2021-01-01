package eu.lostname.lostproxy.manager;

import com.google.gson.Gson;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.interfaces.linkages.ITeamSpeakLinkage;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class LinkageManager {

    private final Gson gson;

    public LinkageManager(Gson gson) {
        this.gson = gson;
    }

    public ITeamSpeakLinkage createTeamSpeakLinkage(UUID uuid, String playerName, String identity) {
        Document d = LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.TEAMSPEAK_LINKAGES).find(eq("_id", uuid.toString())).first();

        if (d == null) {
            ITeamSpeakLinkage iTeamSpeakLinkage = new ITeamSpeakLinkage(playerName, uuid, System.currentTimeMillis(), identity);

            d = gson.fromJson(gson.toJson(iTeamSpeakLinkage), Document.class);

            LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.TEAMSPEAK_LINKAGES).insertOne(d);
            return gson.fromJson(d.toJson(), ITeamSpeakLinkage.class);
        } else {
            return null;
        }
    }

    public boolean isTeamSpeakIdentityInUse(String identity) {
        return LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.TEAMSPEAK_LINKAGES).find(eq("identity", identity)).first() != null;
    }

    public ITeamSpeakLinkage getTeamSpeakLinkage(UUID uuid) {
        Document d = LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.TEAMSPEAK_LINKAGES).find(eq("_id", uuid.toString())).first();
        return d != null ? gson.fromJson(d.toJson(), ITeamSpeakLinkage.class) : null;
    }

    public ITeamSpeakLinkage getTeamSpeakLinkage(String identity) {
        Document d = LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.TEAMSPEAK_LINKAGES).find(eq("identity", identity)).first();
        return d != null ? gson.fromJson(d.toJson(), ITeamSpeakLinkage.class) : null;
    }

    public void deleteTeamSpeakLinkage(ITeamSpeakLinkage iTeamSpeakLinkage) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection("teamspeakLinkages").deleteOne(eq("_id", iTeamSpeakLinkage.getUuid().toString()));
    }
}
