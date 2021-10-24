package teletubbies.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import teletubbies.commons.core.GuiSettings;
import teletubbies.commons.core.UserProfile;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.Model;
import teletubbies.model.ReadOnlyAddressBook;
import teletubbies.model.ReadOnlyUserPrefs;
import teletubbies.model.person.Person;


public class HistoryCommandTest {
    private final List<String> validCommandInputs = CommandTestUtil.VALID_COMMAND_HISTORY_INPUTS;
    private final List<String> invalidCommandInputs = CommandTestUtil.INVALID_COMMAND_HISTORY_INPUTS;

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelStubWithWorkingCommandHistoryInputMethods();
    }

    @Test
    public void addValidCommandsAndExecuteTest_success() throws CommandException {
        for (String s: validCommandInputs) {
            model.addCommandInput(s);
        }
        HistoryCommand historyCommand = new HistoryCommand();
        CommandResult commandResult = historyCommand.execute(model);

        List<String> copyOfInputs = new ArrayList<>(validCommandInputs);
        Collections.reverse(copyOfInputs);
        String expectedResult = String.format(HistoryCommand.MESSAGE_SUCCESS, String.join("\n", copyOfInputs));

        assertEquals(expectedResult, commandResult.getFeedbackToUser());
    }

    @Test
    public void addInvalidCommandsAndExecuteTest_success() throws CommandException {
        for (String s: invalidCommandInputs) {
            model.addCommandInput(s);
        }
        HistoryCommand historyCommand = new HistoryCommand();
        CommandResult commandResult = historyCommand.execute(model);

        List<String> copyOfInputs = new ArrayList<>(invalidCommandInputs);
        Collections.reverse(copyOfInputs);
        String expectedResult = String.format(HistoryCommand.MESSAGE_SUCCESS, String.join("\n", copyOfInputs));

        assertEquals(expectedResult, commandResult.getFeedbackToUser());
    }

    /**
     * A Model Stub that only implements {@code CommandHistoryInput} methods.
     */
    private class ModelStubWithWorkingCommandHistoryInputMethods implements Model {

        private final List<String> commandInputHistory = new ArrayList<>();

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setUserProfile(UserProfile userProfile) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public UserProfile getUserProfile() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook addressBook) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addCommandInput(String textInput) {
            commandInputHistory.add(textInput);
        }

        @Override
        public List<String> getInputHistory() {
            return new ArrayList<>(commandInputHistory);
        }

        @Override
        public boolean canUndoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canRedoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void undoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void redoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitAddressBook() {
            throw new AssertionError("This method should not be called.");
        }
    }
}
