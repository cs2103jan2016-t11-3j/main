package parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class focuses on breaking down a string for date into the relevant components
 * 
 * it takes in a date input with varying degree of flexibility, in dd/mm/yy or number-month-year format
 * 
 * @author sylvesterchin
 * 
 */

public class DateParser {
	
	// possible words that the user may input
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
	
	//corresponding integer value for each month
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
	
	//changeable default year
	private static final int DEFAULT_YEAR = 2016;
	
	private final ArrayList<String> list = new ArrayList<String>();
	
	private int start_day = -1;
	private int start_month = -1;
	private int start_year = -1;
	private int end_day = -1;
	private int end_month = -1;
	private int end_year = -1;
	
	private int startDate = -1;
	private int endDate = -1;
	
	private String dateString;
	private LocalDate dateObject = LocalDate.MAX;
	
	/**
	 * This method takes in a string input returns LocalDate to the datetimeparser
	 * 
	 * @param input    date string that is in the format
	 * @throws Exception 
	 */
	public void processDate(String input) throws Exception {
		if (!input.isEmpty()) {
			furtherProcessDate(input);
			if (start_month == -1) {
				start_month = end_month;
			}
			setDates();
		}
	}
	
	
	/**
	 * this method further processes the input from date-time-parser
	 * 
	 * format types include
	 * 1. Month spelt out:    3rd june 2013 - 4th june 2014
	 * 2. "slash" format:     4/5-5/6
	 * 3. incomplete format:  3 - 6 june 2015
	 * @throws Exception 
	 */
	public void furtherProcessDate(String input) throws Exception {
		if (hasMonth(input)) {
			setMonth(input);
			input = removeMonth(input);
			splitStringAndProcess(input);
		} else if (hasSlash(input)) {
			setMonthWithSlash(input);
		} else if (isRelative(input)) { 
			processRelativeDate(input);
		} else {
			throw new Exception("Invalid Date Format");
		}
	}
	
	
	/**
	 * method checks if the input string is a relative date
	 * @param input   date string 
	 * 			e.g. tmr (relative date)
	 * 			e.g. 8 june (not relative date)
	 * @return boolean   true,if the date string is a relative date
	 */
	public boolean isRelative(String input) {
		if (input.matches(Constants.REGEX_RELATIVE_DATE_ALL) || input.matches(Constants.REGEX_DAYS_TEXT)) {
			return true;
		} else {
			return false;
		}
		
	}
	
	/**
	 * method will set date object for relative date inputs such as today, tmr, next week
	 * 
	 * @param input
	 */
	public void processRelativeDate(String input) {
		input = input.trim();
		if (input.matches(Constants.REGEX_RELATIVE_DATE_1)) {
			if (input.matches("today")) {
				dateObject = LocalDate.now();
			} else {
				dateObject = LocalDate.now().plusDays(1);
			}
		} else if (input.matches("("+"(next )?"+ Constants.REGEX_DAYS_TEXT+")")) { // GOT PROBLEM
			input = input.replaceAll("next ", "").trim();
			dateObject = LocalDate.now();
			if (LocalDate.now().getDayOfWeek().toString().toLowerCase().contains(input)) {
				dateObject = dateObject.plusWeeks(1);
			} else {
				while (!dateObject.getDayOfWeek().toString().toLowerCase().contains(input)) {
					//
					dateObject = dateObject.plusDays(1);
				}	
			}
		} else if (input.matches("next " + "(week|wk)(s)?")) {
			dateObject = LocalDate.now().plusWeeks(1);
		}
	}
	
	/**
	 * this method checks if the string contains keywords for any of the months
	 * 
	 * @param input    string element in array list representing one date
	 */
	public boolean hasMonth(String input) {
		Pattern dateTimePattern = Pattern.compile(Constants.REGEX_MONTHS_TEXT);
		Matcher matcher = dateTimePattern.matcher(input);
		if (matcher.find()) {
			return true;
		} else {
			return false;
		}
	}
	
	//this method checks if the date is in dd/mm/yy format
	public boolean hasSlash(String input) {
		return input.contains("/");
	}
	
	//this method will allocate relevant integer to month variable
	public void setMonth(String input) {
			start_month = setMonthInDataProcessor(input);
	}
	
	//method removes all the characters in the date
	public String removeMonth(String input) {
		return input.replaceAll("[a-zA-Z]+", " ");
	}
	
	/**
	 * this method will split a date into the day, month and year
	 * 
	 * @param input   date input by the user without the month
	 * @param i       indicates if the date is start or end
 	 * 
	 */
	public void splitStringAndProcess(String input) {
		ArrayList<String> templist = new ArrayList<String>();
		int _day = -1, _year = -1;
		if (input.startsWith(" ")) {
			input = input.replaceFirst(" ", "");
		}
		for (String temp : input.split("\\s+")) {
			temp = temp.replaceAll("[a-zA-Z]+", "");
			templist.add(temp);
		}
		//set the day n year according to the filled elements of the array
		if (templist.size() == 2) {
			_day = Integer.parseInt(templist.get(0));
			_year = Integer.parseInt(templist.get(1));
		} else if (templist.size() == 1) {
			_day = Integer.parseInt(templist.get(0));
		}
		//set attributes
		start_day = _day;
		start_year = _year;
	}
	
	/**
	 * this method will set the day, month and year for inputs with in dd/mm/yy format
	 * 
	 * @param input  input by user for one date, in dd/mm/yyyy format
	 * @param i      indicator if date is start or end date
	 * @throws Exception 
	 */
	public void setMonthWithSlash(String input) throws Exception {
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		for (String temp : input.split("/")) {
			temp = temp.replaceAll(" ", "");
			temp = temp.replaceAll("[a-zA-Z]+", "");
			int tempInt = Integer.parseInt(temp);
			list.add(tempInt);
		}
		if (list.size() != 1) {
			start_day = list.get(0);
			start_month = list.get(1);
			
			if (list.size() == 3) {
				start_year = list.get(2);
			}	
		} else {
			throw new Exception("Invalid Date");
		}
		
	}
	
	/**
	 * this method takes in the string without month keywords or slashes
	 * and checks if it contains only digits. sets start_day if it contains 
	 * digits only.
	 * 
	 *@param input  user input without month and year, just day
	 *@param i      indicator to show if input date is start or end
	 */
	public void processMonthlessDate(String input, int i) {
		input = input.replaceAll("[a-zA-Z]+", "");
		input = input.replaceAll(" ", "");
		
		if (input.isEmpty()) {
			return;
		}
		
		int temp = Integer.parseInt(input);
		
		if(i == 0) {
			if(!input.isEmpty() && input.matches("[0-9]+")) {
				start_day = temp;
			} else {
				start_day = -1;
			}
		} else if (i == 1) {
			if(!input.isEmpty() && input.matches("[0-9]+")) {
				end_day = temp;
			} else {
				end_day = -1;	
			}
		}
	}
	
	/**
	 * this method will return the corresponding month's integer value
	 */
	public int setMonthInDataProcessor(String month) {
		month = month.toLowerCase();
		if (month == MONTH_1_1 || month.contains(MONTH_1_2)) {
			return VALUE_JAN;
		} else if (month == MONTH_2_1 || month.contains(MONTH_2_2)) {
			return VALUE_FEB;
		} else if (month == MONTH_3_1 || month.contains(MONTH_3_2)) {
			return VALUE_MAR;
		} else if (month == MONTH_4_1 || month.contains(MONTH_4_2)) {
			return VALUE_APR;
		} else if (month.contains(MONTH_5_1)) {
			return VALUE_MAY;
		} else if (month == MONTH_6_1 || month.contains(MONTH_6_2)) {
			return VALUE_JUN;
		} else if (month == MONTH_7_1 || month.contains(MONTH_7_2)) {
			return VALUE_JUL;
		} else if (month == MONTH_8_1 || month.contains(MONTH_8_2)) {
			return VALUE_AUG;
		} else if (month == MONTH_9_1 || month.contains(MONTH_9_2)) {
			return VALUE_SEPT;
		} else if (month == MONTH_10_1 || month.contains(MONTH_10_2)) {
			return VALUE_OCT;
		} else if (month == MONTH_11_1 || month.contains(MONTH_11_2)) {
			return VALUE_NOV;
		} else if (month == MONTH_12_1 || month.contains(MONTH_12_2)) {
			return VALUE_DEC;
		} else {
			return -1;
		}
	}
	
	/**
	 * method sets the LocalDate for the object by cleaning up minor format differences
	 * and forming yyyyMMdd string
	 * 
	 * passes date in string format to setDateObject method for setting the LocalDate value
	 * @throws Exception 
	 */
	public void setDates() throws Exception {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		
		if (start_year < 100 && start_year != -1) {
			start_year = 2000 + start_year;
		} else if (start_year == -1) {
			start_year = DEFAULT_YEAR;
		}
		if (start_day > 31 || start_month > 12) {
			throw new Exception("Invalid Date");
		} else if (start_day != -1 && start_month != -1 && start_year != -1) {
			startDate = start_day + start_month * 100 + start_year * 10000;
		}
		
		dateString = Integer.toString(startDate); //now insert the damn dashes
		dateString = addDashes(dateString);
		setDateObject(dateString, dateFormatter);
	}
	
	/**
	 * method adds dashes into the date string to convert it into yyyy-MM-dd format
	 * 
	 * @param input   date string in yyyyMMdd format 
	 * 			e.g. 20140529
	 * @return string in yyyy-MM-dd format
	 * 			e.g. 2014-05-29
	 */
	private String addDashes(String input) {
		if (input.length() == 8) {
			String year, month, day;
			year = input.substring(0,4);
			month = input.substring(4,6);
			day = input.substring(6);
			return year + "-" + month + "-" + day;	
		} else {
			return "";
		}
	}
	
	
	
	//if already set due to relative date, the method will not set the date again
	public void setDateObject(String input, DateTimeFormatter dateFormatter) {
		if (dateObject == LocalDate.MAX && !input.isEmpty()) {
			dateObject = LocalDate.parse(input, dateFormatter);
		}
	}
	
	public int getStartDate() {
		return startDate;
	}
	
	public int getEndDate() {
		return endDate;
	}
	
	public String getDateString() {
		return dateString;
	}

	
	//method used to obtain the size of the list for testing 
	public int getListSize() {
		return list.size();
	}
	
	//method used to get the ith element in the list for testing
	public String getListElement(int i) {
		return list.get(i);
	}
	
	//method used to clear the arraylist of string for testing 
	public void clearList() {
		list.clear();
	}
	
	//method used to reset all dates for testing
	public void resetDate() {
		start_day = -1;
		start_month = -1;
		start_year = -1;
		end_day = -1;
		end_month = -1;
		end_year = -1;
		
		startDate = -1;
		endDate = -1;
		dateObject = LocalDate.MAX;
	}
	
	public int getStartDay() {
		return start_day;
	}
	
	public int getStartMonth() {
		return start_month;
	}
	public int getStartYear() {
		return start_year;
	}
	public int getEndDay() {
		return end_day;
	}
	public int getEndMonth() {
		return end_month;
	}
	public int getEndYear() {
		return end_year;
	}

	public LocalDate getDateObject() {
		return dateObject;
	}

}