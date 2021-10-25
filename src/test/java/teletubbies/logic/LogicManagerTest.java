package teletubbies.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static teletubbies.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static teletubbies.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_PHONE_NUMBER;
import static teletubbies.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import teletubbies.logic.commands.AddCommand;
import teletubbies.logic.commands.CommandResult;
import teletubbies.logic.commands.CommandTestUtil;
import teletubbies.logic.commands.ListCommand;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.logic.parser.exceptions.ParseException;
import teletubbies.model.Model;
import teletubbies.model.ModelManager;
import teletubbies.model.ReadOnlyAddressBook;
import teletubbies.model.person.Person;
import teletubbies.storage.JsonAddressBookStorage;
import teletubbies.storage.JsonUserPrefsStorage;
import teletubbies.storage.StorageManager;
import teletubbies.testutil.Assert;
import teletubbies.testutil.PersonBuilder;
import teletubbies.testutil.TypicalPersons;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy exception");
    private static final String INVALID_COMMAND = "uicfhmowqewca";
    private static final String DELETE_COMMAND_WITH_INVALID_PHONE = "delete --p 000";
    private static final String DELETE_COMMAND_WITH_INVALID_INDEX = "delete --i 20";
    private static final String VALID_COMMAND = ListCommand.COMMAND_WORD;

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Model trackedModel = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        trackedModel.addCommandInput(INVALID_COMMAND);
        assertParseException(INVALID_COMMAND, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandPhoneExecutionError_throwsCommandException() {
        trackedModel.addCommandInput(DELETE_COMMAND_WITH_INVALID_PHONE);
        assertCommandException(DELETE_COMMAND_WITH_INVALID_PHONE, MESSAGE_INVALID_PERSON_DISPLAYED_PHONE_NUMBER);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        trackedModel.addCommandInput(DELETE_COMMAND_WITH_INVALID_INDEX);
        assertCommandException(DELETE_COMMAND_WITH_INVALID_INDEX, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        trackedModel.addCommandInput(VALID_COMMAND);
        assertCommandSuccess(VALID_COMMAND, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        // Setup LogicManager with JsonAddressBookIoExceptionThrowingStub
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookIoExceptionThrowingStub(temporaryFolder.resolve("ioExceptionAddressBook.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ioExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);

        // Execute add command
        String addCommand = AddCommand.COMMAND_WORD + CommandTestUtil.NAME_DESC_AMY + CommandTestUtil.PHONE_DESC_AMY
                + CommandTestUtil.EMAIL_DESC_AMY + CommandTestUtil.ADDRESS_DESC_AMY;
        Person expectedPerson = new PersonBuilder(TypicalPersons.AMY).withTags().build();
        Model expectedModel = trackedModel;
        expectedModel.addPerson(expectedPerson);
        expectedModel.addCommandInput(addCommand);
        String expectedMessage = LogicManager.FILE_OPS_ERROR_MESSAGE + DUMMY_IO_EXCEPTION;
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        Assert.assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredPersonList().remove(0));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage, trackedModel);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage, trackedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        Assert.assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * A stub class to throw an {@code IOException} when the save method is called.
     */
    private static class JsonAddressBookIoExceptionThrowingStub extends JsonAddressBookStorage {
        private JsonAddressBookIoExceptionThrowingStub(Path filePath) {
            super(filePath);
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
            throw DUMMY_IO_EXCEPTION;
        }
    }
}
