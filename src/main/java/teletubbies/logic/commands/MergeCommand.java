package teletubbies.logic.commands;

import java.util.logging.Logger;

import teletubbies.commons.core.LogsCenter;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.logic.commands.uieffects.MergeUiConsumer;
import teletubbies.model.Model;

public class MergeCommand extends Command {
    public static final String COMMAND_WORD = "merge";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Merges contacts from selected file with existing "
            + "contacts.\nExample: " + COMMAND_WORD;
    public static final String MESSAGE_SUCCESS = "Contacts merged successfully.";
    public static final String MESSAGE_FILE_NOT_FOUND = "Data file not found. Please try again.";
    public static final String MESSAGE_INCORRECT_FORMAT = "Data file not in the correct format.";

    private final Logger logger = LogsCenter.getLogger(getClass());

    @Override
    public CommandResult execute(Model model) throws CommandException {
        return new CommandResult(MESSAGE_SUCCESS, CommandResult.UiEffect.MERGE, new MergeUiConsumer(model));
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
