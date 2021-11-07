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
import teletubbies.logic.commands.uieffects.ExportUiConsumer;
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

    private final Logger logger = LogsCenter.getLogger(getClass());

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (!model.isAwaitingExportConfirmation()) {
            throw new CommandException(Messages.MESSAGE_UNKNOWN_COMMAND);
        }

        return new CommandResult(MESSAGE_SUCCESS, CommandResult.UiEffect.EXPORT, new ExportUiConsumer(model));
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
