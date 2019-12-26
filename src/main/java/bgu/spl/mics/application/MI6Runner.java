package bgu.spl.mics.application;


import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.TimeBroadCast;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.*;
import com.google.gson.Gson;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {


    public static void main(String[] args) throws IOException {
//        if(args.length!=3)
//            throw new IllegalArgumentException("Three inputs expected");

        for(int j=0;j<20;j++){
        String inputFileName="/home/tal/Desktop/AA.json"; //args[0];
        String inventoryOutputName="inventory.json"; //args[1];
        String diaryOutputName="diary.json";// args[2];

        InputJsonDefinition input=null;
        try (Reader reader = new FileReader(inputFileName)) {
             input=new Gson().fromJson(reader,InputJsonDefinition.class);
        }catch(FileNotFoundException ignored){ System.out.println("file not found"); }

        if(input!=null) {
            /**
             * initialize all classes by the input json.
             */

            //Inventory:
            Inventory.getInstance().load(input.getInventory());

            //Squad:
            Squad.getInstance().load(input.getSquad());

            //Subscribers- M, MoneyPenny, MoneypennySendAgents, Intelligence and Q:
            List<Subscriber> subscribers = new LinkedList<>();

            subscribers.add(new Q());

            for (int i = 0; i < input.getServices().getMoneypenny(); i++)
                subscribers.add(new Moneypenny(i));

            for (int i = 0; i < input.getServices().getM(); i++)
                subscribers.add(new M(i));

            for (int i = 0; i < input.getServices().getIntelligence().size(); i++)
                subscribers.add(input.getServices().getIntelligence().get(i));


            Thread[] threadArray = new Thread[subscribers.size() + 1];
            //TimeService:
            threadArray[0] = new Thread(new TimeService(input.getServices().getTime()));
            for (int i = 1; i < threadArray.length; i++)
                threadArray[i] = new Thread(subscribers.get(i - 1));

            for (int i = 0; i < threadArray.length; i++)
                threadArray[i].start();

            //main Thread waits until time-service is terminating
            try {
                threadArray[0].join();
            } catch (InterruptedException ignored) {
            }

            //wait two time-ticks to be sure all the thread were terminated
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }


            //termination
            for (int i = 0; i < threadArray.length; i++)
                threadArray[i].interrupt();


            //outputs
            Inventory.getInstance().printToFile(inventoryOutputName);
            Diary.getInstance().printToFile(diaryOutputName);

            System.out.println(j+"!!!!!!!!!!!");
        }
        }
    }
}
