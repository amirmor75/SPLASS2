package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;

import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.sql.Time;


/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {

	private  int serialNumber;
	private int currentDuration;

	public M() {
		super("M");
		serialNumber=0;
		currentDuration=0;
	}

	public M(int num) {
		super("M");
		serialNumber=num;
		currentDuration=0;
	}


	@Override
	protected synchronized void initialize() {

		//This callback update the current duration of the program when a timeBroadCast received.
		Callback<TimeBroadCast> timeCall=(TimeBroadCast e)->{
			currentDuration=e.getCurrentDuration();
		};
		this.subscribeBroadcast(TimeBroadCast.class,timeCall);


		//our callback wait() for the AgentsAvailableEvent we sent to Moneypenny
		Callback<MissionReceivedEvent> mCall=(MissionReceivedEvent e)->{
			//asks for the availability of the agents
			MissionInfo info =e.getEventInformation();
			AgentAvailableEvent agentAvailableEvent=new AgentAvailableEvent(info.getSerialAgentsNumbers());
			Future<Boolean> agentAvailFuture = getSimplePublisher().sendEvent(agentAvailableEvent);

			//asks for the availability of gadgets
			GadgetAvailableEvent gadgetAvailableEvent=new GadgetAvailableEvent(e.getEventInformation().getGadget());
			Future<Boolean> gadgetAvailFuture = getSimplePublisher().sendEvent(gadgetAvailableEvent);

			//checks if the mission’s expiry time had reached. If not - then M acknowledges Moneypenny
			// to Agent.send() the required agents, and a report will be added to the diary
			if(currentDuration<info.getTimeExpired()){

			}
			//In any case (mission either executed or aborted), Diary.total will be incremented.

		};

		this.subscribeEvent(MissionReceivedEvent.class,mCall);
  }
  

	
		
	

}
