package teletubbies.model;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import teletubbies.commons.core.GuiSettings;
import teletubbies.commons.core.Range;
import teletubbies.commons.core.UserProfile;
import teletubbies.commons.core.UserProfile.Role;
import teletubbies.commons.exceptions.EarliestVersionException;
import teletubbies.commons.exceptions.IllegalValueException;
import teletubbies.commons.exceptions.LatestVersionException;
import teletubbies.commons.exceptions.UserRoleSetException;
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
    void setUserProfile(UserProfile userProfile) throws UserRoleSetException;

    /**
     * Returns the user profile.
     */
    UserProfile getUserProfile();

    /**
     * Returns user's role
     */
    Role getUserRole();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Adds a listener.
     */
    void addListener(InvalidationListener listener);

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
     * Returns true if a person with the same name {@code person} exists in the address book.
     */
    boolean hasName(Person person);

    /**
     * Returns true if a person with the same phone number as {@code person} exists in the address book.
     */
    boolean hasPhoneNumber(Person person);

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

    /** Returns a list of persons from indices in a filtered person list */
    List<Person> getPersonsFromRange(Range range) throws IllegalValueException;

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
     * Replaces the given person in the address book.
     * @param person
     */
    void mergePerson(Person person);

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

    /**
     * Get the next command entered if it exists
     *
     * @return next command entered
     * @throws LatestVersionException if already looking at the
     * latest command
     */
    String getNextCommand() throws LatestVersionException;

    /**
     * Get the previous command entered if it exists
     *
     * @return previous command entered
     * @throws EarliestVersionException if already looking at
     * the earliest command
     */
    String getPreviousCommand() throws EarliestVersionException;

    //@@author yamidark
    //Reused from https://github.com/se-edu/addressbook-level4/blob/master/src/main/java/seedu/address/model/Model.java
    // with minor modifications
    /**
     * Returns true if the model has previous address book states to restore.
     */
    boolean canUndoAddressBook();

    /**
     * Returns true if the model has undone address book states to restore.
     */
    boolean canRedoAddressBook();

    /**
     * Restores the model's address book to its previous state.
     */
    void undoAddressBook() throws EarliestVersionException;

    /**
     * Restores the model's address book to its previously undone state.
     */
    void redoAddressBook() throws LatestVersionException;

    /**
     * Saves the current address book state for undo/redo.
     */
    void commitAddressBook();
    //@@author yamidark

}
