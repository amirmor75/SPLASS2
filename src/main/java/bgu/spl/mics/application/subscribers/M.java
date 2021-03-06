package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;

import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;
import java.util.List;


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
			if(Thread.currentThread().isInterrupted())
				terminate();
			currentDuration=e.getCurrentDuration();
		};
		this.subscribeBroadcast(TimeBroadCast.class,timeCall);
	}

	private void addToReport(MissionInfo info, Future<FutureResult<Integer, List<String>>> agentAvailFuture, Future<FutureResult<Integer, Integer>> gadgetAvailFuture){
		Report report = new Report();//add a report to the diary
		report.setMissionName(info.getMissionName());
		report.setM(this.serialNumber);
		report.setMoneypenny(agentAvailFuture.get().getSecResult());
		report.setAgentsSerialNumbersNumber(info.getSerialAgentsNumbers());
		report.setAgentsNames(agentAvailFuture.get().getThirdResult());
		report.setGadgetName(info.getGadget());
		report.setTimeCreated(currentDuration);
		report.setTimeIssued(info.getTimeIssued());
		report.setQTime(gadgetAvailFuture.get().getSecResult());
		Diary.getInstance().addReport(report);
	}

	private void subscribeToMissionReceived(){
		//our callback wait() for the AgentsAvailableEvent we sent to Moneypenny
		Callback<MissionReceivedEvent> mCall=(MissionReceivedEvent e)->{
			System.out.println("M  "+serialNumber+" received mission "+e.getEventInformation().getMissionName() +" from Inetllegence");
			Diary.getInstance().incrementTotal(); //In any case (mission either executed or aborted), Diary.total will be incremented.
			Future<Boolean> isSend=new Future<>(); //true- send,  false- release
			MissionInfo info =e.getEventInformation();//asks for the availability of the agents
			AgentAvailableEvent agentAvailableEvent=new AgentAvailableEvent(info.getSerialAgentsNumbers(),isSend,info.getDuration());
			Future<FutureResult<Integer, List<String>>> agentAvailFuture = getSimplePublisher().sendEvent(agentAvailableEvent);
			if(agentAvailFuture.get()==null) {
				terminate();
			}
			else {
				if (agentAvailFuture.get() != null && agentAvailFuture.get().isAvailable()) {
					if (Thread.currentThread().isInterrupted()) {
						terminate();
						isSend.resolve(false);
					} else {
						GadgetAvailableEvent gadgetAvailableEvent = new GadgetAvailableEvent(info.getGadget()); //asks for the availability of gadgets
						Future<FutureResult<Integer, Integer>> gadgetAvailFuture = getSimplePublisher().sendEvent(gadgetAvailableEvent);
						if(gadgetAvailFuture.get() == null)
							terminate();
						else {
							if (currentDuration < info.getTimeExpired() && gadgetAvailFuture.get() != null && gadgetAvailFuture.get().isAvailable()) {
								if (Thread.currentThread().isInterrupted()) {
									terminate();
									isSend.resolve(false);
								} else {
									System.out.println("mission " + info.getMissionName() + " is executing...");
									isSend.resolve(true);
									addToReport(info, agentAvailFuture, gadgetAvailFuture);
								}
							} else {
								isSend.resolve(false);
							}
						}
					}
				} else {
					isSend.resolve(false);
				}
			}
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
		subscriveToTermination();
		subscribeToMissionReceived();
		subscribedToTimeBroadCast();
	}
}
