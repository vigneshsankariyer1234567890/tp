package teletubbies.commons.exceptions;

public class LatestAddressBookVersionException extends LatestVersionException {

    private static final String EXCEPTION_MESSAGE = "Teletubbies is currently at it's latest version and cannot be "
            + "redone.";

    public LatestAddressBookVersionException() {
        super(EXCEPTION_MESSAGE);
    }
}
