package teletubbies.model.tag;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import javafx.util.Pair;
import teletubbies.model.person.Person;

public class TagUtils {

    /**
     * Predicate for whether a person contains all
     * provided tags
     *
     * @param tagStringSet tags that persons must contain
     * @return person has tag predicate
     */
    public static Predicate<Person> personHasTagPredicate(Set<Pair<String, Optional<String>>> tagStringSet) {
        return person -> tagStringSet.stream().allMatch(tp -> person.getAllTags().stream().anyMatch(t -> {
            if (tp.getValue().isPresent()) {
                return t.tagName.equals(tp.getKey()) && Objects.equals(t.getTagValue(), tp.getValue().get());
            } else {
                return t.tagName.equals(tp.getKey());
            }
        }));
    }

}
