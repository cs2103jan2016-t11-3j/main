package parser;

/*REGEX GUIDE (regular expression)
 * (?i) means regex is case insensitive
 * | refers to OR
 * $ refers to matching the end of the line
 * ? refers to 0 or 1 occurrence of preceding expression
 * (re) groups regular expressions and remembers matched text
 * \\d refers to digits 0-9
 * 
 * This class contains all the constant regular expressions that the parser will use
 * 
 * 
 */

public class Constants {
	public static final String REGEX_WS = "\\s";
	
	//commands
	public static final String REGEX_ADD = "(?i)^(add)";
    public static final String REGEX_EDIT = "(?i)^(edit|update)" + " " + "[\\d]";// edited to have number after edit
    public static final String REGEX_DELETE = "(?i)^(delete)";
    public static final String REGEX_UNDO = "(?i)^(undo)";
    public static final String REGEX_REDO = "(?i)^(redo)";
    public static final String REGEX_HELP = "(?i)^(help)";
    public static final String REGEX_SAVE = "(?i)^(save)";
    public static final String REGEX_EXIT = "(?i)^(exit|quit)";
    public static final String REGEX_DONE = "(?i)^(done|(complete)(d)|(finish)(ed))";
    
    //index for corresponding commands
	public static final int ADD_INDEX = 1;
	public static final int SEARCH_INDEX = 2;
	public static final int EDIT_INDEX = 3;
	public static final int DELETE_INDEX = 4;
	public static final int UNDO_INDEX = 5;
	public static final int REDO_INDEX = 6;
	public static final int SAVE_INDEX = 7;
	public static final int EXIT_INDEX = 8;
	public static final int HELP_INDEX = 9;	
	public static final int DONE_INDEX = 10;
	
	public enum TaskType {
        floating, deadline, event, repeated;
    }
	
	//regular expressions for date
	public static final String REGEX_DAY_NUMBER = "((?i)0?[1-9]|[12][\\d]|3[01])(st|th|nd|rd)?";
	public static final String REGEX_DAY_ONLYNUMBER = "((?i)0?[1-9]|[12][\\d]|3[01])";
	public static final String REGEX_DAYS_TEXT = "((?i)(mon)(day)?|"
            + "(tue)(sday)?|" + "(wed)(nesday)?|" + "(thu)(rsday)?|" //edit this shit
            + "(fri)(day)?|" + "(sat)(urday)?|" + "(sun)(day)?)";
	public static final String REGEX_MONTHS_NUMBER = "(0?[1-9]|1[0-2])";
	public static final String REGEX_MONTHS_TEXT = "((?i)(jan)(uary)?|"
            + "(feb)(ruary)?|" + "(mar)(ch)?|" + "(apr)(il)?|" + "(may)|"
            + "(jun)(e)?|" + "(jul)(y)?|" + "(aug)(ust)?|" + "(sep)(t)(ember)?|"
            + "(oct)(ober)?|" + "(nov)(ember)?|" + "(dec)(ember)?)";
	public static final String REGEX_YEAR = "((19|20)?\\d\\d)";
    public static final String REGEX_DATE_ATTRIBUTES = "((?i)(day)(s)?|"
            + "(week|wk)(s)?|" + "(month|mth)(s)?|" + "(year|yr))(s)?";

    //regular expression for time
	public static final String REGEX_TIME_HHMM = "(?i)[0-2][0-9][-:]?[0-5][\\d]([h]([r][s]?))";
    public static final String REGEX_TIME_AMPM = "((?i)0?[1-9]|1[0-2])[.-:]?([0-5][0-9])?[ -:]?([a|p][m])"; 
    //has to have am or pm, dont care!
    //removed space btw HH and MM . added fullstop btw hh n mm
    public static final String REGEX_TIME_ATTRIBUTES = "((?i)[\\d]" + "(sec|second)(s)?|" 
    		+ "(min|minutes)(s)?|" + "(hour|hr)(s)?)";
    
    //possible arrangements for date input
	public static final String REGEX_COMBINED_DATE_DDMMYYYY = REGEX_DAY_ONLYNUMBER
            + "[-./]" + REGEX_MONTHS_NUMBER + "[-./]?" + REGEX_YEAR + "?";
    public static final String REGEX_COMBINED_DATE_DDMONTHYYYY = REGEX_DAY_NUMBER
            + "[-./ ]?" + REGEX_MONTHS_TEXT + "[-./ ]?" + REGEX_YEAR + "?";
	
    //possible formats for time input and date input
    public static final String REGEX_TIME_FORMAT = "(" + REGEX_TIME_HHMM
            + "|" + REGEX_TIME_AMPM + ")";
    public static final String REGEX_DATE_FORMAT = "("
            + REGEX_COMBINED_DATE_DDMMYYYY + "|"
            + REGEX_COMBINED_DATE_DDMONTHYYYY + ")";
    
  
    public static final String REGEX_DATETIME_FORMAT = "(("
            + REGEX_DATE_FORMAT + "(,?[ ]" + REGEX_TIME_FORMAT + ")?)|("
            + REGEX_TIME_FORMAT + "(,?[ ]" + REGEX_DATE_FORMAT + ")?))";
  
    
    //deadline has the by or before keyword
    public static final String REGEX_DEADLINE_IDENTIFIER = "(?i)(by|before) "
            + REGEX_DATETIME_FORMAT;
    
    //event has a start and end 
    public static final String REGEX_EVENT_IDENTIFIER = "(?i)(from) "
            + REGEX_DATETIME_FORMAT + " to " +REGEX_DATETIME_FORMAT;
    
    //point task has an at or on keyword
    public static final String REGEX_POINT_TASK_IDENTIFIER = "(?i)(on|at) "
            + REGEX_DATETIME_FORMAT;
    
    //recurring task has an every keyword
    public static final String REGEX_RECURRING_TASK_IDENTIFIER = "(?i)(every) "
            + REGEX_DAYS_TEXT + "|" + REGEX_TIME_ATTRIBUTES + "|" 
    		+ REGEX_TIME_ATTRIBUTES + "(?i)( to " + REGEX_DAYS_TEXT
            + ")?";
    
    public static final String REGEX_TASK_IDENTIFIER = "(?i)(by|before|every|on|at|from|to)";
    
    //if any of the 4 types matches, the input by user will need to read date time
    public static final String REGEX_DATE_TIME_IDENTIFIER = "("
            + REGEX_DEADLINE_IDENTIFIER + "|" + REGEX_EVENT_IDENTIFIER + "|"
            + REGEX_POINT_TASK_IDENTIFIER + "|"
            + REGEX_RECURRING_TASK_IDENTIFIER + ")$"; //checks end of string
    
    //For edit processor
    public static final String REGEX_EDIT_DATE_TIME_IDENTIFIER = "(" 
			+ REGEX_DATE_TIME_IDENTIFIER + "|" + REGEX_DATETIME_FORMAT +")$";
    
    
    public static final String REGEX_RELATIVE_DATE_1 = "(?i)(tmr|tomorrow)";
    public static final String REGEX_RELATIVE_DATE_2 = "(?i)((next) ("
            + REGEX_DATE_ATTRIBUTES + "|" + REGEX_DAYS_TEXT + "))";
    public static final String REGEX_RELATIVE_DATE_3 = "(\\d+ "
            + REGEX_DATE_ATTRIBUTES + " (?i)(later|before|after|from now))";
    public static final String REGEX_RELATIVE_DATE_ALL = "(" + REGEX_RELATIVE_DATE_1 
    		+ "|" + REGEX_RELATIVE_DATE_2 + "|" + REGEX_RELATIVE_DATE_3 + ")$";
    
    public static final String REGEX_RELATIVE_TIME_1 = "("
            + REGEX_TIME_ATTRIBUTES + " (?i)(later|before|after|from now))";
    
    public static final String REGEX_FINAL = "(" + REGEX_RELATIVE_DATE_ALL 
    		+ "|" + REGEX_DATE_TIME_IDENTIFIER + "|" + REGEX_RELATIVE_TIME_1 + ")";
    
    public static final String REGEX_SEARCH = "(" + REGEX_FINAL + "|"
    		+ REGEX_DATETIME_FORMAT + ")$";
    
}
