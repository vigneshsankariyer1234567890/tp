package teletubbies.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Logger;

import teletubbies.commons.core.LogsCenter;
import teletubbies.commons.core.Messages;
import teletubbies.commons.util.StringUtil;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.AddressBook;
import teletubbies.model.Model;
import teletubbies.storage.JsonAddressBookStorage;
import teletubbies.ui.MainWindow;

/**
 * Exports selected persons to file in path specified by user after confirmation.
 */
public class ConfirmExportCommand extends Command {
    public static final String COMMAND_WORD = "y";
    public static final String MESSAGE_SUCCESS = "Contacts exported successfully.";

    private final Logger logger = LogsCenter.getLogger(getClass()); // TODO make a singleton logger or something

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (!model.isAwaitingExportConfirmation()) {
            throw new CommandException(Messages.MESSAGE_UNKNOWN_COMMAND);
        }

        return new CommandResult(MESSAGE_SUCCESS, CommandResult.UiEffect.EXPORT, mainWindow -> {
            File fileToSave = mainWindow.handleFileChooser("Export Contacts File",
                    MainWindow.FileSelectType.SAVE);
            requireNonNull(fileToSave);

            String pathString = includeDotJson(fileToSave.getPath());
            AddressBook ab = model.getExportAddressBook();
            saveAddressBookToPath(ab, pathString);
        });
    }

    /**
     * Saves given address book to given path
     *
     * @param ab AddressBook to save
     * @param pathString Path string for saving
     * @throws CommandException
     */
    public void saveAddressBookToPath(AddressBook ab, String pathString) throws CommandException {
        try {
            new JsonAddressBookStorage(Paths.get(pathString)).saveAddressBook(ab);
        } catch (IOException ioe) {
            logger.warning("Failed to save contacts file : " + StringUtil.getDetails(ioe));
            throw new CommandException(ioe.getMessage());
        }
    }

    /**
     * Add a '.json' to the end of filename if
     * not included already
     *
     * @param path path with or without .json
     * @return path with .json
     */
    public String includeDotJson(String path) {
        if (!path.endsWith(".json")) {
            path += ".json";
        }
        return path;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ConfirmExportCommand)) {
            return false;
        }
        return true;
    }
}
