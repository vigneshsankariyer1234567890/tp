package teletubbies.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import teletubbies.commons.core.GuiSettings;
import teletubbies.commons.core.LogsCenter;
import teletubbies.commons.core.Range;
import teletubbies.commons.core.UserProfile;
import teletubbies.commons.core.UserProfile.Role;
import teletubbies.commons.core.index.Index;
import teletubbies.commons.exceptions.EarliestVersionException;
import teletubbies.commons.exceptions.IllegalValueException;
import teletubbies.commons.exceptions.LatestVersionException;
import teletubbies.commons.util.CollectionUtil;
import teletubbies.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedAddressBook versionedAddressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private boolean isAwaitingExportConfirmation;
    private final CommandInputHistory inputHistory;
    private boolean firstUpArrowClicked = false;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        super();
        CollectionUtil.requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.versionedAddressBook = new VersionedAddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.versionedAddressBook.getPersonList());
        this.inputHistory = new CommandInputHistory();
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public void setUserProfile(UserProfile userProfile) {
        requireNonNull(userProfile);
        this.userPrefs.setUserProfile(userProfile);
    }

    @Override
    public UserProfile getUserProfile() {
        return userPrefs.getUserProfile();
    }

    @Override
    public Role getUserRole() {
        return userPrefs.getUserRole();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.versionedAddressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return versionedAddressBook;
    }

    @Override
    public boolean hasName(Person person) {
        requireNonNull(person);
        return versionedAddressBook.hasName(person);
    }

    @Override
    public boolean hasPhoneNumber(Person person) {
        requireNonNull(person);
        return versionedAddressBook.hasPhoneNumber(person);
    }

    @Override
    public void deletePerson(Person target) {
        versionedAddressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        versionedAddressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        CollectionUtil.requireAllNonNull(target, editedPerson);
        versionedAddressBook.setPerson(target, editedPerson);
    }

    @Override
    public void mergePerson(Person person) {
        versionedAddressBook.mergePerson(person);
    }

    @Override
    public void updateExportList(List<Person> filteredPersonList) {
        // Since VersionedAddressBook stores all states, we can just store the new exportable list as a new state in
        // the VersionedAddressBook.
        isAwaitingExportConfirmation = true;
        this.versionedAddressBook.setPersons(filteredPersonList);
    }

    @Override
    public boolean isAwaitingExportConfirmation() {
        return isAwaitingExportConfirmation;
    }

    @Override
    public AddressBook getExportAddressBook() {
        // Steps:
        // 1. Obtain the filtered list which will be the most recent addressBook state.
        // 2. Undo the VersionedAddressBook to its previous state before the export command.
        // 3. Commit the VersionedAddressBook to remove the filtered list.
        // 4. Return the AddressBook to be exported.
        AddressBook toExport = new AddressBook(versionedAddressBook);
        try {
            this.versionedAddressBook.undo();
            this.versionedAddressBook.commit();
        } catch (EarliestVersionException e) {
            // Undo will always work as VersionedAddressBook must store at least 2 states: the original AddressBook
            // and the filtered addressBook.
            logExportIssuesWithUndo(e);
        }
        isAwaitingExportConfirmation = false;
        return toExport;
    }

    @Override
    public void cancelPendingExport() {
        if (isAwaitingExportConfirmation) {
            try {
                this.versionedAddressBook.undo();
                versionedAddressBook.commit();
            } catch (EarliestVersionException e) {
                logExportIssuesWithUndo(e);
            }
            isAwaitingExportConfirmation = false;
        }
    }

    private void logExportIssuesWithUndo(EarliestVersionException e) {
        logger.log(Level.SEVERE, e, () -> "VersionedAddressBook should be undoable at point of export and have at "
                + "least 2 states stored.");
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    public List<Person> getPersonsFromRange(Range range) throws IllegalValueException {
        requireNonNull(range);
        List<Index> rangeValues = range.getRangeValues();
        if (rangeValues.stream().anyMatch(i -> i.getZeroBased() >= filteredPersons.size())) {
            throw new IllegalValueException(Range.MESSAGE_ILLEGAL_RANGE);
        }
        return rangeValues.stream()
                .map(i -> filteredPersons.get(i.getZeroBased()))
                .collect(Collectors.toList());
    }


    //=========== Undo/Redo =============================================================

    @Override
    public boolean canUndoAddressBook() {
        return versionedAddressBook.canUndo();
    }

    @Override
    public boolean canRedoAddressBook() {
        return versionedAddressBook.canRedo();
    }

    @Override
    public void undoAddressBook() throws EarliestVersionException {
        versionedAddressBook.undo();
    }

    @Override
    public void redoAddressBook() throws LatestVersionException {
        versionedAddressBook.redo();
    }

    @Override
    public void commitAddressBook() {
        versionedAddressBook.commit();
    }

    //=========== InputHistory Accessors and Modifiers ======================================================

    @Override
    public void addCommandInput(String textInput) {
        firstUpArrowClicked = false;
        inputHistory.addCommandInput(textInput);
    }

    @Override
    public List<String> getChronologicallyAscendingHistory() {
        return inputHistory.getChronologicallyAscendingHistory();
    }

    @Override
    public List<String> getChronologicallyDescendingHistory() {
        return inputHistory.getChronologicallyDescendingHistory();
    }

    @Override
    public String getNextCommand() throws LatestVersionException {
        inputHistory.next();
        return inputHistory.peek();
    }

    @Override
    public String getPreviousCommand() throws EarliestVersionException {
        if (!firstUpArrowClicked) {
            firstUpArrowClicked = true;
        } else {
            inputHistory.previous();
        }
        return inputHistory.peek();
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedAddressBook.equals(other.versionedAddressBook)
                && userPrefs.equals(other.userPrefs)
                && filteredPersons.equals(other.filteredPersons)
                && inputHistory.equals(other.inputHistory);
    }
}
