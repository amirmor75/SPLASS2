package bgu.spl.mics.application.passiveObjects;


import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {
	private List<Report> reports;
	private int total=0;
	private static class SingletonHolder {
		private static Diary instance = new Diary();
	}


	/**
	 * Retrieves the single instance of this class.
	 */
	public static Diary getInstance() {
		return SingletonHolder.instance;
	}

	public List<Report> getReports() {
		return reports;
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public void addReport(Report reportToAdd){//not elegant, need to be atomic!!!!!
		synchronized (this){
			reports.add(reportToAdd);
		}
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename){
		Gson gson = new Gson();
		File file=new File(filename);
		try{
			FileWriter writer = new FileWriter(file);
			Iterator<Report> reportIterator=reports.iterator();
			while(reportIterator.hasNext()){
				Report currentRep=reportIterator.next();
				String missionName=currentRep.getMissionName();
				int timeCreated=currentRep.getTimeCreated();
				int M=currentRep.getM();
				String info= String.format("missionName: %s, timeCreated: %s,M: %s",missionName,timeCreated,M);
				gson.toJson(info,writer);
			}
			gson.toJson("number of missions:"+total,writer);
		}catch(Exception ignored){}
	}

	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public int getTotal(){
		return total;
	}
}
