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

			List<String> serials= e.getEventInformation();//requested agents
			System.out.println("MP "+serialNumber+" try getAgents");
			boolean availToMe=Squad.getInstance().getAgents(serials);//checks if the agents available and returns true if so
			FutureResult result = new FutureResult(availToMe, serialNumber, Squad.getInstance().getAgentsNames(serials));

			if(Thread.currentThread().isInterrupted()) {
				System.out.println("XXXXXx");
				complete(e, result);//resolves the event
				System.out.println("YYYYYY");
				terminate();
			}
			else {
				System.out.println("MP " + serialNumber + " getAgents");

				System.out.println("AgentAvailableEvent " + serialNumber + ". are they: " + availToMe);

				complete(e, result);//resolves the event
				Future<Boolean> send = e.getSend();
				Boolean isSend = send.get();
				if (isSend!=null && isSend) {
					System.out.println("try to send");
					if(Thread.currentThread().isInterrupted()) {
						complete(e, result);//resolves the event
						terminate();
					}
					else {
						Squad.getInstance().sendAgents(e.getEventInformation(), e.getDuration());
						if(Thread.currentThread().isInterrupted()) {
							complete(e, result);//resolves the event
							terminate();
						}
					}
				} else {
					if(Thread.currentThread().isInterrupted()) {
						complete(e, result);//resolves the event
						terminate();
					}
					else {
						Squad.getInstance().releaseAgents(e.getEventInformation());
						System.out.println("MP " + serialNumber + " releasing...");
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
		subscribeToAgentAvailable();
		subscribeToTermination();
	}

}
