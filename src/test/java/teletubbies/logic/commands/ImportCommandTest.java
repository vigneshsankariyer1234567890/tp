package teletubbies.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ImportCommandTest {
    @Test
    public void equals() {
        final ImportCommand importFirstCommand = new ImportCommand();
        final ImportCommand importSecondCommand = new ImportCommand();

        // same object -> returns true
        assertTrue(importFirstCommand.equals(importFirstCommand));

        // same values -> returns true
        assertTrue(importFirstCommand.equals(importSecondCommand));

        // different types -> returns false
        assertFalse(importFirstCommand.equals(1));

        // null -> returns false
        assertFalse(importFirstCommand.equals(null));
    }
}
