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
		Callback<AgentAvailableEvent> agentAvailable=(AgentAvailableEvent e)-> {
			if (Thread.currentThread().isInterrupted()) {
				FutureResult<Integer,List<String>> result = new FutureResult<>(false, serialNumber, null);
				complete(e, result);
				terminate();
			}
			else {
				System.out.println("MP " + serialNumber + " got AgentAvailableEvent");
				List<String> serials = e.getEventInformation();//requested agents
				boolean availToMe = Squad.getInstance().getAgents(serials);//checks if the agents available and returns true if so
				FutureResult<Integer,List<String>> result = new FutureResult<>(availToMe, serialNumber, Squad.getInstance().getAgentsNames(serials));
				complete(e, result);//resolves the event
				if (Thread.currentThread().isInterrupted()) {
					terminate();
				} else {
					Future<Boolean> send = e.getSend();
					Boolean isSend = send.get();
					if (Thread.currentThread().isInterrupted()) {
						terminate();
					} else {
						if (isSend != null && isSend) {
							System.out.println("MP " + serialNumber + " sending...");
							Squad.getInstance().sendAgents(e.getEventInformation(), e.getDuration());
							if (Thread.currentThread().isInterrupted())
								terminate();
						} else {
							Squad.getInstance().releaseAgents(e.getEventInformation());
							System.out.println("MP " + serialNumber + " releasing...");
						}
					}
				}
			}
		};
		this.subscribeEvent(AgentAvailableEvent.class,agentAvailable);
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
        subscribeToTermination();
        subscribeToAgentAvailable();
	}

}
