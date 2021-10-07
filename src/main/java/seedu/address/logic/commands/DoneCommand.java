package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.CompletionStatus;

public class DoneCommand extends Command {

    public static final String COMMAND_WORD = "done";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks the person identified "
            + "by the index number used in the last person listing as completed.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 ";

    public static final String MESSAGE_ARGUMENTS = "Index: %1$d";

    private final Index index;
    private final CompletionStatus completionStatus;

    /**
     * @param index of the person in the filtered person list to edit the remark
     */
    public DoneCommand(Index index) {
        requireAllNonNull(index);
        this.index = index;
        this.completionStatus = new CompletionStatus(true);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException(String.format(MESSAGE_ARGUMENTS, index.getOneBased()));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DoneCommand)) {
            return false;
        }

        // state check
        DoneCommand e = (DoneCommand) other;
        return index.equals(e.index) && (this.completionStatus.equals(e.completionStatus));
    }
}
