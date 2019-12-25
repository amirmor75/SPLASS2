package bgu.spl.mics;


public class GadgetAvailableEvent implements Event<FutureResult<Integer,Integer>> {

    private String eventInformation;
    private Future<FutureResult<Integer,Integer>> futureAvailavle;


    public GadgetAvailableEvent(String gadget){
        eventInformation=gadget;
    }

    public String getEventInformation() {
        return eventInformation;
    }

    public Future<FutureResult<Integer,Integer>> getFuture(){return futureAvailavle;}
}
