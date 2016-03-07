package logic;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		Logic logic = new Logic();
		ArrayList<String> output = new ArrayList<String>();
		
		while (true) {
			System.out.print("command: ");
			String input = sc.nextLine();
			logic.run(input);
			
			output = logic.getOutput();
			for (int i = 0; i < output.size(); i++) {
				System.out.println(output.get(i));
			}
			
			System.out.println();
		}
	}
	
}
