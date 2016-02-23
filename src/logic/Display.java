import java.util.ArrayList;

public class Display {
	
	private static final String MESSAGE_EMPTY_LIST = "Task list is empty";
	private static final String DISPLAY_RESULT_DEADLINE = "%1$s. %2$s, %3$s, %4$shrs, %5$s";
	private static final String DISPLAY_RESULT_EVENT = "%1$s. %2$s, %3$s-%4$s, %5$shrs-%6$shrs, %7$s";
	private static final String DISPLAY_RESULT_FLOATING = "%1$s. %2$s, %3$s";
	
	private ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();
	private ArrayList<String> output = new ArrayList<String>();

	public Display(ArrayList<TaskObject> taskList) {
		this.taskList = taskList;
	}
	
	ArrayList<String> run() {
		if (taskList.isEmpty()) {
			outputEmptyMessage();
		} else {
			for (int i = 0; i < taskList.size(); i++) {
				TaskObject task = taskList.get(i);
				
				String taskCategory = task.getCategory();
				String taskTitle = task.getTitle();
				int taskStartDate = task.getStartDate();
				int taskEndDate = task.getEndDate();
				int taskStartTime = task.getStartTime();
				int taskEndTime = task.getEndTime();
				String taskStatus = task.getStatus();
				
				switch (taskCategory) {
					case "deadline":
						String taskStartDateInOutputFormat = parseDate(taskStartDate);
						outputDeadlineTask(i, taskTitle, taskStartDateInOutputFormat, taskStartTime, taskStatus);
						break;
					case "event":
						taskStartDateInOutputFormat = parseDate(taskStartDate);
						String taskEndDateInOutputFormat = parseDate(taskEndDate);
						outputEventTask(i, taskTitle, taskStartDateInOutputFormat, taskEndDateInOutputFormat, taskStartTime, taskEndTime, taskStatus);
						break;
					case "floating":
						outputFloatingTask(i, taskTitle, taskStatus);
						break;
					default:
						break;
				}
				
			}
		}
		
		return output;
	}
	
	private void outputEmptyMessage() {
		output.add(MESSAGE_EMPTY_LIST);
	}

	// Returns the date in DD/MM/YYYY format
	private String parseDate(int date) {
		String dateInString = "" + date;
		String day = dateInString.substring(6, 8);
		String month = dateInString.substring(4, 6);
		String year = dateInString.substring(0, 4);
		
		return day + "/" + month + "/" + year;
	}
	
	private void outputDeadlineTask(int num, String taskTitle, String taskStartDate, int taskStartTime, String taskStatus) {
		output.add(String.format(DISPLAY_RESULT_DEADLINE, num, taskTitle, taskStartDate, taskStartTime, taskStatus));
	}
	
	private void outputEventTask(int num, String taskTitle, String taskStartDate, String taskEndDate, 
								 int taskStartTime, int taskEndTime, String taskStatus) {
		output.add(String.format(DISPLAY_RESULT_EVENT, num, taskTitle, taskStartDate, taskEndDate, taskStartTime, taskEndTime, taskStatus));
	}
	
	private void outputFloatingTask(int num, String taskTitle, String taskStatus) {
		output.add(String.format(DISPLAY_RESULT_FLOATING, num, taskTitle, taskStatus));
	}
}
