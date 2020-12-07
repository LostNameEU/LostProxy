package eu.lostname.lostproxy.interfaces.linkages;

import eu.lostname.lostproxy.interfaces.ILinkage;

import java.util.UUID;

public class ITeamSpeakLinkage extends ILinkage {

    private String identity;

    public ITeamSpeakLinkage(String playerName, UUID _id, long creationTimestamp, String identity) {
        super(playerName, _id, creationTimestamp);
        this.identity = identity;
    }


    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
