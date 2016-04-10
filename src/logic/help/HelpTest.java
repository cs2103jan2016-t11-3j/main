//@@author A0124052X

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

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	// File Path
	public static String FILE_PATH = "/src/logic/help/";

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
			File fakeFile = new File("");
			String path = fakeFile.getAbsolutePath(); // To obtain working directory
			File file = new File(path + FILE_PATH + "1.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			text = reader.readLine();
			System.out.println(toDisp.get(i));
			assertEquals(text, "Adding an Event/Deadline/Floating task");
			i++;
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
