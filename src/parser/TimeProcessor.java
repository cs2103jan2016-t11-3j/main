package parser;

import java.util.ArrayList;

public class TimeProcessor {
	private static final String TIME_AM_1 = "AM";
	private static final String TIME_AM_2 = "A.M.";
	private static final String TIME_AM_3 = "am";
	private static final String TIME_AM_4 = "a.m.";
	private static final String TIME_PM_1 = "PM";
	private static final String TIME_PM_2 = "A.M.";
	private static final String TIME_PM_3 = "pm";
	private static final String TIME_PM_4 = "p.m.";
	
	private static final ArrayList<String> list = new ArrayList<String>();

	private static int startTime;
	private static int endTime;
	
	public void processTime(String input) {
		convertToArray(input);
		if (list.size() == 2 || list.size() == 1) {
			//event
			furtherProcessTime();
		} else if (list.isEmpty()) {
			//floating task
			//start and end date = -1
		}
	}
	
	/**
	 * this method splits string into array list for easy manipulation.
	 * ideally the start and end date if any, will be split into two elements of the arraylist (events)
	 * if there is only one date, there will be no splitting
	 */
	private static void convertToArray(String input) {
		if (input.contains("-")) {
			for (String temp: input.split("-")) {
				list.add(temp);
				}
		} else if (input.contains("to")) {
			for (String temp: input.split("to")) {
	 			list.add(temp);
	 			}
		}
	}
	
	private static void furtherProcessTime() {
		for (int i = 0; i < list.size(); i++) {
			String temp = list.get(i);
			if (temp.contains(TIME_AM_1) || temp.contains(TIME_AM_2) ||
					temp.contains(TIME_AM_3) || temp.contains(TIME_AM_4)) {
				setTime(temp, i, false);
			} else if (temp.contains(TIME_PM_1) || temp.contains(TIME_PM_2) ||
					temp.contains(TIME_PM_3) || temp.contains(TIME_PM_4)) {
				setTime(temp, i, true);
			}
		}
	}
	
	/**
	 * this method cleans the string and converts to the integer form. It will be manipulated into HHMM format
	 */
	private static void setTime(String input, int position, boolean isPM) {
		input.replaceAll("[!-/a-zA-Z]", "");
		int temp = 0;
		temp = Integer.parseInt(input);
		if (temp < 100) {
			temp = temp * 100;
		}
		
		if (isPM) {
			temp = temp + 1200;
		}
		
		if (temp == 2400) {
			temp = 0000;
		}
		
		if (position == 0) {
			startTime = temp;
			endTime = startTime;
		} else if (position == 1) {
			endTime = temp;
		}
		}
	
	public int getStartTime() {
		return startTime;
	}
	
	public int getEndTime() {
		return endTime;
	}
}
