package bgu.spl.mics.application;


import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {


    public static void main(String[] args) {
//        if(args.length!=1)
//            throw new IllegalArgumentException(" ");
//
//        try {
//            File file = new File(args[0]);
//            BufferedReader br = new BufferedReader(new FileReader(file));
//            String str;
//            String json="";
//            while ((str = br.readLine()) != null)
//               json+=str+"\n";
//
//            InputJsonDefinition inputFile=new Gson().fromJson(json,InputJsonDefinition.class);
//
//            /**
//             * initialize all classes by the input json.
//             */
//            Inventory.getInstance().load(inputFile.getInventory());
//            Inventory inventory=Inventory.getInstance();
//
//            //initialization of all the rest...
//
//            Squad.getInstance().load(inputFile.getSquad());
//            Squad squad=Squad.getInstance();
//
//        }catch(Exception ignored){}


        Inventory inv=Inventory.getInstance();
        String []load={"a","b","c","d"};
        inv.load(load);
        System.out.println(inv.getItem("a"));
        System.out.println(inv.getItem("a"));
        System.out.println(inv.getItem("a"));
        System.out.println(inv.getItem("e"));
    }
}
