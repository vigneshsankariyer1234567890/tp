package teletubbies.commons.util;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

/**
 * Manages list of {@link InvalidationListener}. This was adapted from the implementation of Se-edu AB4.
 */
public class InvalidationListenerManager {
    private final ArrayList<InvalidationListener> listeners = new ArrayList<>();

    /**
     * Calls {@link InvalidationListener#invalidated(Observable)} for each listener in listeners.
     *
     * @param observable The {@code Observable} to be invalidated by all listeners.
     */
    public void callListeners(Observable observable) {
        ArrayList<InvalidationListener> copyOfListeners = new ArrayList<>(listeners);

        for (InvalidationListener listener: copyOfListeners) {
            listener.invalidated(observable);
        }
    }

    /**
     * Adds {@code listener} to the list of listeners.
     * If the same listener is added more that once, then it will be notified more than once.
     */
    public void addListener(InvalidationListener listener) {
        requireNonNull(listener);
        listeners.add(listener);
    }

    /**
     * Removes {@code listener} from the list of listeners.
     * If the given listener was not previously added, then this method call is a no-op.
     * If the given listener was added more than once, then only the first occurrence in the list will be removed.
     */
    public void removeListener(InvalidationListener listener) {
        requireNonNull(listener);
        listeners.remove(listener);
    }
}
