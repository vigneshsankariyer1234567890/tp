package teletubbies.logic.commands;

import java.util.List;

import teletubbies.commons.core.Messages;
import teletubbies.commons.core.Range;
import teletubbies.commons.core.index.Index;
import teletubbies.commons.exceptions.IllegalValueException;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.Model;
import teletubbies.model.person.Person;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute(Model model) throws CommandException;

    /**
     * Throws a command exception with given message strings
     *
     * @param messages message strings to throw
     * @throws CommandException
     */
    public void throwMessages(List<String> messages) throws CommandException {
        if (!messages.isEmpty()) {
            throw new CommandException(String.join("\n", messages));
        }
    }

    public List<Person> getPersonsFromRange(Model model, Range range) throws CommandException {
        try {
            return model.getPersonsFromRange(range);
        } catch (IllegalValueException ive) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_RANGE);
        }
    }

    public Person getPersonFromIndex(Model model, Index index) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        return lastShownList.get(index.getZeroBased());
    }

}
