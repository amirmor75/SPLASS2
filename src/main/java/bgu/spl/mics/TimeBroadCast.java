package bgu.spl.mics;

public class TimeBroadCast implements Broadcast {
    private int currentDuration;

    public TimeBroadCast(int duration){
        currentDuration=duration;
    }

    public int getCurrentDuration() {
        return currentDuration;
    }
}
