package teletubbies.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import teletubbies.commons.core.HistoryManager;
import teletubbies.commons.exceptions.EarliestVersionException;
import teletubbies.commons.exceptions.EmptyHistoryManagerException;
import teletubbies.commons.exceptions.LatestVersionException;

/**
 * A data structure to encapsulate the logic needed to store previous commands using HistoryManager.
 */
public class CommandInputHistory {
    private HistoryManager<String> inputHistory;

    public CommandInputHistory() {
        this.inputHistory = new HistoryManager<>();
    }

    /**
     * Returns the full history of inputs given to CommandInputHistory.
     * @return full list of inputs given to CommandInputHistory.
     */
    public List<String> getFullInputHistory() {
        inputHistory.resetFullHistory();
        return inputHistory.historyList();
    }

    /**
     * Adds an input to the top if the {@code HistoryManager} stack.
     * @param input to add to the top of the HistoryManager stack.
     */
    public void addCommandInput(String input) {
        requireNonNull(input);
        inputHistory.resetFullHistory();
        inputHistory = inputHistory.commitAndPush(input);
    }

    /**
     * Resets {@code HistoryManager} to a previous version.
     * @throws EarliestVersionException if pointing to the earliest version.
     */
    public void previous() throws EarliestVersionException {
        inputHistory.undo();
    }

    /**
     * Resets {@code HistoryManager} to a later version.
     * @throws LatestVersionException if pointing to the latest version.
     */
    public void next() throws LatestVersionException {
        inputHistory.redo();
    }

    /**
     * Returns current version that {@code HistoryManager} currently has a reference to.
     * @throws EmptyHistoryManagerException if inputHistory is empty.
     */
    public String peek() throws EmptyHistoryManagerException {
        return inputHistory.peek();
    }

    /**
     * Checks if inputHistory is pointing to the earliest command input.
     */
    public boolean isEarliest() {
        return !inputHistory.isUndoable();
    }

    /**
     * Checks if inputHistory is pointing to the latest command input.
     */
    public boolean isLatest() {
        return !inputHistory.isRedoable();
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof CommandInputHistory)) {
            return false;
        }

        CommandInputHistory oth = (CommandInputHistory) obj;
        return this.inputHistory.equals(oth.inputHistory);
    }

}
