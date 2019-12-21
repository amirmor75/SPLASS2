package bgu.spl.mics;

import java.util.List;

public class AgentAvailableEvent implements Event<Boolean> {
    private List<String> eventInformation;
    private Future<Boolean> future;

    public AgentAvailableEvent(List<String> serialList){
        eventInformation=serialList;
    }

    public List<String> getEventInformation() {
        return eventInformation;
    }

    public Future<Boolean> getFuture(){return future;}
}
