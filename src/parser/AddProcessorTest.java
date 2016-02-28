package parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class AddProcessorTest {

	AddProcessor AP = new AddProcessor();
	@Test
	public void testAddCommand() {
		AP.addCommand("add make merrel study date: 29feb to 14march time: 8am-1030pm");
		assertEquals("make merrel study", AP.getTask());
		assertEquals(20160229, AP.getStartDate());
		assertEquals(20160314, AP.getEndDate());
		assertEquals(800, AP.getStartTime());
		assertEquals(2230, AP.getEndTime());
		reset();
		
		AP.addCommand("add make merrel study date: 5/6 time: 3.45pm");
		assertEquals("make merrel study", AP.getTask());
		assertEquals(20160605, AP.getStartDate());
		assertEquals(20160605, AP.getEndDate());
		assertEquals(1545, AP.getStartTime());
		assertEquals(1545, AP.getEndTime());
		reset();
		
		AP.addCommand("add make merrel study");
		assertEquals("make merrel study", AP.getTask());
		assertEquals(-1, AP.getStartDate());
		assertEquals(-1, AP.getEndDate());
		assertEquals(-1, AP.getStartTime());
		assertEquals(-1, AP.getEndTime());
		reset();
	}

	@Test
	public void testReadTask() {
		AP.convertToArray("add make merrel study date: 29feb time: 8am-1030pm");
		AP.readTask();
		assertEquals("make merrel study", AP.getTask());
		reset();
		
		AP.convertToArray("add play dota date: 29feb time: 8am-1030pm");
		AP.readTask();
		assertEquals("play dota", AP.getTask());
		reset();
	}

	@Test
	public void testIsStartOfDate() {
		assertTrue(AP.isStartOfDate("date:"));
	}

	@Test
	public void testReadDate() {
		AP.convertToArray("add make merrel study date: 29feb time: 8am-1030pm");
		AP.readDate(5);
		assertEquals(20160229, AP.getStartDate());
		reset();
		
		AP.convertToArray("add make merrel study date: 29feb 2013-9mar 2013 time: 8am-1030pm");
		AP.readDate(5);
		assertEquals(20130229, AP.getStartDate());
		assertEquals(20130309, AP.getEndDate());
		reset();
		
		AP.convertToArray("add apply ubc scholarship date: 29/2-5/3 time: 8am-1030pm");
		AP.readDate(5);
		assertEquals(20160229, AP.getStartDate());
		assertEquals(20160305, AP.getEndDate());
		reset();
		
		AP.convertToArray("add apply ubc scholarship date: 2-15/3 time: 8am-1030pm");
		AP.readDate(5);
		assertEquals(20160302, AP.getStartDate());
		assertEquals(20160315, AP.getEndDate());
		reset();
	}

	@Test
	public void testIsStartOfTime() {
		assertTrue(AP.isStartOfTime("time:"));
	}

	@Test
	public void testReadTime() {
		AP.convertToArray("add make merrel study date: 29feb time: 8am-1030pm");
		AP.readTime(7);
		assertEquals(800, AP.getStartTime());
		assertEquals(2230, AP.getEndTime());
		reset();
		

		AP.convertToArray("add apply ubc scholarship date: 29/2-5/3 time: 8-3pm");
		AP.readTime(7);
		assertEquals(800, AP.getStartTime());
		assertEquals(1500, AP.getEndTime());
		reset();
		
		AP.convertToArray("add apply ubc scholarship date: 2-15/3 time: 12-4.15pm");
		AP.readTime(7);
		assertEquals(1200, AP.getStartTime());
		assertEquals(1615, AP.getEndTime());
		reset();
	}
	
	private void reset() {
		AP.clearList();
		AP.reset();
		AP.clearDP();
		AP.clearTP();
	}

}
