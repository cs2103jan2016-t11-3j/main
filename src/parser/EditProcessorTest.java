package parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class EditProcessorTest {

	EditProcessor EP = new EditProcessor();
	
	@Test
	public void testProcessEdit() {
		
		EP.processEdit("edit 2 8am");
		assertEquals("8am",EP.getListElement(0));
		assertEquals(800, EP.getStartTime());
		assertEquals(800, EP.getEndTime());
		assertEquals(-1, EP.getStartDate());
		assertEquals(-1, EP.getEndDate());
		assertEquals("", EP.getTask());
		reset();
		
		EP.processEdit("edit 1 6pm");
		assertEquals(1800, EP.getStartTime());
		assertEquals(1800, EP.getEndTime());
		assertEquals(-1, EP.getStartDate());
		assertEquals(-1, EP.getEndDate());
		assertEquals("", EP.getTask());
		reset();
		
		EP.processEdit("edit 1 6pm testing testing");
		assertEquals(-1, EP.getStartTime());
		assertEquals(-1, EP.getEndTime());
		assertEquals(-1, EP.getStartDate());
		assertEquals(-1, EP.getEndDate());
		assertEquals("6pm testing testing", EP.getTask());
		reset();
		
		EP.processEdit("edit 1 6 nov");
		assertEquals(-1, EP.getStartTime());
		assertEquals(-1, EP.getEndTime());
		assertEquals(20161106, EP.getStartDate());
		assertEquals(20161106, EP.getEndDate());
		assertEquals("", EP.getTask());
		reset();
		
		EP.processEdit("edit 1 11/6 end");
		assertEquals(-1, EP.getStartTime());
		assertEquals(-1, EP.getEndTime());
		assertEquals(-1, EP.getStartDate());
		assertEquals(20160611, EP.getEndDate());
		assertEquals("", EP.getTask());
		reset();
		
		
		EP.processEdit("edit 1 1/2/4 start");
		assertEquals(-1, EP.getStartTime());
		assertEquals(-1, EP.getEndTime());
		assertEquals(40201, EP.getStartDate());
		assertEquals(-1, EP.getEndDate());
		assertEquals("", EP.getTask());
		reset();
		
	}

	@Test
	public void testConvertToArray() {
		EP.convertToArray("edit 1 2 3 4 ");
		assertEquals(3, EP.getListSize());
		reset();
		
		EP.convertToArray("edit 1 hallo hallo ");
		assertEquals(2, EP.getListSize());
		reset();
	}

	@Test
	public void testCleanString() {
		EP.convertToArray("edit 1 2 3 4 ");
		assertEquals("2 3 4", EP.cleanString());
		reset();
		
		EP.convertToArray("edit 1 hallo hallo ");
		assertEquals("hallo hallo", EP.cleanString());
		reset();
	}

	@Test
	public void testIsDate() {
		EP.convertToArray("edit 2 5th jan");
		EP.cleanString();
		assertTrue(EP.isDate());
		reset();
		
		EP.convertToArray("edit 2 7/3 - 9/3");
		EP.cleanString();
		assertTrue(EP.isDate());
		reset();
		
		EP.convertToArray("edit 2 buy food on 5th june");
		EP.cleanString();
		assertTrue(EP.isDate());
		reset();
		
		EP.convertToArray("edit 2 buy food");
		EP.cleanString();
		assertFalse(EP.isDate());
		reset();
		
	}

	@Test
	public void testIsAlternativeDate() {
		assertTrue(EP.isAlternativeDate("6/7"));
		
		assertFalse(EP.isAlternativeDate("march"));
		
	}

	@Test
	public void testIsNotTask() {
		EP.convertToArray("edit 2 4-9pm");
		EP.cleanString();
		assertFalse(EP.isTask());
		reset();
		
		EP.convertToArray("edit 2 4-9pm go to market");
		EP.cleanString();
		assertTrue(EP.isTask());
		reset();
	}

	@Test
	public void testRemoveMonths() {
		assertEquals("", EP.removeMonths("january"));
		assertEquals("12 ", EP.removeMonths("12 january"));
		assertEquals("12  return box", EP.removeMonths("12 may return box"));
	}
	
	@Test
	public void testHasNumber() {
		assertTrue(EP.hasNumber("1pm"));
		assertTrue(EP.hasNumber("1 pm"));
	}

	@Test
	public void testIsTime() {
		EP.convertToArray("edit 2 4-9pm");
		EP.cleanString();
		assertTrue(EP.isTime());
		reset();
		
		EP.convertToArray("edit 2 spam");
		EP.cleanString();
		assertFalse(EP.isTime());
		reset();
		
		EP.convertToArray("edit 1 3 to 7 pm");
		EP.cleanString();
		assertTrue(EP.isTime());
		reset();
	}
	
	private void reset() {
		EP.clearList();
		EP.resetAll();
	}

}
