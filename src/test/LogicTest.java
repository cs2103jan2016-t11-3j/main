package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import logic.Logic;

public class LogicTest {

    @Test
    public void testAddTasks() {
        ArrayList<String> commands = new ArrayList<String>();
        ArrayList<String> display = new ArrayList<String>();
        commands.add("add task1");
        commands.add("add task2");
        Logic logic = new Logic();
        for (String command : commands) {
            display = excecute(logic, command);
            AssertHelper.assertArrayListEquals( command , commands, display);
        }
        
    }

    
    public static ArrayList<String> excecute(Logic logic, String command) {
        ArrayList<String> display = new ArrayList<String>();
        logic.run(command);
        display = logic.getOutput();
        return display;
    }
    
}
