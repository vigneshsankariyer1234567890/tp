package teletubbies.logic.commands;

import static java.util.Objects.requireNonNull;

import teletubbies.logic.parser.Prefix;
import teletubbies.model.AddressBook;
import teletubbies.model.Model;

import java.util.List;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.cancelPendingExport();
        model.setAddressBook(new AddressBook());
        model.commitAddressBook();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
