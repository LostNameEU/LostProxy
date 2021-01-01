package eu.lostname.lostproxy.listener.teamspeak;

import com.github.theholywaffle.teamspeak3.api.event.*;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.utils.TSServerGroups;

import java.util.ArrayList;

public class TeamSpeakListeners implements TS3Listener {

    @Override
    public void onTextMessage(TextMessageEvent textMessageEvent) {

    }


    @Override
    public void onClientJoin(ClientJoinEvent event) {
        LostProxy.getInstance().getTeamSpeakManager().getClient(event.getClientId(), clientInfo -> {
            if (!clientInfo.isServerQueryClient()) {
                ArrayList<Integer> serverGroups = LostProxy.getInstance().getTeamSpeakManager().getServerGroupsAsList(clientInfo);
                if (!serverGroups.contains(TSServerGroups.TEAM_BOT)) {
                    LostProxy.getInstance().getLinkageManager().getTeamSpeakLinkage(clientInfo.getUniqueIdentifier(), iTeamSpeakLinkage -> {
                        if (iTeamSpeakLinkage != null) {
                            LostProxy.getInstance().getPlayerManager().getIPlayerAsync(iTeamSpeakLinkage.getUuid(), iPlayer -> {
                                if (!clientInfo.isInServerGroup(iPlayer.getiPermissionGroup().getProperties().getInt("tsGroupId"))) {
                                    LostProxy.getInstance().getTeamSpeakManager().resetAllServerGroups(clientInfo);
                                    LostProxy.getInstance().getTeamSpeakManager().setServerGroupsUsingInGamePermissions(clientInfo, iPlayer);
                                    LostProxy.getInstance().getTeamSpeakManager().setHead(clientInfo, iPlayer.getPlayerName());
                                }
                            });
                        } else {
                            LostProxy.getInstance().getTeamSpeakManager().resetAllServerGroups(clientInfo);
                        }
                    });
                }
            }
        });
    }


    @Override
    public void onClientLeave(ClientLeaveEvent clientLeaveEvent) {

    }


    @Override
    public void onServerEdit(ServerEditedEvent serverEditedEvent) {

    }


    @Override
    public void onChannelEdit(ChannelEditedEvent channelEditedEvent) {

    }


    @Override
    public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent channelDescriptionEditedEvent) {

    }


    @Override
    public void onClientMoved(ClientMovedEvent clientMovedEvent) {

    }


    @Override
    public void onChannelCreate(ChannelCreateEvent channelCreateEvent) {

    }


    @Override
    public void onChannelDeleted(ChannelDeletedEvent channelDeletedEvent) {

    }


    @Override
    public void onChannelMoved(ChannelMovedEvent channelMovedEvent) {

    }


    @Override
    public void onChannelPasswordChanged(ChannelPasswordChangedEvent channelPasswordChangedEvent) {

    }


    @Override
    public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent privilegeKeyUsedEvent) {

    }
}
