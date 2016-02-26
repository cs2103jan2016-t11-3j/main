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
	
	private static ArrayList<String> list = new ArrayList<String>();
	
	private static TimeProcessor TP = new TimeProcessor();
	private static DateProcessor DP = new DateProcessor();
	
	private static String _task = null;
	private static Integer _startDate = -1;
	private static Integer _endDate = -1;
	private static Integer _startTime = -1;
	private static Integer _endTime = -1;
	
	public void processEdit(String input) {
		convertToArray(input);
		String clean_string = cleanString();
		if (isDate() && isNotTask()) {
			//make string 
			DP.processDate(clean_string, false);
			setDate(clean_string);
		} else if (isTime() && isNotTask()) {
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
	private static void convertToArray(String input) {
		for (String temp : input.split(" ")) {
			list.add(temp);
		}
		
		//remove "edit" and "number"
		list.remove(0);
		list.remove(0);
	}
	
	private static String cleanString() {
		String toReturn = null;
		for (int i = 0; i < list.size(); i++) {
			toReturn = toReturn + " " + list.size();
		}
		return toReturn.toLowerCase();
	}
	
	private static boolean isDate() {
		for (int i = 0; i < list.size(); i++) {
			String input = list.get(i);
			if (input == MONTH_1_1 || input == MONTH_1_2 || 
					input == MONTH_2_1 || input == MONTH_2_2 || 
					input == MONTH_3_1 || input == MONTH_3_2 ||
					input == MONTH_4_1 || input == MONTH_4_2 ||
					input == MONTH_5_1 || input == MONTH_6_1 ||
					input == MONTH_6_2 || input == MONTH_7_1 ||
					input == MONTH_7_2 || input == MONTH_8_1 ||
					input == MONTH_8_2 || input == MONTH_9_1 ||
					input == MONTH_9_2 || input == MONTH_10_1 ||
					input == MONTH_10_2 || input == MONTH_11_1 ||
					input == MONTH_11_2 || input == MONTH_12_1 ||
					input == MONTH_12_2) {
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
	private static boolean isAlternativeDate(String input) {
		if (input.contains("/")) {
			input.replaceAll("/", "");
			input.replaceAll("[a-zA-Z]", "");
			if (!input.isEmpty()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	private static boolean isNotTask() {
		//form string
		String command = null;
		for (int i = 0; i < list.size(); i++) {
			command = command + list.get(i);
		}
		//remove date numbers
		command.replaceAll("[0-9]+", "");
		command = removeMonths(command);
		
		if (command.length() > NOT_TASK_CONSTANT) {
			return false;
		} else {
			return true;
		}
	}
	
	private static String removeMonths(String input) {
		input.toLowerCase();
		input.replace("jan", "");
		input.replace("january", "");
		input.replace("feb", "");
		input.replace("february", "");
		input.replace("march", "");
		input.replace("mar", "");
		input.replace("apr", "");
		input.replace("april", "");
		input.replace("may", "");
		input.replace("june", "");
		input.replace("jun", "");
		input.replace("july", "");
		input.replace("jul", "");
		input.replace("august", "");
		input.replace("aug", "");
		input.replace("september", "");
		input.replace("sept", "");
		input.replace("oct", "");
		input.replace("october", "");
		input.replace("november", "");
		input.replace("nov", "");
		input.replace("december", "");
		input.replace("dec", "");
		return input;
	}
	
	private static boolean isTime() {
		for (int i = 0; i < list.size(); i++) {
			String temp = list.get(i);
			if (temp.contains(TIME_AM_1) || temp.contains(TIME_AM_2)
					|| temp.contains(TIME_AM_3) || temp.contains(TIME_AM_4)
					|| temp.contains(TIME_PM_1) || temp.contains(TIME_PM_2)
					|| temp.contains(TIME_PM_3) || temp.contains(TIME_PM_4)) {
				return true;
			} 
		}
		return false;
	}
	
	private static void setDate(String input) {
		if (input.contains("start")) {
			setStartDate(DP.getStartDate());
		} else if (input.contains("end")) {
			setEndDate(DP.getEndDate());
		} else {
			setStartDate(DP.getStartDate());
			setEndDate(DP.getEndDate());
		}
	}
	
	private static void setTime(String input) {
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

	public static void setTask(String _task) {
		EditProcessor._task = _task;
	}

	public Integer getStartDate() {
		return _startDate;
	}

	public static void setStartDate(Integer _startDate) {
		EditProcessor._startDate = _startDate;
	}

	public Integer getEndDate() {
		return _endDate;
	}

	public static void setEndDate(Integer _endDate) {
		EditProcessor._endDate = _endDate;
	}

	public Integer getStartTime() {
		return _startTime;
	}

	public static void setStartTime(Integer _startTime) {
		EditProcessor._startTime = _startTime;
	}

	public Integer getEndTime() {
		return _endTime;
	}

	public static void setEndTime(Integer _endTime) {
		EditProcessor._endTime = _endTime;
	}
}
