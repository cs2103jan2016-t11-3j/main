package logic.exceptions;

import static logic.constants.Strings.*;

@SuppressWarnings("serial")
public class SearchException extends Exception {

	public SearchException() {
		
	}
	
	public SearchException(boolean isSearchDate) {
		super(MESSAGE_SEARCH_BY_DATE_REQUIRED_EXCEPTION);
	}
	
	public String getSearchExceptionMessage() {
		return super.getMessage();
	}
}
