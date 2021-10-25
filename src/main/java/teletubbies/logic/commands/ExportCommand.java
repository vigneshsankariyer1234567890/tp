package teletubbies.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.util.Pair;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.Model;
import teletubbies.model.person.Person;
import teletubbies.model.tag.TagUtils;

/**
 * Exports persons containing tags specified by user, pending user confirmation.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";
    public static final String MESSAGE_REQUEST_CONFIRMATION = "\nEnter y to confirm export.\n"
            + "Entering other commands would cancel the export.";

    private final Set<Pair<String, Optional<String>>> tags;

    public ExportCommand(Set<Pair<String, Optional<String>>> tags) {
        this.tags = tags;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.cancelPendingExport();

        List<Person> filteredPersonList = filteredPersonList(model);
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
     * @param model Model
     * @return filtered list of persons
     */
    public List<Person> filteredPersonList(Model model) {
        requireNonNull(tags);

        ObservableList<Person> personObservableList = model.getFilteredPersonList();
        List<Person> personList = personObservableList.stream()
                .filter(TagUtils.personHasTagPredicate(tags))
                .collect(Collectors.toList());
        return personList;
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
