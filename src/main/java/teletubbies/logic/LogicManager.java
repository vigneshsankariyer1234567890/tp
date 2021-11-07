package teletubbies.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import teletubbies.commons.core.GuiSettings;
import teletubbies.commons.core.LogsCenter;
import teletubbies.commons.core.UserProfile;
import teletubbies.logic.commands.Command;
import teletubbies.logic.commands.CommandResult;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.logic.parser.exceptions.ParseException;
import teletubbies.model.Model;
import teletubbies.model.ReadOnlyAddressBook;
import teletubbies.model.person.Person;
import teletubbies.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final teletubbies.logic.parser.InputParser inputParser;

    //@@author pyokagan
    // Reused from
    // https://github.com/se-edu/addressbook-level4/blob/master/src/main/java/seedu/address/logic/LogicManager.java
    // with minor modifications
    private boolean isModified;
    //@@author pyokagan

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        inputParser = new teletubbies.logic.parser.InputParser();
        //@@author pyokagan
        // Adapted from
        // https://github.com/se-edu/addressbook-level4/blob/master/src/main/java/seedu/address/logic/LogicManager.java
        model.addListener(observable -> isModified = true);
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        model.addCommandInput(commandText);
        Command command = inputParser.parseCommand(commandText);
        CommandResult commandResult = command.execute(model);

        if (isModified && !model.isAwaitingExportConfirmation()) {
            logger.info("AddressBook was modified; saving to file.");
            try {
                storage.saveAddressBook(model.getAddressBook());
            } catch (IOException ioe) {
                throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
            }
        }
        isModified = false;
        return commandResult;
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public UserProfile getUserProfile() {
        return model.getUserProfile();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
