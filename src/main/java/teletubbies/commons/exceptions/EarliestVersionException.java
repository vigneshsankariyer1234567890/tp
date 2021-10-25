package teletubbies.commons.exceptions;

public class EarliestVersionException extends Exception {

    private static final String EXCEPTION_MESSAGE = "History Manager is currently pointing to the"
            + "earliest version and cannot be undone.";

    public EarliestVersionException() {
        super(EXCEPTION_MESSAGE);
    }
}
