package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {
    private Future<Integer> tester;

    @BeforeEach
    public void setUp(){
        tester=new Future<>();
    }

    @Test
    public void testResolveNotNullParam(){
        tester.resolve(5);
        tester.resolve(null);
        assertEquals(5,tester.get());
    }

    @Test
    public void testResolveSetResult(){
        Integer param=5;
        tester.resolve(param);
        assertEquals(param,tester.get());
    }

    @Test
    public void testResolveSetDone(){
        Integer param=5;
        tester.resolve(param);
        assertEquals(true,tester.isDone());
    }

    @Test
    public void testLimitedGet(){
        tester.resolve(5);
        Object obj=tester.get(1, TimeUnit.HOURS);
        assertEquals(true,tester.isDone());
    }

    @Test
    public void testLimitedGet_outOfTime(){
        Object result=tester.get(1, TimeUnit.NANOSECONDS);
        assertEquals(false,tester.isDone());
        assertEquals(null,result);
    }

}
