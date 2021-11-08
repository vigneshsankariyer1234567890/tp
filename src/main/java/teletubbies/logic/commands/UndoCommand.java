package teletubbies.logic.commands;

import static java.util.Objects.requireNonNull;

import teletubbies.commons.exceptions.EarliestVersionException;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.Model;

/**
 * Reverts Teletubbies' {@code AddressBook} to previous state.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Teletubbies was successfully undone!";
    public static final String MESSAGE_FAILURE = "Teletubbies is currently at its earliest version and cannot be "
            + "reverted.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.cancelPendingExport();

        if (!model.canUndoAddressBook()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        try {
            model.undoAddressBook();
        } catch (EarliestVersionException e) {
            throw new CommandException(e.getMessage());
        }

        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
