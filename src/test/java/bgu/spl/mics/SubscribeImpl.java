package bgu.spl.mics;

public class SubscribeImpl extends Subscriber{


    private Class<? extends Event> typeEvent;
    private Class<? extends Broadcast> typeBroadcast;
    private boolean awake=false;
    private Callback<String> callBack;
    private EventExtendsImpl event;
    private BroadcastExtendImpl broadcast;

    /**
     * @param name the Subscriber name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public SubscribeImpl(String name) {
        super(name);
    }
    public Class<? extends Event> getMyEventType(){
        return typeEvent;
    }
    public Class<? extends Broadcast> getMyBroadcastType(){
        return typeBroadcast;
    }
    public Event getEvent(){ return event; }
    public BroadcastExtend getBroadcast(){ return broadcast; }
    public String getResult(){
        String result="result";
        event.setFutureResult(result);
        return result;
    }
    public String getFutureResult(){ return event.getFutureResult(); }




    @Override
    protected void initialize() {
        //nonsense
        synchronized (this){
            callBack = (String s) -> {
                while (!awake) {
                    try {
                        MessageBrokerImpl.getInstance().wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
    }







}
