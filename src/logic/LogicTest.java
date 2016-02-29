package logic;

import static org.junit.Assert.*;

import org.junit.Test;
import java.util.ArrayList;

public class LogicTest {

	Logic logic = new Logic();	
	ArrayList<String> output = new ArrayList<String>();
	CommandObject commandObj;
	TaskObject taskObj;
	
	/*
	@Test
	public void testParser() {
		String str = "display";
		logic.setUserInput(str);
		logic.callParser();
		
	}*/
	
	
	@Test
	public void test() {
		/*
		String testUserInput = "add floatfloatfloat";
		logic.run(testUserInput);
		printOutput();
		
		String testUserInput2 = "add meowwoofmoo";
		logic.run(testUserInput2);
		printOutput();
		*/
		
		String testUserInput3 = "search";
		logic.run(testUserInput3);
		commandObj = logic.getCommandObject();
		taskObj = logic.getTaskObject();
		
		printTaskObjectFields();
		
		printOutput();
	}
	
	private void printTaskObjectFields() {
		System.out.println("title = " + taskObj.getTitle());
		System.out.println("start date = " + taskObj.getStartDate());
		System.out.println("end date = " + taskObj.getEndDate());
		System.out.println("start time = " + taskObj.getStartTime());
		System.out.println("end time = " + taskObj.getEndTime());
		System.out.println("category = " + taskObj.getCategory());
		System.out.println("status = " + taskObj.getStatus());
		System.out.println("task id = " + taskObj.getTaskId());
		
	}

	public void printOutput() {
		output = logic.getOutput();
		for (int i = 0; i< output.size(); i++) {
			System.out.println(output.get(i));
		}
	}

}
