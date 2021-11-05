package teletubbies.model.person;

import java.util.Set;
import java.util.function.Predicate;

import teletubbies.model.tag.Tag;

public class PersonHasTagsPredicate implements Predicate<Person> {

    private final Set<Tag> tagSet;

    public PersonHasTagsPredicate(Set<Tag> tagSet) {
        this.tagSet = tagSet;
    }

    @Override
    public boolean test (Person person) {
        return tagSet.stream().allMatch(tp -> // For all test tags
            person.getAllTags().stream().anyMatch(t -> { // Person has tags
                if (tp.getTagValue().isEmpty()) {
                    return t.equals(tp);
                } else {
                    return t.equalsNameAndValue(tp);
                }
            }
        ));
    }

    /**
     * Number of tags that the person should
     * have
     *
     * @return number of tags matched
     */
    public int numberOfTags() {
        return tagSet.size();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PersonHasTagsPredicate // instanceof handles nulls
                && tagSet.equals(((PersonHasTagsPredicate) other).tagSet)); // state check
    }
}
