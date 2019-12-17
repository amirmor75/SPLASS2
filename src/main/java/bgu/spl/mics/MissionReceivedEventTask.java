package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;

public class MissionReceivedEventTask implements MissionReceivedEvent {


    private MissionInfo info;
    private Future<Report> future;


    public final MissionInfo getInfo() {return info;}
    public Future<Report> getFuture(){return future;}
}
