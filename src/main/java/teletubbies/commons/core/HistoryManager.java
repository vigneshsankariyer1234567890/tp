package teletubbies.commons.core;

import java.util.ArrayList;
import java.util.List;

import teletubbies.commons.exceptions.EarliestVersionException;
import teletubbies.commons.exceptions.EmptyHistoryManagerException;
import teletubbies.commons.exceptions.LatestVersionException;

/**
 * A stack-like data structure which will be used to store the history of states of any type T.
 * @param <T> The type of class which will be managed by HistoryManager.
 */
public class HistoryManager<T> {

    private final ArrayList<T> historyStack;
    private int stackPointer;

    /**
     * Creates an empty HistoryManager.
     */
    public HistoryManager() {
        this.historyStack = new ArrayList<>();
        this.stackPointer = -1;
    }

    /**
     * Creates a HistoryManager with a predefined historical list of items.
     * @param history which refer to a predefined historical list of items.
     */
    public HistoryManager(List<T> history) {
        this.historyStack = new ArrayList<>(history);
        this.stackPointer = historyStack.size() - 1;
    }

    /**
     * Checks if the HistoryManager can revert to an earlier version.
     */
    public boolean isUndoable() {
        return stackPointer > 0;
    }

    /**
     * Checks if the HistoryManager can be redone to a later version.
     */
    public boolean isRedoable() {
        return stackPointer < historyStack.size() - 1;
    }

    /**
     * Checks if the HistoryManager is empty.
     */
    public boolean isEmpty() {
        return historyStack.size() == 0 || stackPointer < 0;
    }

    /**
     * Returns a copy of the history of states in chronological order up to {@code stackPointer}.
     * @return copy of history up to {@code stackPointer}.
     */
    public List<T> historyList() {
        return new ArrayList<>(historyStack.subList(0, stackPointer + 1));
    }

    /**
     * Resets to the full history stored by {@code historyStack} by repointing {@code stackPointer} to the top of
     * {@code historyStack}.
     */
    public void resetFullHistory() {
        this.stackPointer = historyStack.size() - 1;
    }

    /**
     * Returns the current item in the stack that is referred to by {@code stackPointer}.
     * @return the current item referred to by {@code stackPointer} or null if empty.
     */
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        assert stackPointer >= 0;
        assert stackPointer < historyStack.size();
        return historyStack.get(stackPointer);
    }

    private void push(T item) {
        if (!isRedoable()) {
            historyStack.add(item);
        } else {
            historyStack.add(stackPointer + 1, item);
        }
        stackPointer++;
    }

    private T pop() throws EmptyHistoryManagerException {
        if (isEmpty()) {
            throw new EmptyHistoryManagerException();
        }
        T top = historyStack.get(stackPointer);
        stackPointer--;
        return top;
    }

    /**
     * Undoes the state to a previous version as stored by HistoryManager
     * @throws EarliestVersionException if HistoryManager is not undoable.
     */
    public void undo() throws EarliestVersionException {
        if (!isUndoable()) {
            throw new EarliestVersionException();
        }
        stackPointer--;
    }

    /**
     * Reverts the state to a later version as stored by HistoryManager
     * @throws LatestVersionException if HistoryManager is not redoable.
     */
    public void redo() throws LatestVersionException {
        if (!isRedoable()) {
            throw new LatestVersionException();
        }
        stackPointer++;
    }

    /**
     * Commits the current history as stored by HistoryManager and pushes new state to the top of the {@code
     * historyStack}. This ensures the immutability of historyStack and guarantees that if anything is added,
     * stackPointer will point to the latest version, at the top of the {@code historyStack}.
     * @param item The new state to be stored.
     * @return A new HistoryManager with an updated history.
     */
    public HistoryManager<T> commitAndPush(T item) {
        HistoryManager<T> copy = commit();
        copy.push(item);
        return copy;
    }

    private HistoryManager<T> commit() {
        return HistoryManager.clearedCopy(this);
    }

    /**
     * Static method that clears the history after stackPointer.
     * @param original the original HistoryManager
     * @param <U> the type that is stored by HistoryManager
     * @return A new HistoryManager that contains the history up to stackPointer.
     */
    public static <U> HistoryManager<U> clearedCopy(HistoryManager<U> original) {
        List<U> sublist = original.historyStack.subList(0, original.stackPointer + 1);
        return new HistoryManager<>(sublist);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof HistoryManager)) {
            return false;
        }

        HistoryManager<?> oth = (HistoryManager<?>) other;

        if (oth.stackPointer != this.stackPointer) {
            return false;
        }

        if (oth.historyStack.size() != this.historyStack.size()) {
            return false;
        }

        for (int i = 0; i < this.historyStack.size(); i++) {
            Object a = this.historyStack.get(i);
            Object b = oth.historyStack.get(i);
            if (!a.equals(b)) {
                return false;
            }
        }

        return true;
    }
}
