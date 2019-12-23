package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.LinkedList;
import java.util.List;

/**
 * A Publisher only.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {

	private List<MissionInfo> missions;

	public List<MissionInfo> getMissions(){
		return missions;
	}

	public Intelligence() {
		super("Intelligence");
		missions=new LinkedList<>();
	}

	public Intelligence(List<MissionInfo> missionsInfo) {
		super("Intelligence");
		missions=missionsInfo;
	}

	@Override
	protected void initialize() {
		Callback<TimeBroadCast> intelligenceCall=(TimeBroadCast timeDuration)->{
			List<MissionInfo> forDeletion=new LinkedList<>();
			for(MissionInfo mission:missions) {
				if(timeDuration.getCurrentDuration()>mission.getTimeExpired()) //if the time expired then the mission it won’t be executed at all.
					forDeletion.add(mission);
				else if (mission.getTimeIssued() == timeDuration.getCurrentDuration())
					getSimplePublisher().sendEvent(new MissionReceivedEvent(mission));
			}
			missions.removeAll(forDeletion);
		};
		subscribeBroadcast(TimeBroadCast.class,intelligenceCall);
	}


}
