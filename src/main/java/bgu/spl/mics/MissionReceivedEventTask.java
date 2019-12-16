package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.MissionInfo;

public class MissionReceivedEventTask implements MissionReceivedEvent {
    private Future<MissionInfo> info;

    public void initEvent(){
        info=new Future<>();
    }

    public Future<MissionInfo> getFuture(){
        return info;
    }
}
