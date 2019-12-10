package bgu.spl.mics;

public class GadgetAvailableEventTask implements GadgetAvailableEvent {
    private String gadgetName;
    private Future<String> myFuture;

    public Future<String> getMyFuture() {
        return myFuture;
    }

    public String getGadgetName(){
        return gadgetName;
    }
}
