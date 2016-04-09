//@@author A0125003A
package parser;

import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.exceptions.InvalidDateFormatException;

/**
 * This class focuses on breaking down a string for date into the relevant components
 * it takes in a date input with varying degree of flexibility, 
 * in dd/mm/yy or number-month-year format.
 * 
 * @author sylvesterchin
 * 
 */

public class DateParser {
	private int day = -1;
	private int month = -1;
	private int year = -1;
	private int date = -1;
	
	private String dateString;
	private LocalDate dateObject = LocalDate.MAX;
	
	/**
	 * This method takes in a string input returns LocalDate to the datetimeparser.
	 * 
	 * @param input    
	 * 				date string that is in the format.
	 * @throws Exception 
	 */
	public void parseDate(String input) throws Exception {
		if (!input.isEmpty()) {
			input = preprocess(input);
			processDate(input);
			setDates();	
		}
	}
	
	// ================================
	// First Level of Abstraction
	// ================================
	
	/**
	 * This method returns the input in lower case and redundant white spaces removed.
	 * 
	 * @param input
	 * 				general string, in upper or lower case.
	 * @return
	 */
    private String preprocess(String input) {
        return input.toLowerCase().trim();
    }
	
    /**
	 * This method further processes the input from date-time-parser.
	 * 
	 * format types include
	 * 1. dd-Month-yyyy format:  3rd june 2013 
	 * 2. ddmmyyy format:        4/5-5/6
	 * 3. relative date format:  next monday
	 * 
	 * @param input
	 * 				date input from user, not null.
	 * @throws Exception 
	 */
	private void processDate(String input) throws Exception {
		boolean hasAlphabets = input.matches(".*[a-zA-Z]+.*");
	    if (hasAlphabets) {
	        processWithAlphabets(input);
	    } else {
	        processWithoutAlphabets(input);
	    }
	}
	
	/**
	 * This method sets the LocalDate for the object by cleaning up minor format differences
	 * and forming yyyyMMdd string. It passes date in string format to setDateObject method 
	 * for setting the LocalDate value
	 * @throws Exception 
	 */
	private void setDates() throws Exception {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		
		if (year < 100 && year != -1) {
			year = 2000 + year;
		} else if (year == -1) {
			year = Constants.DEFAULT_YEAR;
		}
		
		if (day > 31 || month > 12) {
			throw new Exception("Invalid Date");
		} else if (day != -1 && month != -1 && year != -1) {
			date = day + month * 100 + year * 10000;
		}
		
		dateString = Integer.toString(date); //now insert the damn dashes
		dateString = addDashes(dateString);
		setDateObject(dateString, dateFormatter);
	}
	
	// ================================
	// Second Level of Abstraction
	// ================================
	
	/**
	 * This method will take in dates without alphabets, such as 
	 * dates in the dd/mm/yyyy format and process it accordingly.
	 * 
	 * @param input
	 * 				date in dd/mm/yyyy format, not null.
	 * @throws Exception
	 */
	private void processWithoutAlphabets(String input) throws Exception {
	    if (hasSymbolSeparator(input)) {
            setSymbolSeparatedDate(input);
        } else {
            throw new InvalidDateFormatException(input);
        }
    }

	/**
	 * This method will identify the date format of the input and process it
	 * accordingly.
	 * 
	 * @param input
	 * 				date string in either relative or ddMonthyyyy format. not null.
	 * @throws Exception
	 */
    private void processWithAlphabets(String input) throws Exception {
	    if (hasMonth(input)) {
            setMonth(input);
            input = removeMonth(input);
            splitStringAndProcess(input);
        } else if (isRelative(input)) { 
            processRelativeDate(input);
        } else {
	        throw new InvalidDateFormatException(input);
	    }
    }
    
    /**
	 * This method adds dashes into the date string to convert it into yyyy-MM-dd format.
	 * 
	 * @param input   
	 * 				date string in yyyyMMdd format.
	 * 				e.g. 20140529
	 * @return 
	 * 				string in yyyy-MM-dd format.
	 * 				e.g. 2014-05-29
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
	private void setDateObject(String input, DateTimeFormatter dateFormatter) {
		if (dateObject == LocalDate.MAX && !input.isEmpty()) {
			dateObject = LocalDate.parse(input, dateFormatter);
		}
	}
		

	// ================================
	// Third Level of Abstraction
	// ================================
    
    //this method checks if the date is in dd/mm/yy format.
  	private boolean hasSymbolSeparator(String input) {
  		if (input.contains("/") || input.contains(".") || input.contains("-") ) {
  			return true;
  		} else {
  			return false;
  		}
  	}
  	
  	/**
	 * This method will set the day, month and year for inputs with in dd/mm/yy format.
	 * 
	 * @param input  
	 * 				input by user for one date, in dd/mm/yyyy format.
	 * @throws Exception 
	 */
	private void setSymbolSeparatedDate(String input) throws Exception {
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		for (String temp : input.split("[.-/]")) {
			temp = temp.replaceAll(" ", "");
			temp = temp.replaceAll("[a-zA-Z]+", "");
			int tempInt = Integer.parseInt(temp);
			list.add(tempInt);
		}
		
		if (list.size() != 1) {
			day = list.get(0);
			month = list.get(1);
			
			if (list.size() == 3) {
				year = list.get(2);
			}	
		} else {
			throw new Exception("Invalid Date");
		}
	}
    
	/**
	 * This method checks if the string contains keywords for any of the months.
	 * 
	 * @param input    
	 * 				string element in array list representing one date. non null.
	 * @return
	 * 				boolean true if input contains any of the 12 months
	 * 
	 */
	private boolean hasMonth(String input) {
		Pattern dateTimePattern = Pattern.compile(Constants.REGEX_MONTHS_TEXT);
		Matcher matcher = dateTimePattern.matcher(input);
		return matcher.find();
	}
	
	//this method will allocate relevant integer to month variable.
	private void setMonth(String input) {
			month = setMonthInDateProcessor(input);
	}
	
	//method removes all the characters in the date.
	private String removeMonth(String input) {
		return input.replaceAll("[a-zA-Z]+", " ");
	}
	
	/**
	 * This method will split a date into the day, month and year.
	 * 
	 * @param input   
	 * 				date input by the user without the month. non null.
	 */
	private void splitStringAndProcess(String input) {
		ArrayList<String> templist = new ArrayList<String>();
		int _day = -1, _year = -1;
		input = preprocess(input);
		
		for (String temp : input.split("\\s+")) {
			temp = temp.replaceAll("[a-zA-Z]+", "");
			templist.add(temp);
		}
		
		//set the day n year according to the filled elements of the array.
		if (templist.size() == 2) {
			_day = Integer.parseInt(templist.get(0));
			_year = Integer.parseInt(templist.get(1));
		} else if (templist.size() == 1) {
			_day = Integer.parseInt(templist.get(0));
		}
		//set attributes.
		day = _day;
		year = _year;
	}
	
    /**
	 * This method checks if the input string is a relative date.
	 * 
	 * @param input   
	 * 				date string, not null.
	 * @return boolean   
	 * 				true,if the date string is a relative date.
	 */
	private boolean isRelative(String input) {
		if (input.matches(Constants.REGEX_RELATIVE_DATE_ALL) || input.matches(Constants.REGEX_DAYS_TEXT)
				|| input.matches(Constants.REGEX_RECURRING_INTERVAL_EVERYDAY)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * This method will set date object for relative date inputs such as today, tmr, next week.
	 * 
	 * @param input
	 * 				non-null string that is a relative date, such as "today" or "next fri".
	 * @throws Exception 
	 */
	private void processRelativeDate(String input) throws Exception {
		input = preprocess(input);
		if (input.matches(Constants.REGEX_RELATIVE_DATE_0) 
				|| input.matches(Constants.REGEX_RECURRING_INTERVAL_EVERYDAY)) {
				dateObject = LocalDate.now();
		} else if (input.matches(Constants.REGEX_RELATIVE_DATE_1)) {
				dateObject = LocalDate.now().plusDays(1);
		} else if (input.matches("("+"(next )"+ Constants.REGEX_DAYS_TEXT+")")) {
			setDateNextWeek(input);
		} else if (input.matches("("+"(this )"+ Constants.REGEX_DAYS_TEXT+")")) {
			setDateThisWeek(input);
		} else if (input.matches("next " + "(week|wk)(s)?")) {
			dateObject = LocalDate.now().plusWeeks(1);
		} else if (input.matches(Constants.REGEX_DAYS_TEXT)) {
			setDateToComingDayOfWeek(input);
		}
	}
	
	// ================================
	// Fourth Level of Abstraction
	// ================================

	/**
	 * This method will return the corresponding month's integer value.
	 * 
	 * @param month
	 * 				month input in string format, non-null.
	 */
	private int setMonthInDateProcessor(String month) {
		month = month.toLowerCase();
		if (month.contains(Constants.MONTH_1_1) || month.contains(Constants.MONTH_1_2)) {
			return Constants.VALUE_JAN;
		} else if (month.contains(Constants.MONTH_2_1) || month.contains(Constants.MONTH_2_2)) {
			return Constants.VALUE_FEB;
		} else if (month.contains(Constants.MONTH_3_1) || month.contains(Constants.MONTH_3_2)) {
			return Constants.VALUE_MAR;
		} else if (month.contains(Constants.MONTH_4_1) || month.contains(Constants.MONTH_4_2)) {
			return Constants.VALUE_APR;
		} else if (month.contains(Constants.MONTH_5_1)) {
			return Constants.VALUE_MAY;
		} else if (month.contains(Constants.MONTH_6_1) || month.contains(Constants.MONTH_6_2)) {
			return Constants.VALUE_JUN;
		} else if (month.contains(Constants.MONTH_7_1) || month.contains(Constants.MONTH_7_2)) {
			return Constants.VALUE_JUL;
		} else if (month.contains(Constants.MONTH_8_1) || month.contains(Constants.MONTH_8_2)) {
			return Constants.VALUE_AUG;
		} else if (month.contains(Constants.MONTH_9_1) || month.contains(Constants.MONTH_9_2)) {
			return Constants.VALUE_SEPT;
		} else if (month.contains(Constants.MONTH_10_1) || month.contains(Constants.MONTH_10_2)) {
			return Constants.VALUE_OCT;
		} else if (month.contains(Constants.MONTH_11_1) || month.contains(Constants.MONTH_11_2)) {
			return Constants.VALUE_NOV;
		} else if (month.contains(Constants.MONTH_12_1) || month.contains(Constants.MONTH_12_2)) {
			return Constants.VALUE_DEC;
		} else {
			return -1;
		}
	}
	
	/**
	 * This method will set the date object to the next nearest day that 
	 * the input specifies.
	 * 
	 * @param input
	 * 				non-null string which is a day from Monday to Sunday.
	 * @throws InvalidDateFormatException
	 */
	private void setDateToComingDayOfWeek(String input) throws InvalidDateFormatException {
	    input = processDayOfWeek(input);
	    dateObject = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.valueOf(input))); 
	}
	
	/**
	 * This method will set the date object to the day as specified by the input
	 * on the following week.
	 * 
	 * @param input
	 * 				non-null string which contains a day from Monday to Sunday.
	 * @throws InvalidDateFormatException
	 */
	private void setDateNextWeek(String input) throws InvalidDateFormatException {
		input = input.replaceAll("next", "").trim();
        input = processDayOfWeek(input);
        dateObject = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.valueOf(input)));
        int set = dateObject.getDayOfWeek().getValue();
        int now = LocalDate.now().getDayOfWeek().getValue();
        if (set > now ) {
            dateObject = dateObject.plusWeeks(1);
        }
	}
	
	/**
	 * This method will set the date object to the day as specified by the input
	 * in this week.
	 * 
	 * @param input
	 * 				non-null string which contains a day from Monday to Sunday.
	 * @throws Exception
	 */
	private void setDateThisWeek(String input) throws Exception {
		input = input.replaceAll("this", "").trim();
		input = processDayOfWeek(input);
		int set = DayOfWeek.valueOf(input).getValue();
        int now = LocalDate.now().getDayOfWeek().getValue();
        if (set < now ) {
        	dateObject = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.valueOf(input)));
            //throw new Exception(input + " is over this week. Did you mean next " + input + "?");
        } else if (set > now) {
            dateObject = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.valueOf(input)));
        }
	}
	
	@SuppressWarnings("unused")
    private LocalDate setStartofNextWeek() {
		LocalDate date = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
		return date;
	}
	
	// ================================
	// Fifth Level of Abstraction
	// ================================
	
	/**
	 * This method will read the day of week and return the day-of-week in
	 * correct format.
	 * 
	 * @param dayOfWeek
	 * 				string input representing the day of the week. E.g. monday.
	 * @return
	 * 				day-of-the-week in upper case, similar to LocalDate.getDayOfWeek output.
	 * @throws InvalidDateFormatException
	 */
	private String processDayOfWeek(String dayOfWeek) throws InvalidDateFormatException {
        dayOfWeek = preprocess(dayOfWeek);
        if (Constants.DAY_1.contains(dayOfWeek)) {
        	dayOfWeek = Constants.DAY_1;
        } else if (Constants.DAY_2.contains(dayOfWeek)) {
        	dayOfWeek =  Constants.DAY_2;
        } else if (Constants.DAY_3.contains(dayOfWeek)) {
        	dayOfWeek =  Constants.DAY_3;
        } else if (Constants.DAY_4.contains(dayOfWeek)) {
        	dayOfWeek =  Constants.DAY_4;
        } else if (Constants.DAY_5.contains(dayOfWeek)) {
        	dayOfWeek =  Constants.DAY_5;
        } else if (Constants.DAY_6.contains(dayOfWeek)) {
        	dayOfWeek =  Constants.DAY_6;
        } else if (Constants.DAY_7.contains(dayOfWeek)) {
        	dayOfWeek =  Constants.DAY_7;
        } else {
            throw new InvalidDateFormatException(dayOfWeek);
        }
        return dayOfWeek.toUpperCase();
    }
	
	/**
	 * Getter method for testing purposes
	 * 
	 * @return LocalDate object
	 */
	public LocalDate getDateObject() {
		return dateObject;
	}
	
	//method used to reset all dates for testing
	public void resetDate() {
		day = -1;
		month = -1;
		year = -1;
		
		date = -1;
		dateObject = LocalDate.MAX;
	}
}