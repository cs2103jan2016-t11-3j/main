package logic.constants;

public class Strings {
	//common Constants
	public static final String CATEGORY_EVENT = "event";
	public static final String CATEGORY_DEADLINE = "deadline";

	// CommandFacade Constants
	public static final String CATEGORY_FLOATING = "floating";
	public static final String MESSAGE_INVALID_COMMAND = "Invalid command";
	
	// Recurring Constants
	public final static String FREQ_HOURLY = "HOURLY";
	public final static String FREQ_DAILY = "DAILY";
	public final static String FREQ_WEEKLY = "WEEKLY";
	public final static String FREQ_MONTHLY = "MONTHLY";
	public final static String FREQ_YEARLY = "YEARLY";
	public static final String MESSAGE_INVALID_RECURRENCE = "No valid end of recurrence";
	
	//Add Constants
	public static final String MESSAGE_ADD = "Task added: ";
	private final String MESSAGE_FAIL = "Failed to add task. ";
	private final String MESSAGE_CLASH = "Task: %1s clashes with %2s";
	private final String MESSAGE_INVALID_TIME = "Reason: Invalid time input.";
	private final String MESSAGE_NULL_POINTER = "Reason: No object available to access.";

}

