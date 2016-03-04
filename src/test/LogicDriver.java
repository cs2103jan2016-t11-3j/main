package test;

import java.util.ArrayList;
import java.util.Scanner;

import logic.Logic;

public class LogicDriver {
    
    private static ArrayList<String> display = new ArrayList<String>();
    private static Scanner scanner = new Scanner(System.in);   

    public static void main(String[] args) {
        Logic logic = new Logic();
            while (true) {
                System.out.print("Command: ");
                String userCommand = scanner.nextLine();
                logic.run(userCommand);
                display = logic.getOutput();
                print(display);
            }


    }

    private static void print(ArrayList<String> list) {
        for(String line : list)
            System.out.print(line + "\n");
    }

}
