package logic.load;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import common.TaskObject;

public class LoadTest {

	@Test
	public void test() {
		TaskObject task = new TaskObject();
		task.setTitle("backup");
		Load load = new Load(task);
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		ArrayList<String> actualOutput = new ArrayList<String> ();
		expectedOutput.add("Loaded file from: backup");
		actualOutput = load.run();
		
		assertEquals(expectedOutput, actualOutput);
	}

}
