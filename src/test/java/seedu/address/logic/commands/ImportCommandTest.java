package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;

class ImportCommandTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonAddressBookStorageTest");

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void readAddressBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readAddressBook(null));
    }

    private java.util.Optional<ReadOnlyAddressBook> readAddressBook(String filePath) throws Exception {
        return new ImportCommand(filePath).readAddressBook(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readAddressBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataConversionException.class, () -> readAddressBook("notJsonFormatAddressBook.json"));
    }

    @Test
    public void readAddressBook_invalidPersonAddressBook_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readAddressBook("invalidPersonAddressBook.json"));
    }

    @Test
    public void execute_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ImportCommand(null).execute(model));
    }

    @Test
    public void execute_fileNotFound_throwCommandException() {
        assertThrows(CommandException.class, ImportCommand.MESSAGE_FILE_NOT_FOUND, () ->
                new ImportCommand("notAddressBook.json").execute(model));
    }

    @Test
    public void equals() {
        final ImportCommand importFirstCommand = new ImportCommand("first");
        final ImportCommand importSecondCommand = new ImportCommand("second");

        // same object -> returns true
        assertTrue(importFirstCommand.equals(importFirstCommand));

        // same values -> returns true
        ImportCommand importFirstCommandCopy = new ImportCommand("first");
        assertTrue(importFirstCommand.equals(importFirstCommandCopy));

        // different types -> returns false
        assertFalse(importFirstCommand.equals(1));

        // null -> returns false
        assertFalse(importFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(importFirstCommand.equals(importSecondCommand));
    }
}
