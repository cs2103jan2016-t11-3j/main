package test;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import logic.Logic;

public class LogicDriver {
    
    private static ArrayList<String> display = new ArrayList<String>();
    private static Logic logic = new Logic();
    
    public static void main(String[] args) throws NoSuchFileException, IOException {
            
        for (String command : args) {
            display = execute(command);
            print(display);
        }


    }

    /**
     * Method for testing
     * @param userCommand
     * @return
     */
    public static ArrayList<String> execute(String userCommand) {
        logic.run(userCommand);
        display = logic.getOutput();
        return display;
    }

    private static void print(ArrayList<String> list) {
        for(String line : list)
            System.out.print(line + "\n");
    }

}
