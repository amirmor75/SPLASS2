package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
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

	private void subscribeToAgentAvailable(){
		Callback<AgentAvailableEvent> agentAvailable=(AgentAvailableEvent e)->{
			List<String> serials= e.getEventInformation();//requested agents
			boolean availToMe=Squad.getInstance().getAgents(serials);//checks if the agents available and returns true if so
			MessageBrokerImpl.getInstance().complete(e,availToMe);//resolves the event
		};
		this.subscribeEvent(AgentAvailableEvent.class,agentAvailable);
	}

	private  void subscribeToSendRelease(){
		//MoneyPenny received this event from M inorder to execute a mission- whats requires an access to Squad
		Callback<SendReleaseAgentsEvent> sendRelease=(SendReleaseAgentsEvent event)->{
			String function=event.getFunction();
			if(function.equals("send"))
				Squad.getInstance().sendAgents(event.getAgentsSerials(),event.getDuration());
			if(function.equals("release"))
				Squad.getInstance().releaseAgents(event.getAgentsSerials());
			event.setQserialNumber(serialNumber);
			event.setAgentsNames(Squad.getInstance().getAgentsNames(event.getAgentsSerials()));
		};
		subscribeEvent(SendReleaseAgentsEvent.class,sendRelease);
	}

	private void subscribeToTermination(){
		Callback<TerminationBroadCast> terminateCall=(TerminationBroadCast timeDuration)->{
			//terminate When the program duration over
			terminate();
		};
		subscribeBroadcast(TerminationBroadCast.class,terminateCall);
	}

	@Override
	protected void initialize() {
		subscribeToAgentAvailable();
		subscribeToSendRelease();
		subscribeToTermination();
	}

}
