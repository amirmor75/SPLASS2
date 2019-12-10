package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;

public class AgentsAvailableEventTask implements AgentsAvailableEvent {
    private Agent result;
    private Future<Agent> myFuture;

    public Future<Agent> getMyFuture() {
        return myFuture;
    }

    public Agent getResult() {
        return result;
    }
}
