package teletubbies.commons.exceptions;

public class EmptyHistoryManagerException extends Exception {

    private static final String EXCEPTION_MESSAGE = "History Manager has no items and is empty.";

    public EmptyHistoryManagerException() {
        super(EXCEPTION_MESSAGE);
    }
}
