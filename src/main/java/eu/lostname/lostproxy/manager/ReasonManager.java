package eu.lostname.lostproxy.manager;

import com.google.gson.Gson;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import eu.lostname.lostproxy.databases.LostProxyDatabase;
import eu.lostname.lostproxy.interfaces.bkms.IBanReason;
import eu.lostname.lostproxy.interfaces.bkms.IMuteReason;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;

public class ReasonManager {

    private final Gson gson;
    private final LostProxyDatabase database;

    private final ArrayList<IBanReason> registedBanReasons;
    private final ArrayList<IBanReason> registedMuteReasons;

    private final ArrayList<String> banReasonCommandProcess;
    private final ArrayList<String> muteReasonCommandProcess;

    public ReasonManager(Gson gson, LostProxyDatabase database) {
        this.gson = gson;
        this.database = database;
        this.registedBanReasons = new ArrayList<>();
        this.registedMuteReasons = new ArrayList<>();
        this.banReasonCommandProcess = new ArrayList<>();
        this.muteReasonCommandProcess = new ArrayList<>();

        loadBanReasons();
        loadMuteReasons();
    }

    public void loadBanReasons() {
        database.getMongoDatabase().getCollection(MongoCollection.BAN_REASONS).find().sort(Sorts.ascending("_id")).forEach(document -> this.registedBanReasons.add(gson.fromJson(document.toJson(), IBanReason.class)), (unused, throwable) -> throwable.printStackTrace());
    }

    public void loadBanReasons() {
        database.getMongoDatabase().getCollection(MongoCollection.MUTE_REASONS).find().sort(Sorts.ascending("_id")).forEach(document -> this.registedMuteReasons.add(gson.fromJson(document.toJson(), IMuteReason.class)), (unused, throwable) -> throwable.printStackTrace());
    }

    public void reloadBanReasons() {
        registedBanReasons.clear();
        database.getMongoDatabase().getCollection(MongoCollection.BAN_REASONS).find().sort(Sorts.ascending("_id")).forEach(document -> this.registedBanReasons.add(gson.fromJson(document.toJson(), IBanReason.class)), (unused, throwable) -> throwable.printStackTrace());
    }

    public void reloadMuteReasons() {
        registedMuteReasons.clear();
        database.getMongoDatabase().getCollection(MongoCollection.MUTE_REASONS).find().sort(Sorts.ascending("_id")).forEach(document -> this.registedMuteReasons.add(gson.fromJson(document.toJson(), IMuteReason.class)), (unused, throwable) -> throwable.printStackTrace());
    }

    public void saveBanReason(IBanReason iBanReason, SingleResultCallback<UpdateResult> voidSingleResultCallback) {
        database.getMongoDatabase().getCollection(MongoCollection.BAN_REASONS).replaceOne(Filters.eq("_id", iBanReason.getId()), gson.fromJson(gson.toJson(iBanReason), Document.class), voidSingleResultCallback);
    }

    public void saveMuteReason(IMuteReason iMuteReason, SingleResultCallback<UpdateResult> voidSingleResultCallback) {
        database.getMongoDatabase().getCollection(MongoCollection.MUTE_REASONS).replaceOne(Filters.eq("_id", iMuteReason.getId()), gson.fromJson(gson.toJson(iMuteReason), Document.class), voidSingleResultCallback);
    }

    public void deleteBanReason(IBanReason iBanReason, SingleResultCallback<DeleteResult> deleteResultSingleResultCallback) {
        database.getMongoDatabase().getCollection(MongoCollection.BAN_REASONS).deleteOne(Filters.eq("_id", iBanReason.getId()), deleteResultSingleResultCallback);
    }

    public void deleteMuteReason(IMuteReason iMuteReason, SingleResultCallback<DeleteResult> deleteResultSingleResultCallback) {
        database.getMongoDatabase().getCollection(MongoCollection.MUTE_REASONS).deleteOne(Filters.eq("_id", iMuteReason.getId()), deleteResultSingleResultCallback);
    }

    public IBanReason getBanReasonByID(int id) {
        return registedBanReasons.stream().filter(one -> one.getId() == id).findFirst().orElse(null);
    }

    public IMuteReason getMuteReasonByID(int id) {
        return registedMuteReasons.stream().filter(one -> one.getId() == id).findFirst().orElse(null);
    }

    public ArrayList<IBanReason> getRegistedBanReasons() {
        return registedBanReasons;
    }

    public ArrayList<IBanReason> getRegistedMuteReasons() {
        return registedMuteReasons;
    }

    public ArrayList<String> getBanReasonCommandProcess() {
        return banReasonCommandProcess;
    }

    public ArrayList<String> getMuteReasonCommandProcess() {
        return muteReasonCommandProcess;
    }
}
