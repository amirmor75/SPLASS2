package bgu.spl.mics;


public class AgentsAvailableEventTask implements AgentsAvailableEvent {
    /**
     * states whether or not a certain 00 agent (or a couple of them)
     *  is available to execute a mission
     */
    private Future<Boolean> isAvailable;

    public void initEvent(){
        isAvailable=new Future<>();
    }

    public Future<Boolean> getFuture() {
        return isAvailable;
    }

}
