package bgu.spl.mics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        messageBroker.register(sSubscriber);
        messageBroker.complete(sEvent,sSubscriber.getResult());
        assertEquals("result",sSubscriber.getFutureResult());
    }

    @Test
    public void testSendBroadcast(){
        messageBroker.register(sSubscriber);
        messageBroker.sendBroadcast(sBroadcast);
        assertEquals(sSubscriber.getBroadcast(),sBroadcast);
    }

    @Test
    public void testSendEvent(){
        messageBroker.register(sSubscriber);
        messageBroker.sendEvent(sEvent);
        assertEquals(sSubscriber.getEvent(),sEvent);
    }

    @Test
    public void testRegister(){ //makes sure register makes queue
        messageBroker.register(sSubscriber);
        messageBroker.sendEvent(sEvent);
        assertEquals(sSubscriber.getEvent(),sEvent);
    }

    @Test
    public void testUnRegister(){
        messageBroker.register(sSubscriber);
        messageBroker.unregister(sSubscriber);
        messageBroker.sendEvent(sEvent);
        assertEquals(sSubscriber.getEvent(),null);
    }

    @Test
    public void testAwaitMessage(){
        messageBroker.register(sSubscriber);
        messageBroker.sendEvent(sEvent);
        try {
            messageBroker.awaitMessage(sSubscriber);
        }catch(Exception e){ }
        assertEquals(sSubscriber.getEvent(),sEvent);
    }


}
