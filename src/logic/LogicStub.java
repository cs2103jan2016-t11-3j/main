package logic;

import common.*;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

import java.util.ArrayList;

// for testing purposes
public class LogicStub extends Logic{

	public LogicStub() {
		
	}
	
	public LogicStub(ArrayList<TaskObject> taskList, ArrayList<String> alertOutput) {
		this.taskList = taskList;
		this.alertOutput = alertOutput;
	}
}
