package teletubbies.logic.commands;

import static java.util.Objects.requireNonNull;

import teletubbies.model.AddressBook;
import teletubbies.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Teletubbies has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.cancelPendingExport();
        model.setAddressBook(new AddressBook());
        model.commitAddressBook();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
