package teletubbies.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's unique identifier in the address book.
 * Guarantees: immutable; unique
 */
public class Uuid {
    public final String uuid;

    /**
     * Constructs a {@code Uuid}.
     *
     * @param uuid Person's Uuid.
     */
    public Uuid(String uuid) {
        requireNonNull(uuid);
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return uuid;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof teletubbies.model.person.Uuid // instanceof handles nulls
                && uuid.equals(((teletubbies.model.person.Uuid) other).uuid)); // state check
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
