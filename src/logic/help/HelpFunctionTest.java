package logic.help;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HelpFunctionTest {

	private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@Test
	// For invalid search string
	public void testHelpFunctionOne() {
		PrintStream printStream = new PrintStream(outContent);
		System.setOut(printStream);
		HelpFunction helper = new HelpFunction("hello");
		ArrayList<String> toDisplay = new ArrayList<String>();
		toDisplay = helper.searchManual();
		for (int i = 0; i < toDisplay.size(); i++) {
			System.out.print(toDisplay.get(i));
			assertEquals(HelpFunction.MESSAGE_NO_TOPIC, outContent.toString());
		}
		printStream.flush();
	}

	@Test
	// For no search string
	public void testHelpFunctionTwo() {
		String text = "";
		int i = 0;
		HelpFunction help = new HelpFunction();
		ArrayList<String> toDisp = new ArrayList<String>();
		toDisp = help.searchManual();
		try {
			File file = new File(HelpTopic.FILE_PATH + "helpfile.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			text = reader.readLine();
			while (text != null && i < toDisp.size()) {
				System.out.println(toDisp.get(i));
				assertEquals(text, outContent.toString());
				i++;
				text = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
