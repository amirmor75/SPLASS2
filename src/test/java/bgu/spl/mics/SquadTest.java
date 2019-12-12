package bgu.spl.mics;


import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SquadTest {
    private Squad squad;
    private Agent[] agents;
    @BeforeEach
    public void setUp(){
        squad=new Squad();
        agents=new Agent[2];
        agents[0]=new Agent();
        agents[0].setName("Amir Mor");
        agents[0].setSerialNumber("001");
        agents[0]=new Agent();
        agents[0].setName("Dani Cohen");
        agents[0].setSerialNumber("002");
        squad.load(agents);
    }//initializing squad instance with two Agents

    /**
     * PRE- none
     * Post- Agents are not available or not taken
     */
    @Test
    public void testGetAgents(){
        List<String> a=new LinkedList<String>();
        a.add("001");
        a.add("002");
        assertTrue(squad.getAgents(a));// returns true because they exist
        //next line assuming the array changes with squad
        assertTrue(!agents[0].isAvailable() && !agents[1].isAvailable());//agents should be aquired
    }

    /**
     * PRE- none
     * POST- agents available
     */
    @Test
    public void testReleaseAgents(){
        agents[0].acquire();
        agents[1].acquire();
        List<String> a=new LinkedList<String>();
        a.add("001");
        a.add("002");
        squad.releaseAgents(a);
        //next line assuming the array changes with squad
        assertTrue(agents[0].isAvailable() && agents[1].isAvailable());
    }

    /**
     * PRE- serials of agents exist
     * POST- none
     */
    @Test
    public void testGetAgentsNames(){
        List<String> a=new LinkedList<String>();
        a.add("001");
        a.add("002");
        List<String> b= squad.getAgentsNames(a);
        assertTrue(b.contains("Amir Mor") && b.contains("Dani cohen"));
    }

    /**
     * PRE-
     * POST-agents available
     */
    @Test
    public void testSendAgents(){
        List<String> a=new LinkedList<String>();
        a.add("001");
        a.add("002");
        int timeTick=100000000;
        long startTime = System.nanoTime();
        squad.sendAgents(a,timeTick);
        long endTime = System.nanoTime();
        long nanoDuration = (endTime - startTime);
        long miliDuration=nanoDuration/1000000;
        assertTrue(miliDuration>=timeTick);
    }

    /**
     * PRE-
     * POST-agents available
     */
    @Test
    public void testLoad(){
        squad=new Squad();
        agents=new Agent[2];
        agents[0]=new Agent();
        agents[0].setName("Amir Mor");
        agents[0].setSerialNumber("001");
        agents[0]=new Agent();
        agents[0].setName("Dani Cohen");
        agents[0].setSerialNumber("002");
        squad.load(agents);
        List<String> a=new LinkedList<String>();
        a.add("001");
        a.add("002");
        assertTrue(squad.getAgents(a));// returns true if they exist
    }
}
