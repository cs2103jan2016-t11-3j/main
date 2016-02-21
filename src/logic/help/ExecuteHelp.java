package logic.help;

import java.util.ArrayList;

public class ExecuteHelp {

	public static void main(String[] args) {
		HelpFunction help = new HelpFunction("Hello");
		ArrayList<String> toDisp = new ArrayList<String>();
		toDisp = help.searchManual();
		
		for(int i = 0; i < toDisp.size(); i++) {
			System.out.println(toDisp.get(i));
		}
		
		HelpFunction helper = new HelpFunction("Add");
		ArrayList<String> toDisplay = new ArrayList<String>();
		toDisplay = helper.searchManual();
		
		for(int i = 0; i < toDisplay.size(); i++) {
			System.out.println(toDisplay.get(i));
		}
		
		HelpFunction h = new HelpFunction();
		ArrayList<String> display = new ArrayList<String>();
		display = h.searchManual();
		
		for(int i = 0; i < display.size(); i++) {
			System.out.println(display.get(i));
		}
	}

}
