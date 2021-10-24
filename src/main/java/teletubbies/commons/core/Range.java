package teletubbies.commons.core;

import teletubbies.commons.core.index.Index;

import java.util.List;
import java.util.stream.Collectors;

public class Range {

    public static final String MESSAGE_ILLEGAL_RANGE = "Range contains values outside of the list";

    private final List<Index> values;

    public Range(List<Integer> values) {
        this.values = values.stream().map(Index::fromOneBased).collect(Collectors.toList());
    }

    public List<Index> getRangeValues() {
        return values;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Range // instanceof handles nulls
                && values.equals(((Range) other).values)); // state check
    }

}
