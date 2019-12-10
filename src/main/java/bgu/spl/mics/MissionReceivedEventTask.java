package bgu.spl.mics;

import java.util.List;

public class MissionReceivedEventTask implements MissionReceivedEvent {
    private List<String> agentsSerials;
    private String gadget;
    private String missionName;

    public List<String> getAgentsSerials() {
        return agentsSerials;
    }

    public String getGadget() {
        return gadget;
    }

    public String getMissionName() {
        return missionName;
    }
}
