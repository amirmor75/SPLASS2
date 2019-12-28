package bgu.spl.mics.application;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.*;
import com.google.gson.Gson;
import java.io.*;
import java.util.LinkedList;
import java.util.List;



/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {

    public static void main(String[] args) throws IOException {
        if(args.length!=3)
            throw new IllegalArgumentException("Three inputs expected");

        String inputFileName=args[0];
        String inventoryOutputName=args[1];
        String diaryOutputName=args[2];


        InputJsonDefinition input=null;
        try (Reader reader = new FileReader(inputFileName)) {
             input=new Gson().fromJson(reader,InputJsonDefinition.class);
        }catch(FileNotFoundException ignored){ System.out.println("file not found"); }

        if(input!=null) {

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

            subscribers.addAll(input.getServices().getIntelligence());

            Thread[] threadArray = new Thread[subscribers.size() + 1];
            //TimeService:
            threadArray[0] = new Thread(new TimeService(input.getServices().getTime()));
            for (int i = 1; i < threadArray.length; i++)
                threadArray[i] = new Thread(subscribers.get(i - 1));

            for (Thread thread : threadArray) thread.start();

            //main Thread waits until time-service is terminating
            try {
                threadArray[0].join();
            } catch (InterruptedException ignored) { }

            //termination
            for (Thread thread : threadArray) thread.interrupt();


            //outputs
            Inventory.getInstance().printToFile(inventoryOutputName);
            Diary.getInstance().printToFile(diaryOutputName);


        }
    }
}
