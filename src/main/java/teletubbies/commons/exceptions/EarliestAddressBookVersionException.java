package teletubbies.commons.exceptions;

public class EarliestAddressBookVersionException extends EarliestVersionException {

    private static final String EXCEPTION_MESSAGE = "Teletubbies is currently at it's earliest version and cannot "
            + "revert.";

    public EarliestAddressBookVersionException() {
        super(EXCEPTION_MESSAGE);
    }
}
