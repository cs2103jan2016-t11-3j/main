package parser;

import java.util.ArrayList;

public class EditProcessor {
	
	private static final String TIME_AM_1 = "AM";
	private static final String TIME_AM_2 = "A.M.";
	private static final String TIME_AM_3 = "am";
	private static final String TIME_AM_4 = "a.m.";
	private static final String TIME_PM_1 = "PM";
	private static final String TIME_PM_2 = "A.M.";
	private static final String TIME_PM_3 = "pm";
	private static final String TIME_PM_4 = "p.m.";
	
	private static final String MONTH_1_1 = "january";
	private static final String MONTH_1_2 = "jan";
	private static final String MONTH_2_1 = "february";
	private static final String MONTH_2_2 = "feb";
	private static final String MONTH_3_1 = "march";
	private static final String MONTH_3_2 = "mar";
	private static final String MONTH_4_1 = "april";
	private static final String MONTH_4_2 = "apr";
	private static final String MONTH_5_1 = "may";
	private static final String MONTH_6_1 = "june";
	private static final String MONTH_6_2 = "jun";
	private static final String MONTH_7_1 = "july";
	private static final String MONTH_7_2 = "jul";
	private static final String MONTH_8_1 = "august";
	private static final String MONTH_8_2 = "aug";
	private static final String MONTH_9_1 = "september";
	private static final String MONTH_9_2 = "sept";
	private static final String MONTH_10_1 = "october";
	private static final String MONTH_10_2 = "oct";
	private static final String MONTH_11_1 = "november";
	private static final String MONTH_11_2 = "nov";
	private static final String MONTH_12_1 = "december";
	private static final String MONTH_12_2 = "dec";
	
	private static final Integer NOT_TASK_CONSTANT = 5;
	
	private ArrayList<String> list = new ArrayList<String>();
	
	private TimeProcessor TP = new TimeProcessor();
	private DateProcessor DP = new DateProcessor();
	
	private String _task = null;
	private int _startDate = -1;
	private int _endDate = -1;
	private int _startTime = -1;
	private int _endTime = -1;
	
	/**
	 * this method will take in the string from the parser 
	 * and break down its component, determining if it is a task, time or date edit
	 */
	public void processEdit(String input) {
		convertToArray(input);
		String clean_string = cleanString();
		if (isDate() && !isTask()) {
			DP.processDate(clean_string, false);
			setDate(clean_string);
		} else if (isTime() && !isTask()) {
			TP.processTime(clean_string);
			setTime(clean_string);
		} else {
			setTask(clean_string);
		}
	}
	
	/**
	 * this method will convert instruction into string array list
	 * and remove the "edit" and number
	 */
	public void convertToArray(String input) {
		for (String temp : input.split(" ")) {
			list.add(temp);
		}
		
		//remove "edit" and "number"
		list.remove(0);
		//list.remove(0); 	// REMOVED THIS BECAUSE EDIT FUNCTION NEEDS THE INDEX NUMBER
	}
	
	/**
	 * this method will re-form the command that the user input
	 * without "edit" and the index number
	 */
	public String cleanString() {
		String toReturn = null;
		for (int i = 0; i < list.size(); i++) {
			if(i == 0) {
				toReturn = list.get(i);
			} else {
				toReturn = toReturn + " " + list.get(i);
			}
		}
		return toReturn.toLowerCase();
	}
	
	
	public boolean isDate() {
		for (int i = 0; i < list.size(); i++) {
			String input = list.get(i);
			input.toLowerCase();
			if (input.contains(MONTH_1_1) || input.contains(MONTH_1_2) || 
					input.contains(MONTH_2_1) || input.contains(MONTH_2_2) || 
					input.contains(MONTH_3_1) || input.contains(MONTH_3_2) ||
					input.contains(MONTH_4_1) || input.contains(MONTH_4_2) || 
					input.contains(MONTH_5_1) || input.contains(MONTH_6_1) ||
					input.contains(MONTH_6_2) || input.contains(MONTH_7_1) ||
					input.contains(MONTH_7_2) || input.contains(MONTH_8_1) ||
					input.contains(MONTH_8_2) || input.contains(MONTH_9_1) ||
					input.contains(MONTH_9_2) || input.contains(MONTH_10_1) ||
					input.contains(MONTH_10_2) || input.contains(MONTH_11_1) ||
					input.contains(MONTH_11_2) || input.contains(MONTH_12_1) ||
					input.contains(MONTH_12_2)) {
				return true;
			} else if (isAlternativeDate(input)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * this method checks if the string is a date in the "dd/mm/yyyy" 
	 * or "dd/mm" format
	 */
	public boolean isAlternativeDate(String input) {
		if (input.contains("/")) {
			input = input.replaceAll("/", "");
			input = input.replaceAll("[a-zA-Z]+", "");
			if (!input.isEmpty()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	//this method checks if the string input is task
	public boolean isTask() {
		//form string
		String command = null;
		for (int i = 0; i < list.size(); i++) {
			if (i == 0) {
				command = list.get(i);
			} else {
				command = command + list.get(i);
			}
		}
		//remove date
		command = command.replaceAll("\\d", "");
		command = command.replaceAll("/", "");
		command = removeMonths(command);
		if (command.length() > NOT_TASK_CONSTANT) {
			return true;
		} else {
			return false;
		}
	}
	
	//this method will remove month keywords 
	public String removeMonths(String input) {
		input.toLowerCase();
		input = input.replaceFirst("january", "");
		input = input.replace("jan", "");
		input = input.replace("february", "");
		input = input.replace("feb", "");
		input = input.replace("march", "");
		input = input.replace("mar", "");
		input = input.replace("april", "");
		input = input.replace("apr", "");
		input = input.replace("may", "");
		input = input.replace("june", "");
		input = input.replace("jun", "");
		input = input.replace("july", "");
		input = input.replace("jul", "");
		input = input.replace("august", "");
		input = input.replace("aug", "");
		input = input.replace("september", "");
		input = input.replace("sept", "");
		input = input.replace("oct", "");
		input = input.replace("october", "");
		input = input.replace("november", "");
		input = input.replace("nov", "");
		input = input.replace("december", "");
		input = input.replace("dec", "");
		return input;
	}
	
	/**
	 * this method check if the string passed in is a time input
	 * 
	 * comment -> go and refactor further
	 */
	public boolean isTime() {
		for (int i = 0; i < list.size(); i++) {
			String temp = list.get(i);
			if (i > 0) {
				String tempPrev = list.get(i-1);
				if (hasAMPM(temp) && hasNumber(temp)) {
					return true;
				} else if (hasAMPM(temp) && hasNumber(tempPrev)) {
					return true;
				}
			} else {
				if (hasAMPM(temp) && hasNumber(temp)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean hasNumber(String temp) {
		return temp.matches(".*\\d.*");
	}
	
	//this method checks if the string contains any indicator of time
	public boolean hasAMPM(String temp) {
		return temp.contains(TIME_AM_1) || temp.contains(TIME_AM_2)
				|| temp.contains(TIME_AM_3) || temp.contains(TIME_AM_4)
				|| temp.contains(TIME_PM_1) || temp.contains(TIME_PM_2)
				|| temp.contains(TIME_PM_3) || temp.contains(TIME_PM_4);
	}
	
	//this method sets the date for the object by using the date processor 
	//class to performing the processing
	public void setDate(String input) {
		if (input.contains("start")) {
			setStartDate(DP.getStartDate());
		} else if (input.contains("end")) {
			setEndDate(DP.getEndDate());
		} else {
			setStartDate(DP.getStartDate());
			setEndDate(DP.getEndDate());
		}
	}
	
	//this method sets the time for the object by using the time processor 
	//class to performing the processing
	public void setTime(String input) {
		if (input.contains("start")) {
			setStartTime(TP.getStartTime());
		} else if (input.contains("end")) {
			setEndTime(TP.getEndTime());
		} else {
			setStartTime(TP.getStartTime());
			setEndTime(TP.getEndTime());
		}
	}
	
	public String getTask() {
		return _task;
	}

	public void setTask(String task) {
		_task = task;
	}

	public int getStartDate() {
		return _startDate;
	}

	public void setStartDate(int startDate) {
		_startDate = startDate;
	}

	public int getEndDate() {
		return _endDate;
	}

	public void setEndDate(int endDate) {
		_endDate = endDate;
	}

	public int getStartTime() {
		return _startTime;
	}

	public void setStartTime(int startTime) {
		_startTime = startTime;
	}

	public int getEndTime() {
		return _endTime;
	}

	public void setEndTime(int endTime) {
		_endTime = endTime;
	}
	
	//method used to obtain the size of the list for testing 
	public int getListSize() {
		return list.size();
	}
	
	//method used to get the ith element in the list for testing
	public String getListElement(int i) {
		return list.get(i);
	}
	
	//method used to get the ith element in the list for testing
	public void clearList() {
		list.clear();
	}
 	
 	public void resetAll() {
 		setTask(null);
 		setStartDate(-1);
 		setEndDate(-1);
 		setStartTime(-1);
 		setEndTime(-1);
 		TP.resetTime();
 		TP.clearList();
 		DP.clearList();
 		DP.resetDate();
 	}
}
