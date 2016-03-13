package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import logic.Logic;

public class LogicTest {
    
    private final String MESSAGE_ADD = "Task added: ";

    @Test
    public void testAddTasks() {
        ArrayList<String> commands = new ArrayList<String>();
        ArrayList<String> display = new ArrayList<String>();
        ArrayList<String> output = new ArrayList<String>();
        Logic logic = new Logic();
        for (String command : commands) {
            display = excecute(logic, command);
            output.clear();
            output.add(MESSAGE_ADD + "task");
            AssertHelper.assertArrayListEquals( command , display, output);
        }
    }

    
    public static ArrayList<String> excecute(Logic logic, String command) {
        ArrayList<String> display = new ArrayList<String>();
        logic.run(command);
        display = logic.getOutput();
        return display;
    }
    
}
