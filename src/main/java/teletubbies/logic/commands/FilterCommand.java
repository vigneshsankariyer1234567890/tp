package teletubbies.logic.commands;

import static java.util.Objects.requireNonNull;
import static teletubbies.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;

import teletubbies.commons.core.Messages;
import teletubbies.logic.parser.Prefix;
import teletubbies.model.Model;
import teletubbies.model.person.PersonHasTagsPredicate;

/**
 * Filters and lists all persons in person list whose who contain the specified tags.
 * Tag matching is case-sensitive.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final List<Prefix> REQUIRED_FLAGS = List.of(PREFIX_TAG);

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters person to only those with the specified "
            + "tag names and values (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: " + PREFIX_TAG + " TAGNAME[: TAGVALUE] [-t TAGNAME[: TAGVALUE]]...\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_TAG + " CompletionStatus: COMPLETE";

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
