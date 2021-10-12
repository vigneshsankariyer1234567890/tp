package seedu.address.model.person;

/**
 * Represents a Person's completion status in the address book (to check if
 * the contact has been contacted).
 * Guarantees: immutable; is always valid
 */
public class CompletionStatus {
    public final boolean status;

    public CompletionStatus(boolean status) {
        this.status = status;
    }

    public CompletionStatus() {
        status = false;
    }

    @Override
    public String toString() {
        return String.valueOf(status);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CompletionStatus // instanceof handles nulls
                && (status == ((CompletionStatus) other).status)); // state check
    }
}
