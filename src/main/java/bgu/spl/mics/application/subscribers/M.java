package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;

import bgu.spl.mics.SendReleaseAgentsEvent;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;


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

	private void subscribedToTimeBroadCast(){
		//This callback update the current duration of the program when a timeBroadCast received.
		Callback<TimeBroadCast> timeCall=(TimeBroadCast e)->{
			currentDuration=e.getCurrentDuration();
		};
		this.subscribeBroadcast(TimeBroadCast.class,timeCall);
	}

	private void subscribeToMissionReceived(){
		//our callback wait() for the AgentsAvailableEvent we sent to Moneypenny
		Callback<MissionReceivedEvent> mCall=(MissionReceivedEvent e)->{
			//asks for the availability of the agents
			MissionInfo info =e.getEventInformation();
			AgentAvailableEvent agentAvailableEvent=new AgentAvailableEvent(info.getSerialAgentsNumbers());
			Future<Boolean> agentAvailFuture = getSimplePublisher().sendEvent(agentAvailableEvent);

			//asks for the availability of gadgets
			GadgetAvailableEvent gadgetAvailableEvent=new GadgetAvailableEvent(e.getEventInformation().getGadget());
			Future<Boolean> gadgetAvailFuture = getSimplePublisher().sendEvent(gadgetAvailableEvent);

			//M acknowledges Moneypenny to send the required agents
			//if expiry time passed, M orders Moneypenny to release() all agents.
			if(agentAvailFuture.isDone() && gadgetAvailFuture.isDone() && currentDuration<info.getTimeExpired()){
				SendReleaseAgentsEvent sendRelease=new SendReleaseAgentsEvent("send",agentAvailableEvent.getEventInformation(),info.getDuration());
				getSimplePublisher().sendEvent(sendRelease);
				//add a report to the diary
				Report report=new Report();
				report.setMissionName(info.getMissionName());
				report.setM(this.serialNumber);
				report.setMoneypenny(sendRelease.getQserialNumber());
				report.setAgentsSerialNumbersNumber(info.getSerialAgentsNumbers());
				report.setAgentsNames(sendRelease.getAgentsNames());
				report.setGadgetName(info.getGadget());
				report.setTimeCreated(currentDuration);
				report.setTimeCreated(info.getTimeIssued());
				report.setQTime(gadgetAvailableEvent.getTimeQreceived());
				Diary.getInstance().addReport(report);
			}
			else{
				getSimplePublisher().sendEvent(new SendReleaseAgentsEvent("release",agentAvailableEvent.getEventInformation(),info.getDuration()));
			}
			Diary.getInstance().incrementTotal(); //In any case (mission either executed or aborted), Diary.total will be incremented.
		};
		this.subscribeEvent(MissionReceivedEvent.class,mCall);
	}

	private void subscriveToTermination(){
		Callback<TerminationBroadCast> terminateCall=(TerminationBroadCast timeDuration)->{
			//terminate When the program duration over
			terminate();
		};
		subscribeBroadcast(TerminationBroadCast.class,terminateCall);
	}

	@Override
	protected synchronized void initialize() {
		subscribeToMissionReceived();
		subscribedToTimeBroadCast();
		subscriveToTermination();
  }
  


}
