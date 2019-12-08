package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {
    private Future<Object> tester;

    @BeforeEach
    public void setUp(){
        tester=new Future<>();
    }

    @Test
    public void testBlockingGet(){
        Object obj=tester.get();
        assertEquals(true,tester.isDone());
    }

    @Test
    public void testResolveNotNullParam(){
        Object obj=tester.get();
        tester.resolve(null);
        assertEquals(obj,tester.get());
    }

    @Test
    public void testResolveNotResolvedYet(){
        //TODO: implement this ?
    }

    @Test
    public void testResolveSetResult(){
        Object param=new Object();
        tester.resolve(param);
        assertEquals(param,tester.get());
    }

    @Test
    public void testResolveSetDone(){
        Object param=new Object();
        tester.resolve(param);
        assertEquals(true,tester.isDone());
    }


}
