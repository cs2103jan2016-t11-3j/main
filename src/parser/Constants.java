//@@author A0125003A
package parser;

/**
 * This class contains all the constant regular expressions that the parser will use.
 * Regular expression allows for flexibility for the use when entering expressions and
 * reduce the amount of if-else in the command parser.  
 * 
 * @author sylvesterchin
 *
 */
public class Constants {
	public static final String REGEX_WS = "\\s";
	
	/*--------------------Command Keywords-------------------------------------------------------*/
	public static final String REGEX_PARSER_ADD = "(?i)^(add)";
	public static final String REGEX_PARSER_SEARCH = "(?i)^(view|search|find|filter|display)";
    public static final String REGEX_PARSER_EDIT = "(?i)^(edit|update)";
    public static final String REGEX_PARSER_DELETE = "(?i)^(delete)";
    public static final String REGEX_PARSER_UNDO = "(?i)^(undo)";
    public static final String REGEX_PARSER_REDO = "(?i)^(redo)";
    public static final String REGEX_PARSER_HELP = "(?i)^(help)";
    public static final String REGEX_PARSER_SAVE = "(?i)^(save)";
    public static final String REGEX_PARSER_EXIT = "(?i)^(exit|quit)";
    public static final String REGEX_PARSER_DONE = "(?i)^(done|(complete)(d)?|(finish)(ed)?)";
    public static final String REGEX_PARSER_NOTDONE = "(?i)^(undone|incomplete)";
    public static final String REGEX_PARSER_LOAD = "(?i)^(load)";
    
    /*--------------------Command Index----------------------------------------------------------*/
	public static final int INDEX_ADD = 1;
	public static final int INDEX_SEARCH = 2;
	public static final int INDEX_EDIT = 3;
	public static final int INDEX_DELETE = 4;
	public static final int INDEX_UNDO = 5;
	public static final int INDEX_REDO = 6;
	public static final int INDEX_SAVE = 7;
	public static final int INDEX_EXIT = 8;
	public static final int INDEX_HELP = 9;	
	public static final int INDEX_DONE = 10;
	public static final int INDEX_NOTDONE = 11;
	public static final int INDEX_LOAD = 12;
	
	/*--------------------Month Value------------------------------------------------------------*/
	public static final int VALUE_JAN = 1;
	public static final int VALUE_FEB = 2;
	public static final int VALUE_MAR = 3;
	public static final int VALUE_APR = 4;
	public static final int VALUE_MAY = 5;
	public static final int VALUE_JUN = 6;
	public static final int VALUE_JUL = 7;
	public static final int VALUE_AUG = 8;
	public static final int VALUE_SEPT = 9;
	public static final int VALUE_OCT = 10;
	public static final int VALUE_NOV = 11;
	public static final int VALUE_DEC = 12;
	
	/*--------------------Month String ----------------------------------------------------------*/
	public static final String MONTH_1_1 = "january";
	public static final String MONTH_1_2 = "jan";
	public static final String MONTH_2_1 = "february";
	public static final String MONTH_2_2 = "feb";
	public static final String MONTH_3_1 = "march";
	public static final String MONTH_3_2 = "mar";
	public static final String MONTH_4_1 = "april";
	public static final String MONTH_4_2 = "apr";
	public static final String MONTH_5_1 = "may";
	public static final String MONTH_6_1 = "june";
	public static final String MONTH_6_2 = "jun";
	public static final String MONTH_7_1 = "july";
	public static final String MONTH_7_2 = "jul";
	public static final String MONTH_8_1 = "august";
	public static final String MONTH_8_2 = "aug";
	public static final String MONTH_9_1 = "september";
	public static final String MONTH_9_2 = "sept";
	public static final String MONTH_10_1 = "october";
	public static final String MONTH_10_2 = "oct";
	public static final String MONTH_11_1 = "november";
	public static final String MONTH_11_2 = "nov";
	public static final String MONTH_12_1 = "december";
	public static final String MONTH_12_2 = "dec";
	
	public static final String DAY_1 = "monday";
	public static final String DAY_2 = "tuesday";
	public static final String DAY_3 = "wednesday";
	public static final String DAY_4 = "thursday";
	public static final String DAY_5 = "friday";
	public static final String DAY_6 = "saturday";
	public static final String DAY_7 = "sunday";
	
	public static final String FREQ_DAILY = "DAILY";
	public static final String FREQ_WEEKLY = "WEEKLY";
	public static final String FREQ_MONTHLY = "MONTHLY";
	public static final String FREQ_YEARLY = "YEARLY";
	
	
	public enum TaskType {
        floating, deadline, event, recurring;
    }
	
	/*---------------------Regular Expressions for Dates-----------------------------------------*/
	public static final String REGEX_DAY_NUMBER = "((?i)0?[1-9]|[12][\\d]|3[01])(st|th|nd|rd)?";
	public static final String REGEX_DAY_ONLYNUMBER = "((?i)0?[1-9]|[12][\\d]|3[01])";
	public static final String REGEX_DAYS_TEXT = "((?i)(mon)(day)?|"
            + "(tue)(s)?(day)?|" + "(wed)(nesday)?|" + "(thu)(r)?(s)?(day)?|" //edit this shit
            + "(fri)(day)?|" + "(sat)(urday)?|" + "(sun)(day)?)";
	public static final String REGEX_MONTHS_NUMBER = "(0?[1-9]|1[0-2])";
	public static final String REGEX_MONTHS_TEXT = "((?i)(jan)(uary)?|"
            + "(feb)(ruary)?|" + "(mar)(ch)?|" + "(apr)(il)?|" + "(may)|"
            + "(jun)(e)?|" + "(jul)(y)?|" + "(aug)(ust)?|" + "(sep)(t)(ember)?|"
            + "(oct)(ober)?|" + "(nov)(ember)?|" + "(dec)(ember)?)";
	public static final String REGEX_YEAR = "((19|20)?\\d\\d)";
    public static final String REGEX_DATE_ATTRIBUTES = "(?i)((day)(s)?|"
            + "(week|wk)(s)?|" + "(month|mth)(s)?|" + "(year|yr))(s)?";

    /*--------------------Regular Expressions for Time-------------------------------------------*/
	public static final String REGEX_TIME_HHMM = "(?i)[0-2][0-9][.-:]?[0-5][\\d]([h]([r][s]?))";
    public static final String REGEX_TIME_AMPM = "((?i)0?[1-9]|1[0-2])[.-:]?([0-5][0-9])?[ -:]?([a|p][m])";
    	//has to have am or pm, dont care!
    	//removed space btw HH and MM . added fullstop btw hh n mm
    public static final String REGEX_TIME_ATTRIBUTES = "(?i)(sec|second)(s)?|" 
    		+ "(min|minutes)(s)?|" + "(hour|hr)(s)?";
    
    /*--------------------Regular Expressions for Date Formats-----------------------------------*/
	public static final String REGEX_COMBINED_DATE_DDMMYYYY = REGEX_DAY_ONLYNUMBER
            + "[-./]" + REGEX_MONTHS_NUMBER + "[-./]?" + REGEX_YEAR + "?";
    public static final String REGEX_COMBINED_DATE_DDMONTHYYYY = REGEX_DAY_NUMBER
            + "[-./ ]?" + REGEX_MONTHS_TEXT + "[-./ ]?" + REGEX_YEAR + "?";
	
    /*--------------------Regular Expressions for Dates & Time formats---------------------------*/
    public static final String REGEX_TIME_FORMAT = "(" + REGEX_TIME_HHMM
            + "|" + REGEX_TIME_AMPM + ")";
    public static final String REGEX_DATE_FORMAT = "("
            + REGEX_COMBINED_DATE_DDMMYYYY + "|"
            + REGEX_COMBINED_DATE_DDMONTHYYYY + ")";
    public static final String REGEX_DATETIME_FORMAT = "(("
            + REGEX_DATE_FORMAT + "(,?[ ]" + REGEX_TIME_FORMAT + ")?)|("
            + REGEX_TIME_FORMAT + "(,?[ ]" + REGEX_DATE_FORMAT + ")?))";
    
    /*--------------------Regular Expressions for Relative Dates---------------------------------*/
    public static final String REGEX_RELATIVE_DATE_0 = "(?i)(today|tonight)";
    public static final String REGEX_RELATIVE_DATE_1 = "(?i)(tmr|tomorrow)";
    public static final String REGEX_RELATIVE_DATE_2 = "(?i)((next|this) ("
            + REGEX_DATE_ATTRIBUTES + "|" + REGEX_DAYS_TEXT + "))";
    public static final String REGEX_RELATIVE_DATE_3 = "(\\d+ "
            + REGEX_DATE_ATTRIBUTES + " (?i)(later|before|after|from now))";
    public static final String REGEX_RELATIVE_DATE_ALL = "(" + REGEX_RELATIVE_DATE_0 + "|"
            + REGEX_RELATIVE_DATE_1 + "|" + REGEX_RELATIVE_DATE_2 
            + "|" + REGEX_RELATIVE_DATE_3 +")";
    public static final String REGEX_RELATIVE_DATETIME = "(" + "(" + REGEX_TIME_FORMAT + " )?" 
            + REGEX_RELATIVE_DATE_ALL + "( " + REGEX_TIME_FORMAT + ")?" + ")";
    
    public static final String REGEX_RELATIVE_TIME_1 = "("
            + REGEX_TIME_ATTRIBUTES + " (?i)(later|before|after|from now))";
    
    /*--------------------Deadline Expression----------------------------------------------------*/
    public static final String REGEX_DEADLINE_IDENTIFIER = "(?i)(by|before) " + "("
            + REGEX_DATETIME_FORMAT + "|" + REGEX_RELATIVE_DATETIME + ")";
    public static final String REGEX_POINT_TASK_IDENTIFIER = "(?i)(on|at) " + "("
            + REGEX_DATETIME_FORMAT + "|" + REGEX_RELATIVE_DATETIME + ")";
    
    /*--------------------Event Expression-------------------------------------------------------*/
    public static final String REGEX_EVENT_COMPONENT = "(" + REGEX_DATETIME_FORMAT + "|" 
    		+ REGEX_RELATIVE_DATETIME + ")";
    public static final String REGEX_EVENT_IDENTIFIER_1 = "(?i)(from) " +  REGEX_EVENT_COMPONENT 
    			+ " to " + REGEX_EVENT_COMPONENT;
    public static final String REGEX_EVENT_IDENTIFIER = "(?i)(from) "
    + "(" + REGEX_DATETIME_FORMAT + "|" +  REGEX_RELATIVE_DATETIME + ")"
    + " to " + "(" + REGEX_DATETIME_FORMAT + "|" +  REGEX_RELATIVE_DATETIME + ")";

    
    /*--------------------Recurring Task Expression----------------------------------------------*/
    public static final String REGEX_RECURRING_INTERVAL_EVERYDAY = "(?i)(everyday)";
    public static final String REGEX_RECURRING_INTERVAL = "(?i)(every)[ 0-9]* ("
    		+ REGEX_DATE_ATTRIBUTES + "|" + REGEX_TIME_ATTRIBUTES + "|" + REGEX_DAYS_TEXT 
    		+ "(" + "( and | & | n )?(, )?( , )?(,)?" + REGEX_DAYS_TEXT + ")*" + ")";
    public static final String REGEX_RECURRING_INTERVAL2 = "(" + REGEX_RECURRING_INTERVAL + "|" 
    		+ REGEX_RECURRING_INTERVAL_EVERYDAY + ")";
    public static final String REGEX_RECURRING_START = "(" + REGEX_DATETIME_FORMAT + "|"
    		+ REGEX_DEADLINE_IDENTIFIER + "|" + REGEX_EVENT_IDENTIFIER + "|" 
    		+ REGEX_POINT_TASK_IDENTIFIER + ")?";
    public static final String REGEX_RECURRING_FOR = "(?i)(for) " + "[\\d]+ " 
    		+ "(" + REGEX_DATE_ATTRIBUTES + "|" + "(time|times))";
    public static final String REGEX_RECURRING_UNTIL = "(?i)(until) (" 
    		+ REGEX_DATETIME_FORMAT + "|" + REGEX_RELATIVE_DATE_ALL + ")";
    public static final String REGEX_RECURRING_TASK_IDENTIFIER = "(" + REGEX_RECURRING_INTERVAL 
    		+ "|" + REGEX_RECURRING_INTERVAL_EVERYDAY + ")" + "( " + REGEX_RECURRING_START + ")?" 
    		+ "( " + REGEX_RECURRING_UNTIL + "| " + REGEX_RECURRING_FOR + ")?";
    
    /*--------------------Task Identifier Expression---------------------------------------------*/
    public static final String REGEX_TASK_IDENTIFIER = "(?i)(by|before|every|on|at|from|to)";
    public static final String REGEX_TASK_IDENTIFIER_2 = "(?i)(by|before|every|on|at|from)";//today tomorrow
    public static final String REGEX_TASK_IDENTIFIER_3 = "(?i)(by|before|every|on|from|to)";//saturday
    public static final String REGEX_TASK_IDENTIFIER_4 = "(?i)(by|before|every|from|to)";//monday
    public static final String REGEX_TASK_IDENTIFIER_5 = "(?i)(by|before|on|at|from|to)";
    public static final String REGEX_TASK_IDENTIFIER_6 = "(?i)(by|before|every|at|from)";//tonight 
    
    /*--------------------DateTime Identifier Expression-----------------------------------------*/
    public static final String REGEX_DATE_TIME_IDENTIFIER = "("
            + REGEX_DEADLINE_IDENTIFIER + "|" + REGEX_EVENT_IDENTIFIER + "|"
            + REGEX_POINT_TASK_IDENTIFIER + "|"
            + REGEX_RECURRING_TASK_IDENTIFIER + ")$"; //checks end of string
    
    /*--------------------EDIT DateTime Identifier Expression------------------------------------*/
    public static final String REGEX_EDIT_DATE_TIME_IDENTIFIER = "(" 
			+ REGEX_DATE_TIME_IDENTIFIER + "|" + REGEX_DATETIME_FORMAT +")$";
    
    /*--------------------Overall Identifier Expression------------------------------------*/
    public static final String REGEX_FINAL = "(" + REGEX_DATE_TIME_IDENTIFIER + "|" 
    		+ REGEX_RELATIVE_DATE_ALL + "|" + REGEX_RELATIVE_TIME_1 + ")$";
    
    /*--------------------SEARCH DateTime Identifier Expression----------------------------------*/
    public static final String REGEX_EDIT_STARTEND = "(?i)(start|end)";
    public static final String REGEX_SEARCH = "(" + REGEX_FINAL + "|"
    		+ REGEX_DATETIME_FORMAT + "|" + REGEX_DAYS_TEXT + ")";
    public static final String REGEX_SEARCH2 = "(" + REGEX_RECURRING_INTERVAL + "|"+ REGEX_FINAL + "|"
    		+ "(" + REGEX_DATETIME_FORMAT  + "( " + REGEX_EDIT_STARTEND + ")?)"+ ")$";

	//changeable default year.
	public static final int DEFAULT_YEAR = 2016;	
}
