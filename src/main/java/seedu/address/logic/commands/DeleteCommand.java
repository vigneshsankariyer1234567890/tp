package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person with the given phone number used in the displayed person list.\n"
            + "Parameters: PHONE_NUMBER (must be a valid phone number)\n"
            + "Example: " + COMMAND_WORD + " 81234567";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

//    private final Index targetIndex;

    private final Phone targetPhone;

//    public DeleteCommand(Index targetIndex) {
//        this.targetIndex = targetIndex;
//    }

    public DeleteCommand(Phone targetPhone) {
        this.targetPhone = targetPhone;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // TODO: Return error message if Teletubbies does not contain the given phone number
        // if (targetIndex.getZeroBased() >= lastShownList.size()) {
        //     throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        // }

        List<Person> list = model.getAddressBook().getPersonList();
        Person personToDelete = list.stream().filter(p -> p.getPhone().equals(targetPhone))
                .findFirst().get();
        model.deletePerson(personToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && targetPhone.equals(((DeleteCommand) other).targetPhone)); // state check
    }
}
