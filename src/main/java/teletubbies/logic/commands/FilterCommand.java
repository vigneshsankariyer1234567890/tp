package teletubbies.logic.commands;

import static java.util.Objects.requireNonNull;
import static teletubbies.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;

import teletubbies.commons.core.Messages;
import teletubbies.logic.parser.Prefix;
import teletubbies.model.Model;
import teletubbies.model.person.PersonHasTagsPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case-insensitive.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final List<Prefix> REQUIRED_FLAGS = List.of(PREFIX_TAG);

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters person to only those with the specified "
            + "tags and values (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: " + PREFIX_TAG + " TAGNAME [MORE_TAGS]...\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_TAG + " CompletionStatus:COMPLETE";

    private final PersonHasTagsPredicate predicate;

    public FilterCommand(PersonHasTagsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.cancelPendingExport();

        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FilterCommand // instanceof handles nulls
                && predicate.equals(((FilterCommand) other).predicate)); // state check
    }
}
