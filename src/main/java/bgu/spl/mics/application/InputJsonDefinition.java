package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.subscribers.Intelligence;

import java.util.List;

/**
 * Definition of the input json file
 */
public class InputJsonDefinition {
    private String[] inventory;
    private Services services;
    private Agent[] squad;

    public String[] getInventory() {
        return inventory;
    }

    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;
    }

    public Agent[] getSquad() {
        return squad;
    }

    public void setSquad(Agent[] squad) {
        this.squad = squad;
    }

    public void setInventory(String[] inventory) {
        this.inventory = inventory;
    }

    public class Services {
        private int M;
        private int Moneypenny;
        private List<Intelligence> intelligence;
        private int time;

        public int getM() {
            return M;
        }

        public void setM(int m) {
            M = m;
        }

        public int getMoneypenny() {
            return Moneypenny;
        }

        public void setMoneypenny(int moneypenny) {
            Moneypenny = moneypenny;
        }

        public List<Intelligence> getIntelligence() {
            return intelligence;
        }

        public void setIntelligence(List<Intelligence> intelligence) {
            this.intelligence = intelligence;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }
}
