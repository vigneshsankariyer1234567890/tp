package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.ui.MainWindow;

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
