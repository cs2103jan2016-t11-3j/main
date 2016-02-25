package parser;

import java.util.ArrayList;

/**
 * This class focuses on breaking down a string for date into the relevant components
 */

public class DateProcessor {
	
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
	
	private static final int VALUE_JAN = 1;
	private static final int VALUE_FEB = 2;
	private static final int VALUE_MAR = 3;
	private static final int VALUE_APR = 4;
	private static final int VALUE_MAY = 5;
	private static final int VALUE_JUN = 6;
	private static final int VALUE_JUL = 7;
	private static final int VALUE_AUG = 8;
	private static final int VALUE_SEPT = 9;
	private static final int VALUE_OCT = 10;
	private static final int VALUE_NOV = 11;
	private static final int VALUE_DEC = 12;
	
	private static final int DEFAULT_YEAR = 2016;
	
	private static final ArrayList<String> list = new ArrayList<String>();
	
	private static int start_day = -1;
	private static int start_month = -1;
	private static int start_year = -1;
	private static int end_day = -1;
	private static int end_month = -1;
	private static int end_year = -1;
	
	private static int startDate = -1;
	private static int endDate = -1;
	
	/**
	 * this method takes in a string input and processes it
	 * 
	 * @param input    date string that is in the format
	 */
	public void processDate(String input) {
		convertToArray(input);
		if (list.size() == 2 || list.size() == 1) {
			//event
			furtherProcessDate();
		} else if (list.isEmpty()) {
			//floating task
			//start and end date = -1
		}
		
		if (start_month == -1) {
			start_month = end_month;
		}
		
		if (start_year == -1) {
			start_year = DEFAULT_YEAR;
		}
		
		if (end_year == -1) {
			end_year = DEFAULT_YEAR;
		}
		setDates();	
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
	
	/**
	 * this method further processes the newly made array list
	 */
	private static void furtherProcessDate() {
		for (int i = 0; i < list.size(); i++) {
			if (hasMonth(list.get(i))) {
				String temp = null;
				setMonth(list.get(i), i);
				temp = removeMonth(list.get(i));
				splitStringAndProcess(temp, i);
			} else if (hasSlash(list.get(i))) {
				setMonthWithSlash(list.get(i), i);
			} else {
				processMonthlessDate(list.get(i));
			}
		}
	}
	
	private static boolean hasMonth(String input) {
		input.toLowerCase();
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
		} else {
			return false;
		}
	}
	
	private static boolean hasSlash(String input) {
		return input.contains("/");
	}
	
	private static void setMonth(String input, int i) {
		if (i == 0) {
			start_month = setMonthInDataProcessor(input);
			end_month = start_month;
		} else if (i == 1) {
			end_month = setMonthInDataProcessor(input);
		}
	}
	
	private static String removeMonth(String input) {
		return input.replace("[a-zA-Z]+", " ");
	}
	
	private static void splitStringAndProcess(String input, int i) {
		ArrayList<String> list = new ArrayList<String>();
		int _day = 0, _year = 0;
		for (String temp : input.split(" ")) {
			list.add(temp);
		}
		
		//set the day n year according to the filled elements of the array
		if (list.size() == 2) {
			_day = Integer.parseInt(list.get(0));
			_year = Integer.parseInt(list.get(1));
		} else if (list.size() == 1) {
			_day = Integer.parseInt(list.get(0));
		}
		
		//set attributes
		if (i == 0) {
			start_day = _day;
			end_day = _day;
			start_year = _year;
			end_year = _year;
		} else if (i == 1) {
			end_day = _day;
			end_year = _year;
		}
	}
	
	private static void setMonthWithSlash(String input,int i) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		//split date into new arraylist
		for (String temp : input.split("/")) {
			int tempInt = Integer.parseInt(temp);
			list.add(tempInt);
		}
		
		
		if (i == 0) {
			start_day = list.get(0);
			end_day = start_day;
			start_month = list.get(1);
			end_month = start_month;
			if (list.size() == 3) {
				start_year = list.get(2);
				end_year = start_year;
			}
		} else if (i == 1) {
			end_day = list.get(0);
			end_month = list.get(1);
			if (list.size() == 3) {
				end_year = list.get(2);
			}
		}
	}
	
	/**
	 * this method takes in the string without month keywords or slashes
	 * and checks if it contains only digits. sets start_day if it contains 
	 * digits only.
	 */
	private static void processMonthlessDate(String input) {
		input.replaceAll("[a-zA-Z]+", "");
		int temp = Integer.parseInt(input);
		if(!input.isEmpty() && input.matches("[0-9]+")) {
			start_day = temp;
		} else {
			start_day = -1;	
		}
	}
	
	private static int setMonthInDataProcessor(String month) {
		if (month == MONTH_1_1 || month == MONTH_1_2) {
			return VALUE_JAN;
		} else if (month == MONTH_2_1 || month == MONTH_2_2) {
			return VALUE_FEB;
		} else if (month == MONTH_3_1 || month == MONTH_3_2) {
			return VALUE_MAR;
		} else if (month == MONTH_4_1 || month == MONTH_4_2) {
			return VALUE_APR;
		} else if (month == MONTH_5_1) {
			return VALUE_MAY;
		} else if (month == MONTH_6_1 || month == MONTH_6_2) {
			return VALUE_JUN;
		} else if (month == MONTH_7_1 || month == MONTH_7_2) {
			return VALUE_JUL;
		} else if (month == MONTH_8_1 || month == MONTH_8_2) {
			return VALUE_AUG;
		} else if (month == MONTH_9_1 || month == MONTH_9_2) {
			return VALUE_SEPT;
		} else if (month == MONTH_10_1 || month == MONTH_10_2) {
			return VALUE_OCT;
		} else if (month == MONTH_11_1 || month == MONTH_11_2) {
			return VALUE_NOV;
		} else if (month == MONTH_12_1 || month == MONTH_12_2) {
			return VALUE_DEC;
		} else {
			return -1;
		}
	}
	
	private static void setDates() {
		if (start_day != -1 && start_month != -1 && start_year != -1) {
			startDate = start_day + start_month * 100 + start_year * 10000;
		}
		
		if (end_day != -1 && end_month != -1 && end_year != -1) {
			endDate = end_day + end_month * 100 + end_year * 10000;
		}		
	}
	
	public int getStartDate() {
		return startDate;
	}
	
	public int getEndDate() {
		return endDate;
	}
	


}