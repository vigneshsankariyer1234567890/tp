package teletubbies.logic.commands;

import static java.util.Objects.requireNonNull;
import static teletubbies.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import teletubbies.commons.core.index.Index;
import teletubbies.commons.util.CollectionUtil;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.logic.parser.CliSyntax;
import teletubbies.model.Model;
import teletubbies.model.person.Person;
import teletubbies.model.tag.CompletionStatusTag;
import teletubbies.model.tag.CompletionStatusTag.CompletionStatus;

public class DoneCommand extends Command {

    public static final String COMMAND_WORD = "done";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks the completion status of the "
            + "person identified by the index number used in the last person listing.\n"
            + "Sets completed by default. Only one of " + CliSyntax.PREFIX_ONGOING + " or "
            + CliSyntax.PREFIX_INCOMPLETE + " can be used.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 " + CliSyntax.PREFIX_ONGOING;

    public static final String MESSAGE_COMPLETED_SUCCESS = "Marked Person as completed: %1$s";

    private final Index index;
    private final CompletionStatus completionStatus;

    /**
     * @param index of the person in the filtered person list to mark
     */
    public DoneCommand(Index index) {
        this(index, CompletionStatus.COMPLETE);
    }

    /**
     * @param index of the person in the filtered person list to mark
     */
    public DoneCommand(Index index, CompletionStatus completionStatus) {
        CollectionUtil.requireAllNonNull(index);
        this.index = index;
        this.completionStatus = completionStatus;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.cancelPendingExport();

        Person personToEdit = getPersonFromIndex(model, index);
        Person editedPerson = new Person(personToEdit.getUuid(),
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                new CompletionStatusTag(completionStatus),
                personToEdit.getRemark(),
                personToEdit.getTags());

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.commitAddressBook();

        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    /**
     * Generates a command execution success message
     * {@code personToEdit}.
     */
    private String generateSuccessMessage(Person personToEdit) {
        return String.format(MESSAGE_COMPLETED_SUCCESS, personToEdit);
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
