package teletubbies.logic.commands;

import static java.util.Objects.requireNonNull;
import static teletubbies.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.logic.parser.Prefix;
import teletubbies.model.Model;
import teletubbies.model.person.Person;
import teletubbies.model.person.PersonHasTagsPredicate;
import teletubbies.model.tag.Tag;

/**
 * Exports persons containing tags specified by user, pending user confirmation.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";
    public static final String MESSAGE_REQUEST_CONFIRMATION = "\nEnter y to confirm export.\n"
            + "Entering other commands would cancel the export.";

    public static final List<Prefix> REQUIRED_FLAGS = List.of(PREFIX_TAG);

    private final Set<Tag> tags;

    public ExportCommand(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.cancelPendingExport();

        ObservableList<Person> personObservableList = model.getFilteredPersonList();
        List<Person> filteredPersonList = filterPersonList(personObservableList);
        model.updateExportList(filteredPersonList);

        String feedbackMessage = (tags.isEmpty()
                ? "All contacts will be exported"
                : filteredPersonList.size() + " contacts listed below will be exported. "
                + "Exported tags: " + tags)
                + MESSAGE_REQUEST_CONFIRMATION;

        return new CommandResult(feedbackMessage);
    }

    /**
     * Filters persons to those with specified tags
     *
     * @param personList person list to filter
     * @return filtered list of persons
     */
    public List<Person> filterPersonList(List<Person> personList) {
        requireNonNull(tags);

        return personList.stream()
                .filter(new PersonHasTagsPredicate(tags))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ExportCommand)) {
            return false;
        }

        // state check
        ExportCommand c = (ExportCommand) other;
        return c.tags.containsAll(tags) && tags.containsAll(c.tags);
    }
}
