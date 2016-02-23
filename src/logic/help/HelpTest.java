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

public class HelpTest {

	private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@Test
	// For invalid search string
	public void testHelpFunctionOne() {
		PrintStream printStream = new PrintStream(outContent);
		System.setOut(printStream);
		Help helper = new Help("hello");
		ArrayList<String> toDisplay = new ArrayList<String>();
		toDisplay = helper.run();
		for (int i = 0; i < toDisplay.size(); i++) {
			System.out.print(toDisplay.get(i));
			assertEquals(Help.MESSAGE_NO_TOPIC, outContent.toString());
		}
		printStream.flush();
	}

	@Test
	// For no search string
	public void testHelpFunctionTwo() {
		PrintStream newPrintStream = new PrintStream(outContent);
		System.setOut(newPrintStream);
		String text = "";
		int i = 0;
		Help help = new Help();
		ArrayList<String> toDisp = new ArrayList<String>();
		toDisp = help.run();
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
