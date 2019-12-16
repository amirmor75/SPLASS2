package bgu.spl.mics;

public class GadgetAvailableEventTask implements GadgetAvailableEvent {
    private Future<String> gadget;

    public void initEvent(){
        gadget=new Future<>();
    }

    public Future<String> getFuture() {
        return gadget;
    }
}
