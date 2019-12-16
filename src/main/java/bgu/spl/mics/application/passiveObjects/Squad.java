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

	private Map<String, Agent> agents;
	private static Squad instance=null;

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		synchronized (instance) {
			if (instance == null)
				instance = new Squad();
			return instance;
		}
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param inventory 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public synchronized void load (Agent[] inventory) {
		for(int i=0;i<inventory.length;i++){
			agents.put(inventory[i].getSerialNumber(),inventory[i]);
		}
	}

	/**
	 * Releases agents.
	 */
	public synchronized void releaseAgents(List<String> serials){
		Iterator<String> serial=serials.iterator();
		while(serial.hasNext()){
			agents.remove(serial.next());
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public synchronized void sendAgents(List<String> serials, int time){
		try {Thread.sleep(time);} catch (InterruptedException e) {}
		Iterator<String> serial=serials.iterator();
		Agent agent;
		while(serial.hasNext()){
			agent=agents.get(serial.next());
			if(agent!=null)
				agent.release();
		}
	}

	/**
	 * this is a blocking method- it wait until the agent is available for new mission
	 * when the agent available- He will be acquired to new mission
	 * @param agent an Agent from the map
	 */
	private void acquireAgent(Agent agent){
		synchronized (agent) {
			while (!agent.isAvailable()) {
				try {
					agent.wait();
				} catch (InterruptedException ignored) { }
			}
			agent.acquire();
			agent.notifyAll();
		}
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public synchronized boolean getAgents(List<String> serials){
		boolean allExists=true;
		String agentName;
		Agent agent;
		Iterator<String> serial=serials.iterator();
		while(serial.hasNext()){
			agentName=serial.next();
			if(!agents.containsKey(agentName))
				allExists=false;
			else{
				agent=agents.get(agentName);
				acquireAgent(agent);
			}
		}
		return allExists;
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
        	agent=agents.get(serial.next());
        	if(agent!=null)
        		names.add(agent.getName());
		}
        return names;
    }

}
