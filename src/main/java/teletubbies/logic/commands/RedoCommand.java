package teletubbies.logic.commands;

import static java.util.Objects.requireNonNull;

import teletubbies.commons.exceptions.LatestVersionException;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.Model;

/**
 * Reverts Teletubbies' {@code AddressBook} to next state.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_SUCCESS = "Teletubbies was successfully redone!";
    public static final String MESSAGE_FAILURE = "Teletubbies is currently at its latest version and cannot be "
            + "redone.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.cancelPendingExport();

        if (!model.canRedoAddressBook()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        try {
            model.redoAddressBook();
        } catch (LatestVersionException e) {
            throw new CommandException(e.getMessage());
        }

        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
