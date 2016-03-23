package parser;

import java.time.LocalDateTime;

import common.TaskObject;

public abstract class CommandParser {
	
	protected static final String TIME_AM_1 = "AM";
	protected static final String TIME_AM_2 = "A.M.";
	protected static final String TIME_AM_3 = "am";
	protected static final String TIME_AM_4 = "a.m.";
	protected static final String TIME_PM_1 = "PM";
	protected static final String TIME_PM_2 = "P.M.";
	protected static final String TIME_PM_3 = "pm";
	protected static final String TIME_PM_4 = "p.m.";
	
	protected static final String MONTH_1_1 = "january";
	protected static final String MONTH_1_2 = "jan";
	protected static final String MONTH_2_1 = "february";
	protected static final String MONTH_2_2 = "feb";
	protected static final String MONTH_3_1 = "march";
	protected static final String MONTH_3_2 = "mar";
	protected static final String MONTH_4_1 = "april";
	protected static final String MONTH_4_2 = "apr";
	protected static final String MONTH_5_1 = "may";
	protected static final String MONTH_6_1 = "june";
	protected static final String MONTH_6_2 = "jun";
	protected static final String MONTH_7_1 = "july";
	protected static final String MONTH_7_2 = "jul";
	protected static final String MONTH_8_1 = "august";
	protected static final String MONTH_8_2 = "aug";
	protected static final String MONTH_9_1 = "september";
	protected static final String MONTH_9_2 = "sept";
	protected static final String MONTH_10_1 = "october";
	protected static final String MONTH_10_2 = "oct";
	protected static final String MONTH_11_1 = "november";
	protected static final String MONTH_11_2 = "nov";
	protected static final String MONTH_12_1 = "december";
	protected static final String MONTH_12_2 = "dec";
	
	protected static final int VALUE_JAN = 1;
	protected static final int VALUE_FEB = 2;
	protected static final int VALUE_MAR = 3;
	protected static final int VALUE_APR = 4;
	protected static final int VALUE_MAY = 5;
	protected static final int VALUE_JUN = 6;
	protected static final int VALUE_JUL = 7;
	protected static final int VALUE_AUG = 8;
	protected static final int VALUE_SEPT = 9;
	protected static final int VALUE_OCT = 10;
	protected static final int VALUE_NOV = 11;
	protected static final int VALUE_DEC = 12;
	
	protected static final String KEYWORD_AT = "at";
	protected static final String KEYWORD_BY = "by";
	protected static final String KEYWORD_ON = "on";
	
	protected static final Integer NOT_TASK_CONSTANT = 5;
	
	protected String _task = "";
	protected int _startDate = -1;
	protected int _endDate = -1;
	protected int _startTime = -1;
	protected int _endTime = -1;
	
	protected LocalDateTime _startDateTime = LocalDateTime.MAX;
	protected LocalDateTime _endDateTime = LocalDateTime.MAX;
	
	public abstract TaskObject process(String input) throws Exception;
	
	public abstract void reset();
	
	public abstract int getIndex(); //only for EDIT
	
	protected String getTrimmedString(String input, int startIndex, int endIndex) {
		return input.substring(startIndex, endIndex).trim();
	}
	
}
