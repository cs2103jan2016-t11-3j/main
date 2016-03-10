package parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class DateTimeProcessorTest {
	DateTimeProcessor DTP = new DateTimeProcessor();
	@Test
	public void testParseDateTime() {
		DTP.parseDateTime("by 7 sept 8.45am");
		
	}

	@Test
	public void testSeparateDateTime() {
		//DTP.separateDateTime("on 7sept 8.45am", true);
	}
}
