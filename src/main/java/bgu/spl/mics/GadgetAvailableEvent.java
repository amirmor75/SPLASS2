package bgu.spl.mics;

public class GadgetAvailableEvent implements Event<Boolean> {

    private String eventInformation;
    private Future<Boolean> future;
    private int timeQreceived;

    public GadgetAvailableEvent(String gadget){
        eventInformation=gadget;
    }

    public String getEventInformation() {
        return eventInformation;
    }

    public Future<Boolean> getFuture(){return future;}

    public int getTimeQreceived() { return timeQreceived;  }

    public void setTimeQreceived(int timeQreceived) {  this.timeQreceived = timeQreceived;   }
}
