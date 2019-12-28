package bgu.spl.mics;

import java.util.List;

public class AgentAvailableEvent implements Event<FutureResult<Integer,List<String>>> {
    private List<String> eventInformation;
    private Future<FutureResult<Integer,List<String>>> future;
    /**
     * true- send
     * false- release
     */
    private Future<Boolean> send;
    private int duration;

    public AgentAvailableEvent(List<String> serialList,Future<Boolean> future, int duration){
        eventInformation=serialList;
        send=future;
        this.duration=duration;
    }

    public List<String> getEventInformation() {
        return eventInformation;
    }

    public Future<FutureResult<Integer,List<String>>> getFuture(){return future;}

    public Future<Boolean> getSend() {
        return send;
    }

    public int getDuration() {
        return duration;
    }
}
