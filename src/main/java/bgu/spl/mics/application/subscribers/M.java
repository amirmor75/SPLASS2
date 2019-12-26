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

			System.out.println("M "+serialNumber+" got time: "+e.getCurrentDuration());

			currentDuration=e.getCurrentDuration();
		};
		this.subscribeBroadcast(TimeBroadCast.class,timeCall);
	}

	private void subscribeToMissionReceived(){
		//our callback wait() for the AgentsAvailableEvent we sent to Moneypenny
		Callback<MissionReceivedEvent> mCall=(MissionReceivedEvent e)->{

			System.out.println("M  "+serialNumber+" recieved mission"+e.getEventInformation().getMissionName() +" from Inetllegence");
			Diary.getInstance().incrementTotal(); //In any case (mission either executed or aborted), Diary.total will be incremented.


			Future<Boolean> isSend=new Future<>(); //true- send,  false- release

			MissionInfo info =e.getEventInformation();//asks for the availability of the agents
			AgentAvailableEvent agentAvailableEvent=new AgentAvailableEvent(info.getSerialAgentsNumbers(),isSend,info.getDuration());
			Future<FutureResult<Integer, List<String>>> agentAvailFuture = getSimplePublisher().sendEvent(agentAvailableEvent);

			System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
			if (agentAvailFuture.get()!=null && agentAvailFuture.get().isAvailable()) {
				System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
				if(Thread.currentThread().isInterrupted()) {
					terminate();
					System.out.println("M is terminating");
				}
				else {
					GadgetAvailableEvent gadgetAvailableEvent = new GadgetAvailableEvent(info.getGadget()); //asks for the availability of gadgets
					Future<FutureResult<Integer, Integer>> gadgetAvailFuture = getSimplePublisher().sendEvent(gadgetAvailableEvent);

					//M acknowledges Moneypenny to send the required agents
					//if expiry time passed, M orders Moneypenny to release() all agents.
					System.out.println("ccccccccccccccccccccccccccccccccc");
					if (gadgetAvailFuture.get()!=null && gadgetAvailFuture.get().isAvailable() && currentDuration < info.getTimeExpired()) {
						System.out.println("dddddddddddddddddddddddddddddddd");

						if(Thread.currentThread().isInterrupted()) {
							terminate();
							System.out.println("M is terminating");
						}
						else {
							System.out.println("mission " + info.getMissionName() + " is executing...");

							isSend.resolve(true);
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
					}
					else {
					isSend.resolve(false);
					}
				}
			}
			else {
				System.out.println("XXXXXXXXXXXXXXXXXXXX");
				isSend.resolve(false);
				System.out.println("YYYYYYYYYYYYYYYYYYYY");
			}

		};
		this.subscribeEvent(MissionReceivedEvent.class,mCall);
	}

	private void subscriveToTermination(){
		Callback<TerminationBroadCast> terminateCall=(TerminationBroadCast timeDuration)->{
			System.out.println("M  "+serialNumber+" terminating...");

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
