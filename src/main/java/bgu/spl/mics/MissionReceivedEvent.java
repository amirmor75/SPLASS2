package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.MissionInfo;

public class MissionReceivedEventTask implements MissionReceivedEvent {


    private MissionInfo eventInformation;
    private Future<MissionInfo> future;




    @Override
    public MissionInfo getEventInformation() {
        return eventInformation;
    }

    public Future<MissionInfo> getFuture(){return future;}
}
