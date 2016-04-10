//@@author A0125003A
package parsertest;

import static org.junit.Assert.assertEquals;
import java.time.LocalDate;
import org.junit.Test;
import parser.DateParser;

public class DateParserTest {

	DateParser DP = new DateParser();
	
	/*POSITIVE VALUE PARTITION CASES*/		
	/*case 1: test ability to read ddmm formats*/
	@Test
	public void testA() throws Exception {	
		DP.parseDate("7/6");
		assertEquals("2016-06-07", DP.getDateObject().toString());
		reset();
	}
	
	/*case 2: test ability to read ddmonthyyyy formats*/
	@Test
	public void testB() throws Exception {
		DP.parseDate("6 june 2014");
		assertEquals("2014-06-06", DP.getDateObject().toString());
		reset();
	}
	
	/*case 3: test ability to read ddmmyyyy formats*/
	@Test
	public void testC() throws Exception {
		DP.parseDate("5/6/16");
		assertEquals("2016-06-05", DP.getDateObject().toString());
		reset();
	}
	
	/*case 4: test ability to read relative dates*/
	@Test
	public void testD() throws Exception {
		DP.parseDate("next friday");
		assertEquals("2016-04-22", DP.getDateObject().toString());
		reset();
	}
	
	/*case 5: boundary value for positive-value partition (31)*/
	@Test
	public void testE() throws Exception {
		DP.parseDate("31 july 2000");
		assertEquals("2000-07-31", DP.getDateObject().toString());
		reset();
	}
	
	/*case 6: boundary value for positive-value partition (31)*/
	@Test
	public void testF() throws Exception {
		DP.parseDate("31/12/2000");
		assertEquals("2000-12-31", DP.getDateObject().toString());
		reset();
	}
	
	/*case 7: reading relative dates*/
	@Test
	public void testG() throws Exception {
		DP.parseDate("sat");
		assertEquals("2016-04-16", DP.getDateObject().toString());
		reset();
		
		DP.parseDate("today");
		assertEquals(LocalDate.now(), DP.getDateObject());
		reset();
		
		DP.parseDate("tonight");
        assertEquals(LocalDate.now(), DP.getDateObject());
        reset();
	}
	
	/*NEGATIVE VALUE TEST CASES*/
	/*case 8: test ability to reject non-symbol separated numbers*/
	@Test(expected = Exception.class)
    public void testH() throws Exception {
	    DP.parseDate("1 11 2012");
	}
	
	/*case 9: ability to reject out of bound dates. boundary being 31*/
	@Test(expected = Exception.class)
    public void testI() throws Exception {
	    DP.parseDate("32 feb 2012");
	}
	
	/*case 10: ability to reject non-conforming relative date formats*/
	@Test(expected = Exception.class)
    public void testJ() throws Exception {
	    DP.parseDate("next next monday");
	}
	
	//method resets the list and dates for testing purposes
	private void reset() {
		DP.resetDate();
	}
}