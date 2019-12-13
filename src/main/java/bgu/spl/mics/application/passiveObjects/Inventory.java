package bgu.spl.mics.application.passiveObjects;

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
	private static Inventory instance=null;
	private int version=0;

	/**
     * Retrieves the single instance of this class.
	 * @pre: none
	 * @post: default
     */
	public static Inventory getInstance() {
		synchronized (instance) {
			if (instance == null)
				instance = new Inventory();
			return instance;
		}
	}

	private synchronized Iterator<String> iterator(){
		return new Iterator<String>() { //Anonymous
			final int originalVersion = version;
			int index = 0;

			public boolean hasNext() {
				return gadgets.size()>0;
			}

			public String next(){
				synchronized (this) {
					if (originalVersion != version)
						throw new IllegalStateException("The version changed");
					String gad = gadgets.get(index);
					index++;
					return gad;
				}
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
	 */
	public void printToFile(String filename){
		//Gson gson = new Gson();
		try{
			Iterator<String> gad=iterator();
			while(gad.hasNext()){

			}
		}catch(Exception e){
			//clearFile
			printToFile(filename);
		}
	}
}
