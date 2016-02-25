import java.util.ArrayList;

public class SearchProcessor {
	
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
	
	public static String class_task;
	public static int class_date;
	public static int class_date_day;
	public static int class_date_month;
	public static int class_date_year;
	public static int class_time;
	
	private static ArrayList<String> list = new ArrayList<String>();
	
	public void processSearchTerm(String input) {
		convertToArray(input);
		isTime(input);
		isDate(input);
		}
	
	/**
	 * this method checks for presence of time-keywords in the array 
	 * and converting them into integer to be parsed back to parser class
	 */
	private static boolean isTime(ArrayList<String> input) {
		for (String testing : input) {
			//nid to make sure it is a time
			if (testing.contains(TIME_AM_1) || testing.contains(TIME_AM_2) || 
					testing.contains(TIME_AM_3) || testing.contains(TIME_AM_4)) {
				convertToTime(testing, false);
				return true;
			} else if (testing.contains(TIME_PM_1) || testing.contains(TIME_PM_2)|| 
					testing.contains(TIME_PM_3) || testing.contains(TIME_PM_4)) {
				convertToTime(testing, true);
				return true;
			} 
		}
		return false;
	}
	
	private static void convertToArray(String input) {
			for (String temp: input.split(" ")) {
				list.add(temp);
			}
	}
	
	/**
	 * this method converts a string into an integer that represents
	 * time in HHMM format
	 */
	private static void convertToTime(String input, boolean isPM) {
		input.replaceAll("[!-/a-zA-Z]", "");
		if (!input.isEmpty()) { 
		int time = Integer.parseInt(input);
			if (time < 100) {
				time = time * 100;
			}
 		
			if (isPM) {
				time = time + 1200;
			}
 		
			if (time == 2400) {
				time = 0000;
			}
			class_time = time;
		}
	}
	
	private static boolean isDate(ArrayList <String> input) {
		for (int i = 0;i < input.size(); i++) {
			String testing = input.get(i);
			testing.toLowerCase();
			if (isAValidDate(testing)) {
			setMonth();
			setDay();
			setYear();
			}
		}
	}
	
	private static boolean isAValidDate(String input) {
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
	
	private static void ()
}
