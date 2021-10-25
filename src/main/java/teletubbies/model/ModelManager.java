package teletubbies.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import teletubbies.commons.core.GuiSettings;
import teletubbies.commons.core.LogsCenter;
import teletubbies.commons.core.UserProfile;
import teletubbies.commons.util.CollectionUtil;
import teletubbies.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private boolean isAwaitingExportConfirmation;
    private AddressBook addressBookCopy;
    private final CommandInputHistory inputHistory;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        super();
        CollectionUtil.requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
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
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public boolean hasPhoneNumber(Person person) {
        requireNonNull(person);
        return addressBook.hasPhoneNumber(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        CollectionUtil.requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);
    }

    @Override
    public void updateExportList(List<Person> filteredPersonList) {
        isAwaitingExportConfirmation = true;
        addressBookCopy = new AddressBook(addressBook);
        this.addressBook.setPersons(filteredPersonList);
    }

    @Override
    public boolean isAwaitingExportConfirmation() {
        return isAwaitingExportConfirmation;
    }

    @Override
    public AddressBook getExportAddressBook() {
        AddressBook toExport = new AddressBook(addressBook);
        this.addressBook.resetData(addressBookCopy);
        addressBookCopy = null;
        isAwaitingExportConfirmation = false;
        return toExport;
    }

    @Override
    public void cancelPendingExport() {
        if (isAwaitingExportConfirmation) {
            this.addressBook.resetData(addressBookCopy);
            addressBookCopy = null;
            isAwaitingExportConfirmation = false;
        }
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
        return addressBook.equals(other.addressBook)
                && userPrefs.equals(other.userPrefs)
                && filteredPersons.equals(other.filteredPersons)
                && inputHistory.equals(other.inputHistory);
    }

    //=========== InputHistory accessors and modifiers ======================================================

    @Override
    public void addCommandInput(String textInput) {
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

}
