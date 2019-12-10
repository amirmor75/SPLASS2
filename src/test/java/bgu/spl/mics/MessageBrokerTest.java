package bgu.spl.mics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MessageBrokerTest {
    private MessageBroker messageBroker;
    private EventExtends sEvent;
    private BroadcastExtend sBroadcast;
    private SubscribeImpl sSubscriber;

    @BeforeEach
    public void setUp(){
    messageBroker=new MessageBrokerImpl();
    sEvent=new EventExtendsImpl();
    sBroadcast=new BroadcastExtendImpl();
    sSubscriber=new SubscribeImpl("amir");
    }

    @Test
    public void testSubscribeEvent(){
        messageBroker.subscribeEvent(EventExtends.class,sSubscriber);
        assertTrue(sSubscriber.getMyEventType().equals(EventExtends.class));
    }


    @Test
    public void testSubscribeBroadcast(){
        messageBroker.subscribeBroadcast(BroadcastExtend.class,sSubscriber);
        assertTrue(sSubscriber.getMyBroadcastType().equals(BroadcastExtend.class));

    }

    @Test
    public void testComplete(){
    messageBroker.complete(sEvent,"");
    //assertTrue();
    }

    @Test
    public void testsendBroadcast(){

    }
}
