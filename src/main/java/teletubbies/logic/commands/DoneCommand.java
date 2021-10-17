package teletubbies.logic.commands;

import static teletubbies.commons.util.CollectionUtil.requireAllNonNull;
import static teletubbies.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import teletubbies.commons.core.Messages;
import teletubbies.commons.core.index.Index;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.Model;
import teletubbies.model.person.CompletionStatus;
import teletubbies.model.person.Person;
import teletubbies.commons.util.CollectionUtil;

public class DoneCommand extends Command {

    public static final String COMMAND_WORD = "done";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks the person identified "
            + "by the index number used in the last person listing as completed.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 ";

    public static final String MESSAGE_COMPLETED_SUCCESS = "Marked Person as completed: %1$s";

    private final Index index;
    private final CompletionStatus completionStatus;

    /**
     * @param index of the person in the filtered person list to edit the remark
     */
    public DoneCommand(Index index) {
        CollectionUtil.requireAllNonNull(index);
        this.index = index;
        this.completionStatus = new CompletionStatus(true);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        Person editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), completionStatus, personToEdit.getTags());

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

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
