package parser.exceptions;

@SuppressWarnings("serial")
public class InvalidDateFormatException extends Exception {

    String dateFormat = null;
    
    public InvalidDateFormatException(String input) {
        dateFormat = input;
    }
    
    public String getDateFormat() {
        return dateFormat;
    }

}
