package teletubbies.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import teletubbies.commons.core.LogsCenter;
import teletubbies.commons.exceptions.DataConversionException;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.Model;
import teletubbies.model.ReadOnlyAddressBook;
import teletubbies.storage.JsonAddressBookStorage;
import teletubbies.ui.MainWindow;

/**
 * Imports new file of contacts specified by user and replaces address book.
 */
public class ImportCommand extends Command {
    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports contacts from selected file.\n"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_SUCCESS = "Contacts imported successfully.";
    public static final String MESSAGE_FILE_NOT_FOUND = "Data file not found. Please try again.";
    public static final String MESSAGE_INCORRECT_FORMAT = "Data file not in the correct format.";

    private final Logger logger = LogsCenter.getLogger(getClass()); // TODO make a singleton logger or something

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.cancelPendingExport();

        return new CommandResult(MESSAGE_SUCCESS, CommandResult.UiEffect.IMPORT, mainWindow -> {
            try {
                File fileToImport = mainWindow.handleFileChooser("Import Contacts File",
                        MainWindow.FileSelectType.OPEN);
                requireNonNull(fileToImport);

                Path filePath = fileToImport.toPath();

                Optional<ReadOnlyAddressBook> addressBookOptional = new JsonAddressBookStorage(filePath)
                        .readAddressBook();
                if (addressBookOptional.isEmpty()) {
                    throw new CommandException(MESSAGE_FILE_NOT_FOUND);
                }
                ReadOnlyAddressBook newContacts = addressBookOptional.get();
                model.setAddressBook(newContacts);
                model.commitAddressBook();
                logger.info("Imported contacts from " + filePath);
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
        if (!(other instanceof ImportCommand)) {
            return false;
        }
        return true;
    }
}
