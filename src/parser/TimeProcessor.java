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
	
	private final ArrayList<String> list = new ArrayList<String>();

	private int startTime = -1;
	private int endTime = -1;
	
	/**
	 * this method takes in the user's input from add/edit/search processor
	 * 
	 *@param input  time input from user
	 */
	public void processTime(String input) {
		input = input.replaceFirst("time:", "");
		convertToArray(input);
		if (list.size() == 2 || list.size() == 1) {
			furtherProcessTime();
		}
	}
	
	/**
	 * this method splits string into array list for easy manipulation.
	 * ideally the start and end date if any, will be split into two elements of the arraylist (events)
	 * if there is only one date, there will be no splitting
	 * 
	 * @param input  time input by user
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
		} else {
			list.add(input);
		}
	}
	
	/**
	 * this method will check process the time input by recognizing am/pm and if the time is 
	 * in order (8-5pm is recognized as 8am to 5pm)
	 */
	public void furtherProcessTime() {
		boolean isCross = false;
		for (int i = 0; i < list.size(); i++) {
			String temp = list.get(i);
			if (list.size() > 1) {
				String tempNext = list.get(1);
				if (isAM(temp)) {
					setTime(temp, i, false);
				} else if (isPM(temp)) {
					setTime(temp, i, true);
				} else if (isAM(tempNext)) {
					isCross = checkIfCrossover(temp, tempNext);
					if (isCross) {
						setTime(temp, i, true);
					} else {
						setTime(temp, i, false);
					}
					
				} else if (isPM(tempNext)) {
					isCross = checkIfCrossover(temp, tempNext);
					if (isCross) {
						setTime(temp, i, false);
					} else {
						setTime(temp, i, true);
					}
				} else {
					setTime(temp, i, false);
				}
			} else {
				if (isAM(temp)) {
					setTime(temp, i, false);
				} else if (isPM(temp)) {
					setTime(temp, i, true);
				} else {
					setTime(temp, i, false);
				}
			}
			}
	}

	public boolean isPM(String temp) {
		return temp.contains(TIME_PM_1) || temp.contains(TIME_PM_2) ||
				temp.contains(TIME_PM_3) || temp.contains(TIME_PM_4);
	}

	public boolean isAM(String temp) {
		return temp.contains(TIME_AM_1) || temp.contains(TIME_AM_2) ||
				temp.contains(TIME_AM_3) || temp.contains(TIME_AM_4);
	}
	
	
	/**
	 * this method checks if the two time are in running order, meaning 8-1pm would
	 * mean 8am to 1pm instead of 8pm to 1pm
	 * 
	 * @param temp      start time
	 * @param tempNext  end time
	 */
	private static boolean checkIfCrossover(String temp, String tempNext) {
		int first, second;
		temp = temp.replaceAll("[:!-/a-zA-Z]+", "");
		tempNext = tempNext.replaceAll("[:!-/a-zA-Z]+", "");
		first = Integer.parseInt(temp);
		second = Integer.parseInt(tempNext);
		
		if (first < 100) {
			first = first * 100;
		}
		
		if (second < 100) {
			second = second * 100;
		}
		
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
	
	//method used to reset all dates for testing
	public void resetTime() {
		startTime = -1;
		endTime = -1;
	}
}
