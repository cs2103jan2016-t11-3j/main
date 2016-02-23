package logic.help;

import java.util.ArrayList;

public class ExecuteHelp {

	public static void main(String[] args) {
		Help help = new Help("Hello");
		ArrayList<String> toDisp = new ArrayList<String>();
		toDisp = help.run();

		for (int i = 0; i < toDisp.size(); i++) {
			System.out.println(toDisp.get(i));
		}

		Help helper = new Help("Add");
		ArrayList<String> toDisplay = new ArrayList<String>();
		toDisplay = helper.run();

		for (int i = 0; i < toDisplay.size(); i++) {
			System.out.println(toDisplay.get(i));
		}

		Help h = new Help();
		ArrayList<String> display = new ArrayList<String>();
		display = h.run();

		for (int i = 0; i < display.size(); i++) {
			System.out.println(display.get(i));
		}
	}

}
