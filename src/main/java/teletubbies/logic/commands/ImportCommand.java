package teletubbies.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import teletubbies.commons.core.LogsCenter;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.logic.commands.uieffects.ImportUiConsumer;
import teletubbies.model.Model;

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

    private final Logger logger = LogsCenter.getLogger(getClass());

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.cancelPendingExport();

        return new CommandResult(MESSAGE_SUCCESS, CommandResult.UiEffect.IMPORT, new ImportUiConsumer(model));
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
