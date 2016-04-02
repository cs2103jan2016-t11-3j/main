package parser.exceptions;

@SuppressWarnings("serial")
public class InvalidDateFormatException extends Exception {

    static final String MESSAGE = "The date format %s is unrecognized";
    
    public InvalidDateFormatException(String input) {
        
        super(String.format(MESSAGE, input));
    }

}
