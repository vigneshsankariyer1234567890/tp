package teletubbies.model.tag;

import javafx.collections.ObservableList;
import javafx.util.Pair;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import teletubbies.model.person.Person;

public class TagUtils {

    public static List<Person> filterPersonsByTag(ObservableList<Person> personList,
                                                  Set<Pair<String, Optional<String>>> tagStringSet) {
        return personList.stream()
                .filter(person -> tagStringSet.stream().allMatch(tp -> person.getAllTags().stream().anyMatch(t -> {
                    if(tp.getValue().isPresent()) {
                        return t.tagName.equals(tp.getKey()) && Objects.equals(t.tagValue, tp.getValue().get());
                    } else {
                        return t.tagName.equals(tp.getKey());
                    }
                }))).collect(Collectors.toList());
    }

}
