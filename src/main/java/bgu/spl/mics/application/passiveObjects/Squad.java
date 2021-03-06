package bgu.spl.mics.application.passiveObjects;

import java.util.*;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private Map<String, Agent> agentMap=new Hashtable<>();//serial number is the key

	private static class SingletonHolder {
		private static Squad instance=new Squad();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		return SingletonHolder.instance;
	}


	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public synchronized void load (Agent[] agents) {
		for (Agent agent: agents)
			agentMap.put(agent.getSerialNumber(),agent);
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		for (String serial: serials) {
			if(agentMap.get(serial)!=null)
				agentMap.get(serial).release();
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   time-ticks to sleep
	 */
	public void sendAgents(List<String> serials, int time){
		try {Thread.sleep(time*100);} catch (InterruptedException ignored) {
			Thread.currentThread().interrupt();
		}
		releaseAgents(serials);
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public synchronized boolean getAgents(List<String> serials){
		for (String s:serials){
			if(!agentMap.containsKey(s)){
				releaseAgents(serials);
				return false;
			}
			else{
				Agent agent=agentMap.get(s);
				agent.acquire(); //the wait() takes place in Agent
				System.out.println(agent.getName()+" acquired");
			}
		}
		return true;
	}

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
        List<String> names=new LinkedList<>();
        Iterator<String> serial=serials.iterator();
        Agent agent;
        while(serial.hasNext()){
        	agent=agentMap.get(serial.next());
        	if(agent!=null)
        		names.add(agent.getName());
		}
        return names;
    }

}
