package teletubbies.model;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import teletubbies.model.person.Person;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook extends Observable {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

}
