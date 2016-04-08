package logic.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import common.TaskObject;

public class Sort {

	private ArrayList<TaskObject> taskList;
	
	public Sort(ArrayList<TaskObject> taskList) {
		this.taskList = taskList;
	}
	
	public ArrayList<TaskObject> run() {
		Comparator<TaskObject> dateComparator = new Comparator<TaskObject>() {
			@Override
			public int compare(final TaskObject o1, final TaskObject o2) {
				if (o1.getStatus().equals(o2.getStatus())) {
					if (o1.getStartDateTime().equals(o2.getStartDateTime())) {
						if (o1.getEndDateTime().equals(o2.getEndDateTime())) {
							return o1.getTitle().compareTo(o2.getTitle());
						}
						return o1.getEndDateTime().compareTo(o2.getEndDateTime());
					}
					return o1.getStartDateTime().compareTo(o2.getStartDateTime());
				}
				return o2.getStatus().compareTo(o1.getStatus());
			}
		};
		Collections.sort(taskList, dateComparator);
		
		return taskList;

	}
}
