package teletubbies.model.person;

import static java.util.Objects.requireNonNull;
import static teletubbies.logic.parser.CliSyntax.EMPTY_REMARK;

/**
 * Represents a Person's remark in the address book.
 * Guarantees: immutable; is always valid
 */
public class Remark {

    // @@author: j-lum
    // Reused from
    // https://github.com/se-edu/addressbook-level3/compare/tutorial-add-remark
    public final String value;

    /**
     * Constructs a {@code Remark}.
     *
     * @param remark A remark associated with a contact.
     */
    public Remark(String remark) {
        requireNonNull(remark);
        value = remark;
    }

    /**
     * Returns if a remark is present.
     */
    public boolean isPresent() {
        return !this.value.equals(EMPTY_REMARK);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Remark // instanceof handles nulls
                && value.equals(((Remark) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
