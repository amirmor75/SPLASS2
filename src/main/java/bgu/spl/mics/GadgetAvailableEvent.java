package bgu.spl.mics;

public class GadgetAvailableEvent implements Event<String> {


    private String eventInformation;
    private Future<String> future;

    public GadgetAvailableEvent(String gadget){
        eventInformation=gadget;
    }

    public String getEventInformation() {
        return eventInformation;
    }

    public Future<String> getFuture(){return future;}
}
