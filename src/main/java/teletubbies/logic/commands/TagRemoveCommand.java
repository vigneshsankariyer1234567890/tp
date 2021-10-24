package teletubbies.logic.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import teletubbies.commons.core.Range;
import teletubbies.commons.core.UserProfile.Role;
import teletubbies.commons.exceptions.IllegalValueException;
import teletubbies.commons.util.CollectionUtil;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.logic.parser.CliSyntax;
import teletubbies.model.Model;
import teletubbies.model.person.Person;
import teletubbies.model.tag.Tag;

public class TagRemoveCommand extends Command {

    public static final String COMMAND_WORD = "tagrm";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes tag from "
            + "persons specified by the indices.\n"
            + "Parameters: RANGE (can be hyphen separated or comma separated integers)\n"
            + "Example: " + COMMAND_WORD + " 1-10 " + CliSyntax.PREFIX_NAME + " Assignee";

    public static final String MESSAGE_COMPLETED_SUCCESS = " Tag removed";

    private final Range range;
    private final String tagName;

    /**
     *
     * @param range
     * @param tagName
     */
    public TagRemoveCommand(Range range, String tagName) {
        CollectionUtil.requireAllNonNull(range);
        CollectionUtil.requireAllNonNull(tagName);
        this.range = range;
        this.tagName = tagName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<String> feedbackMessages = new ArrayList<>();
        List<Person> rangePersons;
        final Tag tagToRm;
        Tag tempTagToRm;

        try {
            rangePersons = model.getPersonsFromRange(range);
            tempTagToRm = new Tag(tagName);
        } catch (IllegalValueException ive) {
            rangePersons = new ArrayList<>();
            tempTagToRm = null;
            feedbackMessages.add(COMMAND_WORD + ": Range of persons out of bounds");
        } catch (IllegalArgumentException iae) {
            rangePersons = new ArrayList<>();
            tempTagToRm = null;
            feedbackMessages.add(COMMAND_WORD + ": Invalid tag name");
        }
        tagToRm = tempTagToRm;

        Role userRole = model.getUserPrefs().getUserProfile().getRole(); // TODO don't get friend of friend
        rangePersons.forEach(p -> {
            Set<Tag> tags = new HashSet<>(p.getTags());
            Tag matchingTag = tags.stream().filter(t -> t.equals(tagToRm)).findFirst()
                    .orElse(null);
            boolean editable = matchingTag == null || List.of(matchingTag.editAccessRoles).contains(userRole);

            if (editable) {
                tags.remove(tagToRm);
            } else {
                feedbackMessages.add("You don't have permission to modify this tag - "
                        + "Person number: " + p.getPhone());
            }

            Person editedPerson = new Person(p.getName(), p.getPhone(), p.getEmail(),
                    p.getAddress(), p.getCompletionStatus(), tags);

            model.setPerson(p, editedPerson);
        });

        if (!feedbackMessages.isEmpty()) {
            throw new CommandException(String.join("\n", feedbackMessages));
        }

        return new CommandResult(MESSAGE_COMPLETED_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagRemoveCommand)) {
            return false;
        }

        // state check
        TagRemoveCommand e = (TagRemoveCommand) other;
        return tagName.equals(e.tagName)
                && (this.range.equals(e.range));
    }
}
