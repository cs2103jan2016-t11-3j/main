package parsertest;

import static org.junit.Assert.*;

import org.junit.Test;

import parser.TimeProcessor;

public class TimeProcessorTest {
	
	private TimeProcessor TP = new TimeProcessor();
	
	@Test
	public void testSetTime() {
		
		/*
		 * test case 1: 4.30pm in the 0th index should be 1630 start & end
		 */
		TP.setTime("time: 4.30pm", 0, true);
		assertEquals(1630, TP.getStartTime());
		assertEquals(1630, TP.getEndTime());
		reset();
		
		/*test case 2: 4.30am in 0th index should be 430 start & end
		 * 
		 */
		TP.setTime("4.30am", 0, false);
		assertEquals(430, TP.getStartTime());
		assertEquals(430, TP.getEndTime());
		reset();
		
		/*test case 3: 5pm in 0th index and 732pm at 1st index
		 *will give 1700 and 1932 as start and end times 
		 */
		TP.setTime("5pm", 0, true);
		assertEquals(1700, TP.getStartTime());
		assertEquals(1700, TP.getEndTime());
		TP.setTime("732pm", 1, true);
		assertEquals(1700, TP.getStartTime());
		assertEquals(1932, TP.getEndTime());
		reset();
		
		/*test case 4: 12pm is recognize as 12 midnight hence 0000
		 */
		TP.setTime("12pm", 0, true);
		assertEquals(0000, TP.getStartTime());
		reset();
		
		/*test case 5: 2 : 30 pm should be 1430 and the non-numerical symbols
		 *will be replaced 
		 */
		TP.setTime("2 : 30 pm", 0, true);
		assertEquals(1430, TP.getStartTime());
		reset();
		
		/*test case 6: nonsensical entries will be ignored
		 */
		TP.setTime("24935", 0, false);
		assertEquals(-1, TP.getStartTime());
		reset();
	}
	
	@Test
	public void testConvertToArray() {
		/*
		 * test case 1: "7pm-8pm"
		 * purpose: test splitting by "-" keyword
		 */
		TP.convertToArray("7pm-8pm");
		assertEquals(2, TP.getListSize());
		assertEquals("7pm", TP.getListElement(0));
		assertEquals("8pm", TP.getListElement(1));
		reset();
		
		/*
		 * test case 2: "7pmto8pm"
		 * purpose: test splitting by "to" keyword
		 */
		TP.convertToArray("7pmto8pm");
		assertEquals(2, TP.getListSize());
		assertEquals("7pm", TP.getListElement(0));
		assertEquals("8pm", TP.getListElement(1));
		reset();
		
		/*
		 *test case 3: "7pm to 8pm"
		 *purpose: test removal of spaces
		 */
		TP.convertToArray("7pm to 8pm");
		assertEquals(2, TP.getListSize());
		assertEquals("7pm", TP.getListElement(0));
		assertEquals("8pm", TP.getListElement(1));
		reset();
	}
	
	@Test
	public void testProcessTime() {
		
		/*
		 * test case 1: "1pm to 3pm"
		 * purpose: test overall function with a simple command
		 */
		TP.processTime("1pm to 3pm");
		assertEquals(1300, TP.getStartTime());
		assertEquals(1500, TP.getEndTime());
		reset();
		
		/*
		 * test case 2: "1 to 3pm"
		 * purpose: test if function to recognize the first time without am/pm
		 */
		TP.processTime("1 to 3pm");
		assertEquals(1300, TP.getStartTime());
		assertEquals(1500, TP.getEndTime());
		reset();
		
		/*
		 * test case 3: "9 to 3am"
		 * purpose: test if function recognise a crossover from am to pm or vice versa
		 */
		TP.processTime("9 to 3am");
		assertEquals(2100, TP.getStartTime());
		assertEquals(300, TP.getEndTime());
		reset();
		
		/*
		 * test case 4: "5 to 3pm"
		 * purpose: test if function recognise a crossover from am to pm or vice versa
		 */
		TP.processTime("5 to 3pm");
		assertEquals(500, TP.getStartTime());
		assertEquals(1500, TP.getEndTime());
		reset();
		
		/*
		 * test case 5: "5 to 17"
		 * purpose: test if function recognise time without am/pm keywords
		 */
		TP.processTime("5 to 17");
		assertEquals(500, TP.getStartTime());
		assertEquals(1700, TP.getEndTime());
		reset();
		
		TP.processTime("12-4.15pm");
		assertEquals(1200, TP.getStartTime());
		assertEquals(1615, TP.getEndTime());
		reset();
		
		TP.processTime("3.45pm");
		assertEquals(1, TP.getListSize());
		assertEquals(1545, TP.getStartTime());
		assertEquals(1545, TP.getEndTime());
		reset();
	}

	
	private void reset() {
		TP.clearList();
		TP.resetTime();
	}

	

}
