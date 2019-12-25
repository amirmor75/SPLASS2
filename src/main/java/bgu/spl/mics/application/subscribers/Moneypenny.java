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
			System.out.println("MP "+serialNumber+" got AgentAvailableEvent");

			Future<Boolean> send=e.getSend();

			List<String> serials= e.getEventInformation();//requested agents
			boolean availToMe=Squad.getInstance().getAgents(serials);//checks if the agents available and returns true if so

			System.out.println("AgentAvailableEvent "+serialNumber+". are they: "+availToMe);

			FutureResult result=new FutureResult(availToMe,serialNumber,Squad.getInstance().getAgentsNames(serials));
			complete(e,result);//resolves the event

			Boolean isSend=send.get();
			if(isSend) {
				Squad.getInstance().sendAgents(e.getEventInformation(), e.getDuration());
				System.out.println("MP "+serialNumber+ " sending...");
			}
			else {
				Squad.getInstance().releaseAgents(e.getEventInformation());
				System.out.println("MP "+serialNumber+ " releasing...");
			}
		};
		this.subscribeEvent(AgentAvailableEvent.class,agentAvailable);
	}

	private void subscribeToTermination(){
		Callback<TerminationBroadCast> terminateCall=(TerminationBroadCast timeDuration)->{
			System.out.println("MP "+serialNumber+"  terminating...");
			//terminate When the program duration over
			terminate();
		};
		subscribeBroadcast(TerminationBroadCast.class,terminateCall);
	}

	@Override
	protected void initialize() {
		subscribeToAgentAvailable();
		subscribeToTermination();
	}

}
