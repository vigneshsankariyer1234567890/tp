package teletubbies.commons.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import teletubbies.commons.exceptions.EarliestVersionException;
import teletubbies.commons.exceptions.LatestVersionException;
import teletubbies.logic.commands.CommandTestUtil;
import teletubbies.model.HistoryManager;

public class HistoryManagerTest {

    private final String testString = "";
    private final List<String> historyOfStrings = CommandTestUtil.VALID_COMMAND_HISTORY_INPUTS;

    @Test
    public void emptyConstructorTest_success() {
        HistoryManager<String> historyManager = new HistoryManager<>();
        assertTrue(historyManager.isEmpty());
        List<String> historyList = historyManager.historyList();
        assertEquals(0, historyList.size());
    }

    @Test
    public void listConstructorTest_success() {
        HistoryManager<String> historyManager = new HistoryManager<>(historyOfStrings);
        assertFalse(historyManager.isEmpty());
        List<String> historyList = historyManager.historyList();
        assertEquals(historyList, historyOfStrings);
    }

    @Test
    public void isUndoableTest_success() {
        HistoryManager<String> historyManager = new HistoryManager<>(historyOfStrings);
        assertTrue(historyManager.isUndoable());
        HistoryManager<String> oneState = new HistoryManager<>(List.of("hello"));
        assertFalse(oneState.isUndoable());
    }

    @Test
    public void isRedoableTest_success() throws EarliestVersionException {
        HistoryManager<String> historyManager = new HistoryManager<>(historyOfStrings);
        assertFalse(historyManager.isRedoable());
        String current = historyManager.peek();
        historyManager.undo();
        String previous = historyManager.peek();
        assertEquals("hello", current);
        assertEquals("hell", previous);
        assertTrue(historyManager.isRedoable());
    }

    @Test
    public void undoExceptionThrows_success() {
        HistoryManager<String> historyManager = new HistoryManager<>(List.of(testString));
        assertThrows(EarliestVersionException.class, historyManager::undo);
        assertEquals(historyManager.peek(), testString);
    }

    @Test
    public void isUndoableCountTest() throws EarliestVersionException {
        int count = 0;
        int secondLastIndex = historyOfStrings.size() - 2;
        HistoryManager<String> historyManager = new HistoryManager<>(historyOfStrings);
        while (historyManager.isUndoable()) {
            String current = historyOfStrings.get(secondLastIndex - count);
            historyManager.undo();
            String currentHM = historyManager.peek();
            assertEquals(current, currentHM);
            count++;
        }
        assertEquals(historyOfStrings.size() - 1, count);
    }

    @Test
    public void isRedoableCountTest() throws LatestVersionException, EarliestVersionException {
        int undoCount = 0;
        int secondLastIndex = historyOfStrings.size() - 2;
        HistoryManager<String> historyManager = new HistoryManager<>(historyOfStrings);
        while (historyManager.isUndoable()) {
            String current = historyOfStrings.get(secondLastIndex - undoCount);
            historyManager.undo();
            String currentHM = historyManager.peek();
            assertEquals(current, currentHM);
            undoCount++;
        }
        assertEquals(historyOfStrings.size() - 1, undoCount);

        int redoCount = 0;
        while (historyManager.isRedoable()) {
            String current = historyOfStrings.get(redoCount + 1);
            historyManager.redo();
            String currentHM = historyManager.peek();
            assertEquals(current, currentHM);
            redoCount++;
        }
        assertEquals(historyOfStrings.size() - 1, redoCount);
    }

    @Test
    public void clearedCopyTest() throws EarliestVersionException {
        int timesToUndo = historyOfStrings.size() / 2;
        HistoryManager<String> historyManager = new HistoryManager<>(historyOfStrings);
        int count = 0;
        while (count < timesToUndo) {
            historyManager.undo();
            count++;
        }
        HistoryManager<String> copied = HistoryManager.clearedCopy(historyManager);
        List<String> sublist = new ArrayList<>(historyOfStrings.subList(0, historyOfStrings.size() - timesToUndo));
        assertEquals(sublist, copied.historyList());
    }

    @Test
    public void commitAndPushTest() throws EarliestVersionException {
        HistoryManager<String> hm = new HistoryManager<>(historyOfStrings);
        int timesToUndo = historyOfStrings.size() / 2;
        List<String> targetSublist = new ArrayList<>(historyOfStrings.subList(0,
                historyOfStrings.size() - timesToUndo));
        String itemToAdd = "helloo";

        int count = timesToUndo;
        while (count > 0) {
            hm.undo();
            count--;
        }
        assertEquals(targetSublist, hm.historyList());
        assertTrue(hm.isRedoable());

        hm = hm.commitAndPush(itemToAdd);
        targetSublist.add(itemToAdd);

        assertEquals(targetSublist, hm.historyList());
        assertFalse(hm.isRedoable());
    }

    @Test
    public void commitAndPushEmptyHistoryManager_success() {
        HistoryManager<String> hm = new HistoryManager<>();
        hm = hm.commitAndPush(testString);
        assertEquals(hm.peek(), testString);
    }

    @Test
    public void resetFullHistoryTest_success() throws EarliestVersionException {
        HistoryManager<String> historyManager = new HistoryManager<>(historyOfStrings);
        int timesToUndo = (int) (Math.random() * historyOfStrings.size());
        while (timesToUndo > 0) {
            historyManager.undo();
            timesToUndo--;
        }
        historyManager.resetFullHistory();
        assertEquals(historyOfStrings, historyManager.historyList());
    }

    @Test
    public void equalsTest() throws EarliestVersionException, LatestVersionException {
        HistoryManager<String> historyManager = new HistoryManager<>(historyOfStrings);

        // same HistoryManager -> returns true
        assertEquals(historyManager, historyManager);

        // same values as original HistoryManager -> returns true
        HistoryManager<String> copyOfHistoryManager = new HistoryManager<>(historyOfStrings);
        assertEquals(historyManager, copyOfHistoryManager);

        // different pointer than original HistoryManager -> returns false
        copyOfHistoryManager.undo();
        assertNotEquals(historyManager, copyOfHistoryManager);

        // same values as original -> returns true
        copyOfHistoryManager.redo();
        assertEquals(historyManager, copyOfHistoryManager);

        // same values as original -> returns true
        String last = copyOfHistoryManager.peek();
        copyOfHistoryManager.undo();
        copyOfHistoryManager = copyOfHistoryManager.commitAndPush(last);
        assertEquals(historyManager, copyOfHistoryManager);
    }
}
