package bgu.spl.mics;

import java.util.List;

public class SendReleaseAgentsEvent implements Event<Boolean>{

    private String function;
    private List<String> agentsSerials;
    private int duration;
    private int QserialNumber;
    private List<String> agentsNames;

    public SendReleaseAgentsEvent(String function, List<String> agents, int duration) {
        this.function=function;
        this.agentsSerials=agents;
        this.duration=duration;
        QserialNumber=0;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) { this.function = function;    }

    public int getDuration() {  return duration;    }

    public void setDuration(int duration) {  this.duration = duration;    }

    public int getQserialNumber() { return QserialNumber;    }

    public void setQserialNumber(int qserialNumber) {  QserialNumber = qserialNumber;    }

    public List<String> getAgentsSerials() { return agentsSerials; }

    public void setAgentsSerials(List<String> agentsSerials) { this.agentsSerials = agentsSerials;  }

    public List<String> getAgentsNames() { return agentsNames; }

    public void setAgentsNames(List<String> agentsNames) { this.agentsNames = agentsNames;  }

}
