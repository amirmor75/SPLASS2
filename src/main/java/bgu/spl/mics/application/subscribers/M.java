package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;

import bgu.spl.mics.application.passiveObjects.MissionInfo;


/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {

	private  int serialNumber;

	public M() {
		super("M");
	}

	public M(int num) {
		super("M");
		serialNumber=num;
	}


	@Override
	protected synchronized void initialize() {//  defines callback
    
		//our callback wait() for the AgentsAvailableEvent we sent to Moneypenny
		Callback<MissionReceivedEvent> mCall=(MissionReceivedEvent e)->{
			//asks for the availability of the agents
			MissionInfo info =e.getEventInformation();
			AgentAvailableEvent agentAvailableEvent=new AgentAvailableEvent(info.getSerialAgentsNumbers());
			Future<Boolean> agentAvailFuture = getSimplePublisher().sendEvent(agentAvailableEvent);
			//asks for the availability of gadgets
			GadgetAvailableEvent gadgetAvailableEvent=new GadgetAvailableEvent(e.getEventInformation().getGadget());
      
      
		};

		this.subscribeEvent(MissionReceivedEvent.class,mCall);

  }
  

	
		
	

}
