package teletubbies.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleObjectProperty;

public class InvalidationListenerManagerTest {
    private SimpleObjectProperty<Object> dummyObservable;
    private InvalidationListenerManager invalidationListenerManager;

    @BeforeEach
    public void setUp() {
        dummyObservable = new SimpleObjectProperty<>();
        invalidationListenerManager = new InvalidationListenerManager();
    }

    @Test
    public void addListenerOnce_success() {
        final int[] counterContainer = {0};
        invalidationListenerManager.addListener(o -> {
            assertEquals(dummyObservable, o);
            counterContainer[0]++;
        });
        invalidationListenerManager.callListeners(dummyObservable);
        assertEquals(1, counterContainer[0]);
    }

    @Test
    public void addListenerMoreThanOnce_success() {
        final int[] counterContainer = {0};
        int countOfTimes = 10;
        InvalidationListener invalidationListener = observable -> counterContainer[0]++;
        for (int i = 0; i < countOfTimes; i++) {
            invalidationListenerManager.addListener(invalidationListener);
        }
        invalidationListenerManager.callListeners(dummyObservable);
        assertEquals(countOfTimes, counterContainer[0]);
    }

    @Test
    public void removeListenerOnce_success() {
        final int[] counterContainer = {0};
        InvalidationListener invalidationListener = observable -> counterContainer[0]++;
        invalidationListenerManager.addListener(invalidationListener);
        invalidationListenerManager.callListeners(dummyObservable);
        assertEquals(1, counterContainer[0]);
        invalidationListenerManager.removeListener(invalidationListener);
        invalidationListenerManager.callListeners(dummyObservable);
        assertEquals(1, counterContainer[0]);
    }

    @Test
    public void addListenerMultipleTimesAndRemoveOnce_multipleCall() {
        final int[] counterContainer = {0};
        InvalidationListener invalidationListener = observable -> counterContainer[0]++;
        int countOfTimes = 3;
        for (int i = 0; i < countOfTimes; i++) {
            invalidationListenerManager.addListener(invalidationListener);
        }
        invalidationListenerManager.removeListener(invalidationListener);
        invalidationListenerManager.callListeners(dummyObservable);
        assertEquals(countOfTimes - 1, counterContainer[0]);
    }
}
