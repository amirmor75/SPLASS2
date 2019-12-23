package bgu.spl.mics.application;


import bgu.spl.mics.Publisher;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.LinkedList;
import java.util.List;


/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {


    public static void main(String[] args) {
//        if(args.length!=1)
//            throw new IllegalArgumentException(" ");

        try (Reader reader = new FileReader("/home/tal/Desktop/AA.json")) {

            InputJsonDefinition input=new Gson().fromJson(reader,InputJsonDefinition.class);
            /**
             * initialize all classes by the input json.
             */

            //Inventory:
            Inventory.getInstance().load(input.getInventory());
            Inventory inventory=Inventory.getInstance();

            //Subscribers- M, MoneyPenny, Intelligence:
            List<Subscriber> subscribers=new LinkedList<>();

            for(int i=0;i<input.getServices().getM();i++)
                subscribers.add(new M(i));

            for(int i=0;i<input.getServices().getMoneypenny();i++)
                subscribers.add(new Moneypenny(i));

            for(Intelligence intelligence:input.getServices().getIntelligence())
                subscribers.add(intelligence);

            //TimeService:
            TimeService timeService=new TimeService(input.getServices().getTime());

            //Squad:
            Squad.getInstance().load(input.getSquad());
            Squad squad=Squad.getInstance();


            /**
             * rest of the code:
             * every subscriber run on his single thread...
             * ThreadPool
             */


        }catch(Exception ignored){ System.out.println("file not found"); }
    }
}
