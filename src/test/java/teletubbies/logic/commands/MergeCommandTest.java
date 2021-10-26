package teletubbies.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MergeCommandTest {

    @Test
    void execute() {
    }

    @Test
    void equals() {
        final MergeCommand mergeFirstCommand = new MergeCommand();
        final MergeCommand mergeSecondCommand = new MergeCommand();

        // same object -> returns true
        assertTrue(mergeFirstCommand.equals(mergeFirstCommand));

        // same values -> returns true
        assertTrue(mergeFirstCommand.equals(mergeSecondCommand));

        // different types -> returns false
        assertFalse(mergeFirstCommand.equals(1));

        // null -> returns false
        assertFalse(mergeFirstCommand.equals(null));
    }
}
