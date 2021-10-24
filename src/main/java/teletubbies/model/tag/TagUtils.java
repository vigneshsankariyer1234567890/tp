package teletubbies.model.tag;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.util.Pair;
import teletubbies.model.person.Person;

public class TagUtils {

    /**
     * Filters given person list to list with only persons
     * containing all provided tags
     *
     * @param personList person list to filter from
     * @param tagStringSet tags that persons must contain
     * @return filtered person list
     */
    public static List<Person> filterPersonsByTag(ObservableList<Person> personList,
                                                  Set<Pair<String, Optional<String>>> tagStringSet) {
        return personList.stream()
                .filter(person -> tagStringSet.stream().allMatch(tp -> person.getAllTags().stream().anyMatch(t -> {
                    if (tp.getValue().isPresent()) {
                        return t.tagName.equals(tp.getKey()) && Objects.equals(t.getTagValue(), tp.getValue().get());
                    } else {
                        return t.tagName.equals(tp.getKey());
                    }
                }))).collect(Collectors.toList());
    }

}
