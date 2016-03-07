package test;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Scanner;

import logic.Logic;

public class LogicDriver {
    
    private static ArrayList<String> display = new ArrayList<String>();
    private static Scanner scanner = new Scanner(System.in);   
    private static Logic logic = new Logic();
    
    public static void main(String[] args) throws NoSuchFileException, IOException {
        logic.load();
            while (true) {
                System.out.print("Command: ");
                String userCommand = scanner.nextLine();
                display = execute(userCommand);
                print(display);
            }


    }

    private static ArrayList<String> execute(String userCommand) {
        logic.run(userCommand);
        display = logic.getOutput();
        return display;
    }

    private static void print(ArrayList<String> list) {
        for(String line : list)
            System.out.print(line + "\n");
    }

}
