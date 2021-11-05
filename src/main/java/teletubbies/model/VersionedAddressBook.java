package teletubbies.model;

import java.util.List;

import teletubbies.commons.core.HistoryManager;
import teletubbies.commons.exceptions.EarliestAddressBookVersionException;
import teletubbies.commons.exceptions.EarliestVersionException;
import teletubbies.commons.exceptions.LatestAddressBookVersionException;
import teletubbies.commons.exceptions.LatestVersionException;

/**
 * A data structure to encapsulate the logic needed to store previous AddressBook states using HistoryManager.
 */
public class VersionedAddressBook extends AddressBook {
    private HistoryManager<ReadOnlyAddressBook> addressBookHistoryManager;

    /**
     * Copies over initial state and initialises HistoryManager with initial state
     */
    public VersionedAddressBook(ReadOnlyAddressBook initialState) {
        super(initialState);
        addressBookHistoryManager = new HistoryManager<>(List.of(new AddressBook(initialState)));
    }

    /**
     * Factory method to produce a new VersionedAddressBook based on a list of address book states.
     */
    protected static VersionedAddressBook of(ReadOnlyAddressBook... addressBookStates)
            throws EmptyAddressBookStateListException {
        if (addressBookStates.length == 0) {
            throw new EmptyAddressBookStateListException();
        }
        VersionedAddressBook result = new VersionedAddressBook(addressBookStates[0]);
        for (int i = 1; i < addressBookStates.length; i++) {
            result.resetData(addressBookStates[i]);
            result.commitCurrentStateAndSave();
        }
        return result;
    }

    /**
     * Factory method to produce a new VersionedAddressBook based on a list of address book states.
     */
    protected static VersionedAddressBook of(List<ReadOnlyAddressBook> addressBookStates)
            throws EmptyAddressBookStateListException {
        if (addressBookStates.isEmpty()) {
            throw new EmptyAddressBookStateListException();
        }
        VersionedAddressBook result = new VersionedAddressBook(addressBookStates.get(0));
        for (int i = 1; i < addressBookStates.size(); i++) {
            result.resetData(addressBookStates.get(i));
            result.commitCurrentStateAndSave();
        }
        return result;
    }

    /**
     * Copies over state and persists state to {@code HistoryManager} using {@code HistoryManager#commitAndPush}.
     */
    public void commitCurrentStateAndSave() {
        AddressBook copyOfAddressBook = new AddressBook(this);
        setAddressBookHistoryManager(addressBookHistoryManager.commitAndPush(copyOfAddressBook));
        indicateModified();
    }

    /**
     * Clears the {@code HistoryManager} after pointer.
     */
    public void commitWithoutSavingCurrentState() {
        setAddressBookHistoryManager(HistoryManager.clearedCopy(addressBookHistoryManager));
        indicateModified();
    }

    private void setAddressBookHistoryManager(HistoryManager<ReadOnlyAddressBook> newHistoryManager) {
        this.addressBookHistoryManager = newHistoryManager;
    }

    /**
     * Undoes state and resets AddressBook to {@code HistoryManager#peek}
     * @throws EarliestVersionException if it is the earliest version
     */
    public void undo() throws EarliestVersionException {
        if (!canUndo()) {
            throw new EarliestAddressBookVersionException();
        }
        addressBookHistoryManager.undo();
        resetData(addressBookHistoryManager.peek());
    }

    /**
     * Redoes state and resets AddressBook to {@code HistoryManager#peek}
     * @throws LatestVersionException if it is the latest version
     */
    public void redo() throws LatestVersionException {
        if (!canRedo()) {
            throw new LatestAddressBookVersionException();
        }
        addressBookHistoryManager.redo();
        resetData(addressBookHistoryManager.peek());
    }

    /**
     * Verifies that the state is undoable.
     */
    public boolean canUndo() {
        return addressBookHistoryManager.isUndoable();
    }

    /**
     * Verifies that the state is redoable.
     */
    public boolean canRedo() {
        return addressBookHistoryManager.isRedoable();
    }

    ReadOnlyAddressBook getMostRecentReadOnlyAddressBook() {
        return addressBookHistoryManager.peek();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof VersionedAddressBook)) {
            return false;
        }

        VersionedAddressBook other = (VersionedAddressBook) obj;

        return super.equals(other)
                && addressBookHistoryManager.equals(other.addressBookHistoryManager);
    }

    /**
     * An exception that will be thrown if no states are provided in the given state list
     */
    public static class EmptyAddressBookStateListException extends Exception {
        private static final String MESSAGE = "There must be at least one state in the given state list.";

        public EmptyAddressBookStateListException() {
            super(MESSAGE);
        }
    }
}
