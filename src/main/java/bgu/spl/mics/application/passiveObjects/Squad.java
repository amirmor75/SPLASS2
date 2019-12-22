package bgu.spl.mics.application.passiveObjects;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private Map<String, Agent> agentMap;//serial number is the key

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
	public synchronized void releaseAgents(List<String> serials){
		for (String serial: serials) {
			agentMap.get(serial).release();
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public synchronized void sendAgents(List<String> serials, int time){
		try {Thread.sleep(time);} catch (InterruptedException ignored) {}
		releaseAgents(serials);
	}

	/**
	 * this is a blocking method- it wait until the agent is available for new mission
	 * when the agent available- He will be acquired to new mission
	 * @param agent an Agent from the map
	 */
	private synchronized void acquireAgent(Agent agent){
		// synchronized agent is not good.. 
		while (!agent.isAvailable()) {
			try {
				agent.wait();
			} catch (InterruptedException ignored) { }
		}
		agent.acquire();
		agent.notifyAll();
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
				acquireAgent(agentMap.get(s));
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
