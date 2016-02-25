import java.util.ArrayList;


public class AddProcessor {
	
	public static ArrayList<String> list = new ArrayList<String>();
	public static ArrayList<Integer> dateList = new ArrayList<Integer>();
	
	private static String task;
	private static int startDate = -1;
	private static int endDate = -1;
	private static int startTime = -1;
	private static int endTime = -1;
	
	private static TimeProcessor TP = new TimeProcessor();
	private static DateProcessor DP = new DateProcessor();
	
	/**
	 * this method will take in the command and allocate the correct information
	 * to task/date/time
	 * 
	 * @param input    string input from user
	 */
	public void addCommand(String input) {
		//splits add command into arraylist
		for (String temp: input.split(" ")) {
 			list.add(temp);
 		}
		//get the task stuff
		readTask(list);
	}
	
	/**
	 * this method extracts out the string from the array list for tasks
	 */
	private static void readTask(ArrayList <String>tempList) {
 		int i = 1;
 		String _task = null;
 		while (!isStartOfDate(tempList.get(i)) && i < tempList.size()) {
 			_task = _task + tempList.get(i);
 			i++;
 		}
 		task = _task;
 		readDate(list, i+1);
	}
	
	// this method checks for first occurrence of the keyword indicating date input
 	private static boolean isStartOfDate(String input) {
 		return input == "date:";
 	}
	
 	/**
 	 * this method will extract out the string from the arraylist for date
 	 */
 	private static void readDate(ArrayList<String> list, int index) {
 		String _date = null;
 		//forms the date string
 		while (!isStartOfTime(list.get(index)) && index < list.size()) {
 			_date = _date + list.get(index);
 			index++;
 		}
 		DP.processDate(_date);
 		setDate();
 		readTime(list, index);
 	}
 	
 	//checks for the "time:" keyword
 	private static boolean isStartOfTime(String input) {
 		return input == "time:";
 	}
 	
 	/**
 	 * this method will extract the string from the array list for time
 	 */
 	private static void readTime(ArrayList<String> list, int index) {
 		String _time = null;
 		
 		while (index < list.size()) {
 			_time = _time + list.get(index);
 			index++;
 		}
 		TP.processTime(_time);
 		setTime();
 	}
 	
 	private static void setDate() {
		startDate = DP.getStartDate();
		endDate = DP.getEndDate();
 	}
 	
 	private static void setTime() {
 		startTime = TP.getStartTime();
 		endTime = TP.getEndTime();
 	}
 	
 	public String getTask() {
 		return task;
 	}
 	
 	public int getStartDate() {
 		return startDate;
 	}
 	
 	public int getEndDate() {
 		return endDate;
 	}
 	
 	public int getStartTime() {
 		return startTime;
 	}
 	
 	public int getEndTime() {
 		return endTime;
 	}
 	
 	
 	
}
