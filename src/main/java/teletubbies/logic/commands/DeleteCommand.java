package teletubbies.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import teletubbies.commons.core.Messages;
import teletubbies.commons.core.index.Index;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.Model;
import teletubbies.model.person.Person;
import teletubbies.model.person.Phone;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person with the given phone number or identified by the index number used in the "
            + "displayed person list.\n"
            + "Parameters: p/PHONE_NUMBER (must be a valid phone number) or i/INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " p/81234567, " + COMMAND_WORD + " i/1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    private final Phone targetPhone;
    private final Index targetIndex;
    private final boolean isPhonePrefix;

    /**
     * @param targetPhone of the person in the person list to delete
     */
    public DeleteCommand(Phone targetPhone) {
        this.targetPhone = targetPhone;
        this.targetIndex = null;
        this.isPhonePrefix = true;
    }

    /**
     * @param targetIndex of the person in the filtered person list to delete
     */
    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
        this.targetPhone = null;
        this.isPhonePrefix = false;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (isPhonePrefix) {
            List<Person> list = model.getAddressBook().getPersonList();

            if (list.stream().noneMatch(p -> p.getPhone().equals(targetPhone))) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_PHONE_NUMBER);
            }

            Person personToDelete = list.stream().filter(p -> p.getPhone().equals(targetPhone))
                    .findFirst().get();

            model.deletePerson(personToDelete);
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete));
        }

        List<Person> lastShownList = model.getFilteredPersonList();

        assert targetIndex != null;
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deletePerson(personToDelete);

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && (isPhonePrefix
                        ? targetPhone.equals(((DeleteCommand) other).targetPhone)
                        : targetIndex.equals(((DeleteCommand) other).targetIndex))); // state check
    }
}
