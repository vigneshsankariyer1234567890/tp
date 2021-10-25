package teletubbies.commons.exceptions;

public class LatestVersionException extends Exception {

    private static final String EXCEPTION_MESSAGE = "History Manager is currently pointing "
            + "to the latest version and cannot be redone.";

    public LatestVersionException() {
        super(EXCEPTION_MESSAGE);
    }
}
