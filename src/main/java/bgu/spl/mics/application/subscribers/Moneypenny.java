package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.AgentAvailableEvent;
import bgu.spl.mics.Callback;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.List;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {

	private  int serialNumber;

	public Moneypenny() {
		super("Moneypenny");
	}

	public Moneypenny(int num) {
		super("Moneypenny");
		serialNumber=num;
	}


	@Override
	protected void initialize() {
		Callback<AgentAvailableEvent> agentavailable=(AgentAvailableEvent e)->{
			List<String> serials= e.getEventInformation();//requested agents
			boolean availToMe=Squad.getInstance().getAgents(serials);//checks if the agents available and returns true if so
			MessageBrokerImpl.getInstance().complete(e,availToMe);//resolves the event
		};
		this.subscribeEvent(AgentAvailableEvent.class,agentavailable);
	}

}
