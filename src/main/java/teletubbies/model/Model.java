package teletubbies.model;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import teletubbies.commons.core.GuiSettings;
import teletubbies.commons.core.UserProfile;
import teletubbies.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Replaces user profile data with the data in {@code userProfile}.
     */
    void setUserProfile(UserProfile userProfile);

    /**
     * Returns the user profile.
     */
    UserProfile getUserProfile();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /**
     * Replaces the address book displayed to users with the list of persons to be exported.
     * Original address book is stored as a copy until export is complete.
     * @param filteredPersonList Filtered list of persons containing user-specified tags.
     */
    void updateExportList(List<Person> filteredPersonList);

    /**
     * Returns true if there is a pending export.
     * @return state of export confirmation.
     */
    boolean isAwaitingExportConfirmation();

    /**
     * Returns address book upon confirmation of export.
     * @return Address book containing contacts to export.
     */
    AddressBook getExportAddressBook();

    /**
     * Resets the address book if export is cancelled.
     */
    void cancelPendingExport();

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Adds the recent text input to the history as stored by {@code CommandInputHistory}.
     * @param textInput to be added to the {@code CommandInputHistory}
     */
    void addCommandInput(String textInput);

    /**
     * Returns the full list of previous inputs entered by user in ascending order.
     * @return list of previous inputs entered by user in ascending order.
     */
    List<String> getChronologicallyAscendingHistory();

    /**
     * Returns the full list of previous inputs entered by user in descending order.
     * @return list of previous inputs entered by user in descending order.
     */
    List<String> getChronologicallyDescendingHistory();
}
