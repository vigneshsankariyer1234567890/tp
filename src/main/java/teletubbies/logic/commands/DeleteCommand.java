package teletubbies.logic.commands;

import static java.util.Objects.requireNonNull;
import static teletubbies.logic.parser.CliSyntax.PREFIX_INDEX;
import static teletubbies.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.List;

import teletubbies.commons.core.Messages;
import teletubbies.commons.core.index.Index;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.logic.parser.Prefix;
import teletubbies.model.Model;
import teletubbies.model.person.Person;
import teletubbies.model.person.Phone;

/**
 * Deletes a person identified using it's displayed index from the contact list.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final List<Prefix> REQUIRED_FLAGS = List.of(PREFIX_INDEX);

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person with the given phone number or the index used in the contact list.\n"
            + "Parameters: "
            + PREFIX_PHONE + " PHONE_NUMBER (must be a valid phone number) or "
            + PREFIX_INDEX + " INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_PHONE + " 81234567, " + COMMAND_WORD + " "
            + PREFIX_INDEX + " 1";

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
        model.cancelPendingExport();

        if (isPhonePrefix) {
            List<Person> list = model.getAddressBook().getPersonList();

            if (list.stream().noneMatch(p -> p.getPhone().equals(targetPhone))) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_PHONE_NUMBER);
            }

            Person personToDelete = list.stream().filter(p -> p.getPhone().equals(targetPhone))
                    .findFirst().get();

            model.deletePerson(personToDelete);
            model.commitAddressBook();
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete));
        }

        assert targetIndex != null;
        Person personToDelete = getPersonFromIndex(model, targetIndex);
        model.deletePerson(personToDelete);
        model.commitAddressBook();

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
