package bgu.spl.mics.application.passiveObjects;




import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;

/**
 *  That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {
	private List<String> gadgets;
	private static Inventory instance=new Inventory();
	private int version=0;

	/**
     * Retrieves the single instance of this class.
	 * @pre: none
	 * @post: default
     */
	public static Inventory getInstance() {
			return instance;
	}

	private synchronized Iterator<String> iterator(){
		return new Iterator<String>() { //Anonymous class
			final int originalVersion = version;
			int index = 0;

			public boolean hasNext() {
				//try {
					if (originalVersion != version) {
						throw new IllegalStateException("The version has changed");
					}
					return index < gadgets.size();
				//}catch (){
				//}
			}

			public String next(){
				//try{
					synchronized (this) {
						if (originalVersion != version)
							throw new IllegalStateException("The version has changed");
						String gad = gadgets.get(index);
						index++;
						return gad;
					}
				//}catch(){
				//}
			}
		};
	}

	/**
     * Initializes the inventory. This method adds all the items given to the gadget
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
	 * @pre: none
	 * @pre: for each i  {@param inventory} inventory[i]!=null
	 * @post: gadgets.size=={@pre gadget}.size()+inventory.size()
     */
	public synchronized void load (String[] inventory) {
		for(int i=0;i<inventory.length;i++){
			gadgets.add(inventory[i]);
		}
	}
	
	/**
     * acquires a gadget and returns 'true' if it exists.
     * <p>
     * @param gadget 		Name of the gadget to check if available
     * @return 	‘false’ if the gadget is missing, and ‘true’ otherwise
	 *
	 * @pre: none
	 * @post: exists()==true && gadgets.size()=={@pre gadgets}.size()-1
	 * @post: exists()==false && gadgets.size()=={@pre gadgets}.size()
     */
	public synchronized boolean getItem(String gadget){
		boolean isExist=gadgets.contains(gadget);
		if(isExist) {
			gadgets.remove(gadget);
			version++;
		}
		return isExist;
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Gadget> which is a
	 * List of all the gadgets in the diary.
	 * This method is called by the main method in order to generate the output.
	 * implemented by optimistic try and fail
	 */
	public void printToFile(String filename){
		Gson gson = new Gson();
		File file=new File(filename);
		try{
			FileWriter writer = new FileWriter(file);
			Iterator<String> gad=iterator();
			while(gad.hasNext()){
				gson.toJson(gad.next(), writer);
			}
		}catch(Exception e){
			file.delete();
			printToFile(filename);
		}
	}
}
