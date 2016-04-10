   package logic.sort;
   
   import java.time.LocalDate;
   import java.time.LocalDateTime;
   import java.time.LocalTime;
   import java.util.ArrayList;

import org.junit.Test;

import static org.junit.Assert.*;
   
   import common.TaskObject;
   
   public class SortTest {
   
       private ArrayList<TaskObject> testList = new ArrayList<TaskObject>();
       
       //incomplete tasks
       private TaskObject taskOne = new TaskObject("Do well for Finals", 
    		   LocalDateTime.of(LocalDate.parse("2016-04-25"), LocalTime.parse("09:00")),
    		   "deadline", "incomplete",1);
       private TaskObject taskTwo = new TaskObject("Submit CS2103", 
    		   LocalDateTime.of(LocalDate.parse("2016-04-20"), LocalTime.parse("10:00")),
    		   "deadline", "incomplete",2);
       private TaskObject taskThree = new TaskObject("Be awake early", 
    		   LocalDateTime.of(LocalDate.parse("2016-04-20"), LocalTime.parse("11:00")),
    		   "deadline", "incomplete",3);
       
       //overdue tasks
       private TaskObject taskFour = new TaskObject("Eat with my parents", 
    		   LocalDateTime.of(LocalDate.parse("2016-03-30"), LocalTime.parse("19:00")),
    		   "deadline", "overdue",4);
       private TaskObject taskFive = new TaskObject("Just a random task", 
    		   LocalDateTime.of(LocalDate.parse("2016-04-09"), LocalTime.parse("19:00")),
    		   "deadline", "overdue",5);
       
       //completed events with same startdate, different end dates
       private TaskObject taskSix = new TaskObject("Meeting with Block", 
    		   LocalDateTime.of(LocalDate.parse("2012-01-31"), LocalTime.parse("21:00")), 
    		   LocalDateTime.of(LocalDate.parse("2013-11-29"), LocalTime.parse("17:00")), 
    		   "event", "complete", 6);
       private TaskObject taskSeven = new TaskObject("Project Meeting", 
    		   LocalDateTime.of(LocalDate.parse("2012-01-31"), LocalTime.parse("21:00")), 
    		   LocalDateTime.of(LocalDate.parse("2012-01-31"), LocalTime.parse("23:59")), 
    		   "event", "complete", 7);
   	
       //floating tasks
       private TaskObject taskEight = new TaskObject("Buy groceries", "floating", "incomplete", 8);
       private TaskObject taskNine = new TaskObject("Collect money from people", "floating", "incomplete", 9);
       
       /* Correct Sorted Sequence
        * taskFour
        * taskFive
        * taskTwo
        * taskThree
        * taskOne
        * taskEight
        * taskNine
        * taskSeven
        * taskSix
        */
    
       
       @Test
       public void testSort() {
    	  
    	   testList.add(taskOne);
    	   testList.add(taskTwo);
    	   testList.add(taskThree);
    	   testList.add(taskFour);
    	   testList.add(taskFive);
    	   testList.add(taskSix);
    	   testList.add(taskSeven);
    	   testList.add(taskEight);
    	   testList.add(taskNine);
    	   
    	   Sort testSort = new Sort(testList);
    	   ArrayList<TaskObject> actualOutput = testSort.run();
    	   
    	   ArrayList<TaskObject> correctOutput = new ArrayList<TaskObject>();
    	   
    	   correctOutput.add(taskFour);
    	   correctOutput.add(taskFive);
    	   correctOutput.add(taskTwo);
    	   correctOutput.add(taskThree);
    	   correctOutput.add(taskOne);
    	   correctOutput.add(taskEight);
    	   correctOutput.add(taskNine);
    	   correctOutput.add(taskSeven);
    	   correctOutput.add(taskSix);
    	   
    	   assertEquals(actualOutput,correctOutput);
    	   correctOutput.clear();
       }
   }
