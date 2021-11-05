package teletubbies.commons.core;

import java.util.Set;
import java.util.stream.Collectors;

import teletubbies.commons.core.index.Index;

public class Range {

    public static final String MESSAGE_ILLEGAL_RANGE = "Range contains values outside of the list!\n"
        + "Range values must be no greater than %d";

    private final Set<Index> values;

    /**
     * Creates a new {@code Range} from a set of one-based indices.
     */
    public Range(Set<Integer> values) {
        this.values = values.stream()
                .map(Index::fromOneBased)
                .collect(Collectors.toUnmodifiableSet());
    }

    public Set<Index> getRangeValues() {
        return values;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Range // instanceof handles nulls
                && values.containsAll(((Range) other).values)
                && ((Range) other).values.containsAll(values)); // state check
    }

}
