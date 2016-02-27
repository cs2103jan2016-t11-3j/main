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

	private static int startTime = -1;
	private static int endTime = -1;
	
	public void processTime(String input) {
		convertToArray(input);
		if (list.size() == 2 || list.size() == 1) {
			furtherProcessTime();
		}
	}
	
	/**
	 * this method splits string into array list for easy manipulation.
	 * ideally the start and end date if any, will be split into two elements of the arraylist (events)
	 * if there is only one date, there will be no splitting
	 */
	public void convertToArray(String input) {
		if (input.contains("-")) {
			for (String temp: input.split("-")) {
				temp = temp.replaceAll(" ", "");
				list.add(temp);
				}
		} else if (input.contains("to")) {
			for (String temp: input.split("to")) {
				temp = temp.replaceAll(" ", "");
	 			list.add(temp);
	 			}
		}
	}
	
	public void furtherProcessTime() {
		boolean isCross = false, isPM = false;
		for (int i = 0; i < list.size(); i++) {
			String temp = list.get(i);
			String tempNext = list.get(1);
			if (temp.contains(TIME_AM_1) || temp.contains(TIME_AM_2) ||
					temp.contains(TIME_AM_3) || temp.contains(TIME_AM_4)) {
				setTime(temp, i, false);
			} else if (temp.contains(TIME_PM_1) || temp.contains(TIME_PM_2) ||
					temp.contains(TIME_PM_3) || temp.contains(TIME_PM_4)) {
				setTime(temp, i, true);
			} else if (tempNext.contains(TIME_AM_1) || tempNext.contains(TIME_AM_2) ||
			tempNext.contains(TIME_AM_3) || tempNext.contains(TIME_AM_4)) {
				isCross = checkIfCrossover(temp, tempNext);
				if (isCross) {
					setTime(temp, i, true);
				} else {
					setTime(temp, i, false);
				}
				
			} else if (tempNext.contains(TIME_PM_1) || tempNext.contains(TIME_PM_2) ||
			tempNext.contains(TIME_PM_3) || tempNext.contains(TIME_PM_4)) {
				isCross = checkIfCrossover(temp, tempNext);
				if (isCross) {
					setTime(temp, i, false);
				} else {
					setTime(temp, i, true);
				}
			}
			}
	}
	
	
	private static boolean checkIfCrossover(String temp, String tempNext) {
		int first, second;
		temp = temp.replaceAll("[:!-/a-zA-Z]+", "");
		tempNext = tempNext.replaceAll("[:!-/a-zA-Z]+", "");
		first = Integer.parseInt(temp);
		second = Integer.parseInt(tempNext);
		
		if (first > second) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * this method cleans the string and converts to the integer form. It will be manipulated into HHMM format
	 */
	public void setTime(String input, int position, boolean isPM) {
		input = input.replaceAll("[:!-/a-zA-Z]+", "");
		input = input.replaceAll(" ", "");
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
		
		if (temp > 2400) {
			return;
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
	
	//method for testing 
	public int getListSize() {
		return list.size();
	}
	
	//method for testing
	public String getListElement(int i) {
		return list.get(i);
	}
	
	//met
	public void clearList() {
		list.clear();
	}
	
	public void resetTime() {
		startTime = -1;
		endTime = -1;
	}
}
