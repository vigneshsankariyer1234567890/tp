package teletubbies.logic.commands;

import javafx.collections.ObservableList;
import teletubbies.commons.core.LogsCenter;
import teletubbies.commons.exceptions.DataConversionException;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.Model;
import teletubbies.model.ReadOnlyAddressBook;
import teletubbies.model.person.Person;
import teletubbies.storage.JsonAddressBookStorage;
import teletubbies.ui.MainWindow;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

public class MergeCommand extends Command {
    public static final String COMMAND_WORD = "merge";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Merges contacts from selected file with existing contacts.\n"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_SUCCESS = "Contacts merged successfully.";
    public static final String MESSAGE_FILE_NOT_FOUND = "Data file not found. Please try again.";
    public static final String MESSAGE_INCORRECT_FORMAT = "Data file not in the correct format.";

    private final Logger logger = LogsCenter.getLogger(getClass()); // TODO make a singleton logger or something

    @Override
    public CommandResult execute(Model model) throws CommandException {
        return new CommandResult(MESSAGE_SUCCESS, CommandResult.UiEffect.MERGE, mainWindow -> {
            try {
                File fileToImport = mainWindow.handleFileChooser("Merge Contacts File",
                        MainWindow.FileSelectType.OPEN);
                requireNonNull(fileToImport);

                Path filePath = fileToImport.toPath();

                Optional<ReadOnlyAddressBook> addressBookOptional = new JsonAddressBookStorage(filePath)
                        .readAddressBook();
                if (addressBookOptional.isEmpty()) {
                    throw new CommandException(MESSAGE_FILE_NOT_FOUND);
                }
                ReadOnlyAddressBook addressBookToMerge = addressBookOptional.get();
                ObservableList<Person> personsToMerge = addressBookToMerge.getPersonList(); 
                personsToMerge.forEach(person -> );
                logger.info("Merged contacts from " + filePath);
            } catch (DataConversionException e) {
                throw new CommandException(MESSAGE_INCORRECT_FORMAT);
            } catch (NullPointerException e) {
                throw new CommandException(MESSAGE_FILE_NOT_FOUND);
            }
        });
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MergeCommand)) {
            return false;
        }
        return true;
    }
}
