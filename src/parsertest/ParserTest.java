//@@author A0125003A
package parsertest;

import static org.junit.Assert.assertEquals;
import common.CommandObject;
import common.TaskObject;
import java.time.LocalDateTime;
import org.junit.Test;
import parser.Parser;


/**
 * This class contains the JUnit test cases for the parser. It tests the input for all cases.
 * 
 * 
 * @author sylvesterchin
 */
public class ParserTest {
	
	Parser tempParser = new Parser();
	TaskObject task = new TaskObject();
	
	/*ADD COMMAND POSITIVE-VALUE PARTITION*/
	
	//--Test adding of floating task 
	@Test
	public void testAdd1() throws Exception {
		Parser tempParser = new Parser("add homework", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(1, cmd.getCommandType());
		assertEquals("homework", cmd.getTaskObject().getTitle());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getStartDateTime());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getEndDateTime());
		assertEquals("incomplete", cmd.getTaskObject().getStatus());
		assertEquals("floating", cmd.getTaskObject().getCategory());
		reset();
	}
	
	//--Test adding deadline
	@Test
	public void testAdd2() throws Exception {
		Parser tempParser = new Parser("add homework IE2100 by 29feb 9am", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(1, cmd.getCommandType());
		assertEquals("homework IE2100", cmd.getTaskObject().getTitle());
		assertEquals("2016-02-29T09:00",cmd.getTaskObject().getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getEndDateTime());
		assertEquals("incomplete", cmd.getTaskObject().getStatus());
		assertEquals("deadline", cmd.getTaskObject().getCategory());
		reset();
	}
	
	//--Test adding deadline with relative date
	@Test
	public void testAdd3() throws Exception {
		Parser tempParser = new Parser("add homework IE2100 by tomorrow 9am", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(1, tempParser.getCommandType());
		assertEquals("homework IE2100", cmd.getTaskObject().getTitle());
		assertEquals("2016-04-05T09:00",cmd.getTaskObject().getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getEndDateTime());
		assertEquals("incomplete", cmd.getTaskObject().getStatus());
		assertEquals("deadline", cmd.getTaskObject().getCategory());
		reset();
	}
	
	//--Test adding deadline with relative date without time
	@Test
	public void testAdd4() throws Exception {
		Parser tempParser = new Parser("add homework IE2100 by tomorrow", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(1, tempParser.getCommandType());
		assertEquals("homework IE2100", cmd.getTaskObject().getTitle());
		assertEquals("2016-04-05T23:59:59.999999999",cmd.getTaskObject().getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getEndDateTime());
		assertEquals("incomplete", cmd.getTaskObject().getStatus());
		assertEquals("deadline", cmd.getTaskObject().getCategory());
		reset();
	}
	
	
	//--Test adding deadline with date/time in description
	@Test
	public void testAdd5() throws Exception {
		Parser tempParser = new Parser("add prep 5pm lecture by 29feb 9am", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(1, tempParser.getCommandType());
		
		assertEquals("prep 5pm lecture", cmd.getTaskObject().getTitle());
		assertEquals("2016-02-29T09:00",cmd.getTaskObject().getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getEndDateTime());
		assertEquals("incomplete", cmd.getTaskObject().getStatus());
		assertEquals("deadline", cmd.getTaskObject().getCategory());
		reset();
	}
	
	//--Test adding deadline with time only
	@Test
	public void testAdd6() throws Exception {
		Parser tempParser = new Parser("add prep 5pm lecture by 9am", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(1, tempParser.getCommandType());
		
		assertEquals("prep 5pm lecture", cmd.getTaskObject().getTitle());
		assertEquals("2016-04-04T09:00",cmd.getTaskObject().getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getEndDateTime());
		assertEquals("incomplete", cmd.getTaskObject().getStatus());
		assertEquals("deadline", cmd.getTaskObject().getCategory());
		reset();
	}
	
	//--Test adding relative day-of-week with time
	@Test
	public void testAdd7() throws Exception {
		Parser tempParser = new Parser("add prep lecture on this sunday 4.10am", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(1, tempParser.getCommandType());
		
		assertEquals("prep lecture", cmd.getTaskObject().getTitle());
		assertEquals("2016-04-10T04:10",cmd.getTaskObject().getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getEndDateTime());
		assertEquals("incomplete", cmd.getTaskObject().getStatus());
		assertEquals("deadline", cmd.getTaskObject().getCategory());
		reset();
	}
	
	//--Test adding event
	@Test
	public void testAdd8() throws Exception {
		Parser tempParser = new Parser("add prep 5pm lecture from 29feb 9am to 8pm", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(1, tempParser.getCommandType());
		
		assertEquals("prep 5pm lecture", cmd.getTaskObject().getTitle());
		assertEquals("2016-02-29T09:00",cmd.getTaskObject().getStartDateTime().toString());
		assertEquals("2016-02-29T20:00",cmd.getTaskObject().getEndDateTime().toString());
		assertEquals("incomplete", cmd.getTaskObject().getStatus());
		assertEquals("event", cmd.getTaskObject().getCategory());
		reset();
	}
	
	//--Test adding recurring deadline with relative start date
	@Test
	public void testAdd9() throws Exception {
		Parser tempParser = new Parser("add 5pm lecture every friday at 4pm until 9june", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(1, tempParser.getCommandType());
		
		assertEquals("5pm lecture", cmd.getTaskObject().getTitle());
		assertEquals("2016-04-08T16:00",cmd.getTaskObject().getStartDateTime().toString());
		assertEquals("WEEKLY",cmd.getTaskObject().getInterval().getFrequency());
		assertEquals(1,cmd.getTaskObject().getInterval().getTimeInterval());
		assertEquals("2016-06-09T23:59:59.999999999",cmd.getTaskObject().getInterval().getUntil().toString());
		assertEquals("incomplete", cmd.getTaskObject().getStatus());
		assertEquals("deadline", cmd.getTaskObject().getCategory());
		reset();
	}
	
	//--Test adding recurring event with relative start date
	@Test
	public void testAdd10() throws Exception {
		Parser tempParser = new Parser("add 5pm lecture every wednesday from 8am to 9am until 9june", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(1, tempParser.getCommandType());
		
		assertEquals("5pm lecture", cmd.getTaskObject().getTitle());
		assertEquals("2016-04-06T08:00",cmd.getTaskObject().getStartDateTime().toString());
		assertEquals("2016-04-06T09:00",cmd.getTaskObject().getEndDateTime().toString());
		assertEquals("WEEKLY",cmd.getTaskObject().getInterval().getFrequency());
		assertEquals(1,cmd.getTaskObject().getInterval().getTimeInterval());
		assertEquals("2016-06-09T23:59:59.999999999",cmd.getTaskObject().getInterval().getUntil().toString());
		assertEquals("incomplete", cmd.getTaskObject().getStatus());
		assertEquals("event", cmd.getTaskObject().getCategory());
		reset();
	}
	
	//--Test adding multiple recurring events with count to limit recurrence
	@Test
	public void testAdd11() throws Exception {
		Parser tempParser = new Parser("add go gym every mon, wed and fri from 8am to 9am for 12 weeks", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(1, tempParser.getCommandType());
		assertEquals("go gym", cmd.getTaskObject().getTitle());
		assertEquals("2016-04-04T08:00",cmd.getTaskObject().getStartDateTime().toString());
		assertEquals("2016-04-04T09:00",cmd.getTaskObject().getEndDateTime().toString());
		assertEquals("WEEKLY",cmd.getTaskObject().getInterval().getFrequency());
		assertEquals(1,cmd.getTaskObject().getInterval().getTimeInterval());
		assertEquals(LocalDateTime.MAX,cmd.getTaskObject().getInterval().getUntil());
		assertEquals(12 ,cmd.getTaskObject().getInterval().getCount());
		assertEquals("incomplete", cmd.getTaskObject().getStatus());
		assertEquals("event", cmd.getTaskObject().getCategory());
		assertEquals(1 ,cmd.getTaskObject().getInterval().getByDayArray()[1]);
        assertEquals(0 ,cmd.getTaskObject().getInterval().getByDayArray()[2]);
        assertEquals(1 ,cmd.getTaskObject().getInterval().getByDayArray()[3]);
        assertEquals(0 ,cmd.getTaskObject().getInterval().getByDayArray()[4]);
        assertEquals(1 ,cmd.getTaskObject().getInterval().getByDayArray()[5]);
        assertEquals(0 ,cmd.getTaskObject().getInterval().getByDayArray()[6]);
        assertEquals(0 ,cmd.getTaskObject().getInterval().getByDayArray()[7]);
		reset();
	}
	
	//--Test adding recurring task without limit
	@Test
	public void testAdd12() throws Exception {
		Parser tempParser = new Parser("add 5pm lecture every friday at 4pm", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(1, tempParser.getCommandType());
		
		assertEquals("5pm lecture", cmd.getTaskObject().getTitle());
		assertEquals("2016-04-08T16:00",cmd.getTaskObject().getStartDateTime().toString());
		assertEquals("WEEKLY",cmd.getTaskObject().getInterval().getFrequency());
		assertEquals(1,cmd.getTaskObject().getInterval().getTimeInterval());
		assertEquals("incomplete", cmd.getTaskObject().getStatus());
		assertEquals("deadline", cmd.getTaskObject().getCategory());
		reset();
	}
	
	//--Test adding recurring task "everyday"
	@Test
	public void testAdd13() throws Exception {
		Parser tempParser = new Parser("add play dota everyday at 11pm", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(1, tempParser.getCommandType());
		assertEquals("play dota", cmd.getTaskObject().getTitle());
		assertEquals("2016-04-04T23:00",cmd.getTaskObject().getStartDateTime().toString());
		assertEquals("DAILY",cmd.getTaskObject().getInterval().getFrequency());
		assertEquals(1,cmd.getTaskObject().getInterval().getTimeInterval());
		assertEquals("incomplete", cmd.getTaskObject().getStatus());
		assertEquals("deadline", cmd.getTaskObject().getCategory());
		reset();
	}
	
	//--Test searching for task, date and time together
	@Test
	public void testSearch1() throws Exception {
		Parser tempParser = new Parser("search hi 7/9/2016 7pm", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(2, cmd.getCommandType());
		assertEquals("hi", cmd.getTaskObject().getTitle());
		assertEquals("2016-09-07T19:00", cmd.getTaskObject().getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getEndDateTime());
		reset();
	}
	
	//--Test searching for relative date
	@Test
	public void testSearch2() throws Exception {
		Parser tempParser = new Parser("search today", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(2, cmd.getCommandType());
		assertEquals("", tempParser.getTask());
		assertEquals("2016-04-04T23:59:59.999999999", cmd.getTaskObject().getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getEndDateTime());
		reset();
	}
	
	//--Test searching without keywords
	@Test
	public void testSearch3() throws Exception {
		Parser tempParser = new Parser("aagadfgad", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(2, cmd.getCommandType());		
		assertEquals("aagadfgad", cmd.getTaskObject().getTitle());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getStartDateTime());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getEndDateTime());
		reset();
	}
	
	//--Test searching without keywords for time
	@Test
	public void testSearch4() throws Exception {
		Parser tempParser = new Parser("7.13pm", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(2, cmd.getCommandType());		
		assertEquals("", cmd.getTaskObject().getTitle());
		assertEquals("+999999999-12-31T19:13", cmd.getTaskObject().getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getEndDateTime());
		reset();
	}
	
	//--Test searching for task details, to pass back an index when only an integer follows the
	//search keyword
	@Test
	public void testSearch5() throws Exception {
		Parser tempParser = new Parser("search 3", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(2, cmd.getCommandType());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getStartDateTime());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getEndDateTime());
		assertEquals(2, cmd.getCommandType());
		assertEquals(3, cmd.getIndex());
		reset();
	}
	
	//--Test searching for double digit indexes
	@Test
	public void testSearch6() throws Exception {
		Parser tempParser = new Parser("search 30", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(2, cmd.getCommandType());
		assertEquals(30, cmd.getIndex());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getStartDateTime());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getEndDateTime());
		reset();
	}
	
	//--Test searching for task without any parameters
	@Test
	public void testSearch7() throws Exception {
		Parser tempParser = new Parser("view", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(2, cmd.getCommandType());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getStartDateTime());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getEndDateTime());
		reset();
	}
	
	//--Test editing title and time simultaneously
	@Test
	public void testEdit1() throws Exception {
		Parser tempParser = new Parser("edit 2 get task 755pm", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(3, cmd.getCommandType());
		assertEquals("get task", cmd.getTaskObject().getTitle());
		assertEquals("+999999999-12-31T19:55", cmd.getTaskObject().getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getEndDateTime());
		reset();
	}
	
	//--Test editing timing 
	@Test
	public void testEdit2() throws Exception {
		Parser tempParser = new Parser("edit 2 755pm", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(3, cmd.getCommandType());
		assertEquals("", cmd.getTaskObject().getTitle());
		assertEquals("+999999999-12-31T19:55", cmd.getTaskObject().getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getEndDateTime());
		reset();
	}
	
	//--Test editing start and end DateTime
	@Test
	public void testEdit3() throws Exception {
		Parser tempParser = new Parser("EDIT 2 from 8 june 755pm to 9 june 9pm", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(3, cmd.getCommandType());
		assertEquals("", cmd.getTaskObject().getTitle());
		assertEquals("2016-06-08T19:55",cmd.getTaskObject().getStartDateTime().toString());
		assertEquals("2016-06-09T21:00",cmd.getTaskObject().getEndDateTime().toString());
		reset();
	}
	
	//--Test editing timing with "end" keyword
	@Test
	public void testEdit4() throws Exception {
		Parser tempParser = new Parser("edit 2 755pm end", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(3, cmd.getCommandType());
		assertEquals("", cmd.getTaskObject().getTitle());
		assertEquals("+999999999-12-31T19:55", cmd.getTaskObject().getEndDateTime().toString());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getStartDateTime());
		reset();
	}
	
	//--Test editing interval
	@Test
	public void testEdit5() throws Exception {
		Parser tempParser = new Parser("edit 12 every friday", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(3, cmd.getCommandType());
		assertEquals("", cmd.getTaskObject().getTitle());
		assertEquals(12,cmd.getIndex());
		assertEquals("WEEKLY", tempParser.TO.getInterval().getFrequency());
		assertEquals(1, tempParser.TO.getInterval().getTimeInterval());
		assertEquals("2016-04-08T23:59:59.999999999",cmd.getTaskObject().getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getEndDateTime());
		reset();
	}
	
	//--Test saving file as another name
	@Test
	public void testSave() throws Exception {
		Parser tempParser = new Parser("save as C://mac/desktop", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(7, cmd.getCommandType());
		assertEquals("as C://mac/desktop", cmd.getTaskObject().getTitle());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getStartDateTime());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getEndDateTime());
		reset();
	}
	
	//--Test marking task as done
	@Test
	public void testDone() throws Exception {
		Parser tempParser = new Parser("done", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(2, cmd.getCommandType());
		assertEquals("", cmd.getTaskObject().getTitle());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getStartDateTime());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getEndDateTime());
		assertEquals("completed", cmd.getTaskObject().getStatus());
		reset();
	}
	
	//--Test marking task as not done
	@Test
	public void testNotDone() throws Exception {
		Parser tempParser = new Parser("incomplete", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(2, cmd.getCommandType());
		assertEquals("", cmd.getTaskObject().getTitle());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getStartDateTime());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getEndDateTime());
		assertEquals("incomplete", cmd.getTaskObject().getStatus());
		reset();
	}
	
	//--Test deleting with "all" keyword
	@Test
	public void testDelete() throws Exception {
		Parser tempParser = new Parser("delete 4 all", 1);
		CommandObject cmd = new CommandObject();
		cmd = tempParser.run();
		assertEquals(4, cmd.getCommandType());
		assertEquals("all", cmd.getTaskObject().getTitle());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getStartDateTime());
		assertEquals(LocalDateTime.MAX, cmd.getTaskObject().getEndDateTime());
		reset();
	}
	
	
	private void reset() {
		tempParser.resetTaskObj();
	}
}
