package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.MissionInfo;

public class MissionReceivedEvent implements Event<MissionInfo> {


    private MissionInfo eventInformation;
    private Future<MissionInfo> future;

    public MissionReceivedEvent(MissionInfo info){
        eventInformation=info;
    }

    public MissionInfo getEventInformation() {
        return eventInformation;
    }

    public Future<MissionInfo> getFuture(){return future;}
}
