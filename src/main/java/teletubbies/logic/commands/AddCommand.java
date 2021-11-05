package teletubbies.logic.commands;

import static java.util.Objects.requireNonNull;
import static teletubbies.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static teletubbies.logic.parser.CliSyntax.PREFIX_EMAIL;
import static teletubbies.logic.parser.CliSyntax.PREFIX_NAME;
import static teletubbies.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.List;

import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.logic.parser.Prefix;
import teletubbies.model.Model;
import teletubbies.model.person.Person;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final List<Prefix> REQUIRED_FLAGS = List.of(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
            PREFIX_ADDRESS);

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the contact list. "
            + "Parameters: "
            + PREFIX_NAME + " NAME "
            + PREFIX_PHONE + " PHONE "
            + "[" + PREFIX_EMAIL + " EMAIL] "
            + "[" + PREFIX_ADDRESS + " ADDRESS] \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + " John Doe "
            + PREFIX_PHONE + " 98765432 "
            + PREFIX_EMAIL + " johnd@example.com "
            + PREFIX_ADDRESS + " 311, Clementi Ave 2, #02-25 ";

    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    public static final String MESSAGE_DUPLICATE_PHONE_NUMBER = "This phone number already exists in the contact list";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.cancelPendingExport();

        if (model.hasPhoneNumber(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PHONE_NUMBER);
        }

        model.addPerson(toAdd);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddCommand) other).toAdd));
    }
}
