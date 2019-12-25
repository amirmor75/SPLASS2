package bgu.spl.mics;

import java.util.List;

public class SendReleaseAgentsEvent implements Event<Boolean>{

    private String function;
    private List<String> agentsSerials;
    private int duration;
    private Future<Boolean> future;

    public SendReleaseAgentsEvent(String function, List<String> agents, int duration) {
        this.function=function;
        this.agentsSerials=agents;
        this.duration=duration;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) { this.function = function;    }

    public int getDuration() {  return duration;    }

    public void setDuration(int duration) {  this.duration = duration;    }

    public List<String> getAgentsSerials() { return agentsSerials; }

    public void setAgentsSerials(List<String> agentsSerials) { this.agentsSerials = agentsSerials;  }

    public Future<Boolean> getFuture() {
        return future;
    }

    public void setFuture(Future<Boolean> future) {
        this.future = future;
    }
}
