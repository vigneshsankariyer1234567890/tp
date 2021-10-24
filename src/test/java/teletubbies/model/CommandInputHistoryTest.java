package teletubbies.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import teletubbies.commons.exceptions.EarliestVersionException;
import teletubbies.commons.exceptions.LatestVersionException;

public class CommandInputHistoryTest {
    private final List<String> inputs = List.of("a", "b", "delete", "hello", "bye", "add", "edit 4", "exit");

    @Test
    public void addToCommandInputHistoryAndCompare_success() {
        CommandInputHistory inputHistory = new CommandInputHistory();
        for (String s: inputs) {
            inputHistory.addCommandInput(s);
        }
        List<String> historyList = inputHistory.getChronologicallyAscendingHistory();
        assertEquals(inputs, historyList);
    }

    @Test
    public void addToCommandInputHistoryAndCompareDescending_success() {
        CommandInputHistory inputHistory = new CommandInputHistory();
        for (String s: inputs) {
            inputHistory.addCommandInput(s);
        }

        List<String> expected = new ArrayList<>(List.copyOf(inputs));
        Collections.reverse(expected);

        assertEquals(expected, inputHistory.getChronologicallyDescendingHistory());
    }

    @Test
    public void undoAndCheckFullList_success() throws EarliestVersionException {
        CommandInputHistory inputHistory = new CommandInputHistory();
        for (String s: inputs) {
            inputHistory.addCommandInput(s);
        }
        while (!inputHistory.isEarliest()) {
            inputHistory.previous();
        }
        List<String> fullHistoryList = inputHistory.getChronologicallyAscendingHistory();
        assertEquals(inputs, fullHistoryList);
    }

    @Test
    public void undoAddAndCheck_success() throws EarliestVersionException {
        CommandInputHistory inputHistory = new CommandInputHistory();
        List<String> targetList = new ArrayList<>(inputs);
        targetList.add(inputs.get(0));
        for (String s: inputs) {
            inputHistory.addCommandInput(s);
        }
        while (!inputHistory.isEarliest()) {
            inputHistory.previous();
        }
        String first = inputHistory.peek();
        inputHistory.addCommandInput(first);
        List<String> fullHistoryList = inputHistory.getChronologicallyAscendingHistory();
        assertEquals(targetList, fullHistoryList);
    }

    @Test
    public void addNull_failure() {
        CommandInputHistory inputHistory = new CommandInputHistory();
        assertThrows(NullPointerException.class, () -> inputHistory.addCommandInput(null));
    }

    @Test
    public void getEarliestVersionRevertBackAndAddTest_success() throws EarliestVersionException,
            LatestVersionException {
        CommandInputHistory inputHistory = new CommandInputHistory();
        List<String> targetList = new ArrayList<>(inputs);
        int randomPosition = (int) (Math.random() * inputs.size());
        targetList.add(inputs.get(randomPosition));

        for (String s: inputs) {
            inputHistory.addCommandInput(s);
        }

        while (!inputHistory.isEarliest()) {
            inputHistory.previous();
        }

        int count = 0;
        while (count < randomPosition) {
            inputHistory.next();
            count++;
        }

        String first = inputHistory.peek();
        assertEquals(inputs.get(randomPosition), first);

        inputHistory.addCommandInput(first);
        List<String> fullHistoryList = inputHistory.getChronologicallyAscendingHistory();
        assertEquals(targetList, fullHistoryList);
    }

    @Test
    public void undoRedoAndCheckPeekTest_success() throws EarliestVersionException,
            LatestVersionException {
        CommandInputHistory inputHistory = new CommandInputHistory();
        for (String s: inputs) {
            inputHistory.addCommandInput(s);
        }
        while (!inputHistory.isEarliest()) {
            inputHistory.previous();
        }
        while (!inputHistory.isLatest()) {
            inputHistory.next();
        }
        String latest = inputHistory.peek();
        assertEquals(inputs.get(inputs.size() - 1), latest);
    }

    @Test
    public void isEmpty_success() {
        CommandInputHistory inputHistory = new CommandInputHistory();
        assertTrue(inputHistory.isEmpty());
        inputHistory.addCommandInput("");
        assertFalse(inputHistory.isEmpty());
    }

    @Test
    public void equalsTest_success() throws EarliestVersionException, LatestVersionException {
        CommandInputHistory inputHistory = new CommandInputHistory();
        for (String s: inputs) {
            inputHistory.addCommandInput(s);
        }

        // same values -> true
        assertEquals(inputHistory, inputHistory);

        CommandInputHistory copyOfInputHistory = new CommandInputHistory();
        for (String s: inputs) {
            copyOfInputHistory.addCommandInput(s);
        }

        // same values -> true
        assertEquals(inputHistory, copyOfInputHistory);

        // different pointer -> false
        copyOfInputHistory.previous();
        assertNotEquals(inputHistory, copyOfInputHistory);

        // same values -> true
        copyOfInputHistory.next();
        assertEquals(inputHistory, copyOfInputHistory);

        // same values -> true
        while (!copyOfInputHistory.isEarliest()) {
            copyOfInputHistory.previous();
        }

        copyOfInputHistory.addCommandInput("in");
        inputHistory.addCommandInput("in");

        assertEquals(inputHistory, copyOfInputHistory);
    }

}
