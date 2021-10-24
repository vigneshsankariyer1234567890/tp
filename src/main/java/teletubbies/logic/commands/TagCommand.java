package teletubbies.logic.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds specified tag to the "
            + "persons specified by the indices.\n"
            + "Parameters: RANGE (can be hyphen separated or comma separated integers)\n"
            + "Example: " + COMMAND_WORD + " 1-10 " + CliSyntax.PREFIX_NAME + " Assignee "
            + CliSyntax.PREFIX_VALUE + " John Doe " + CliSyntax.PREFIX_SUPERVISOR_FLAG;

    public static final String MESSAGE_COMPLETED_SUCCESS = "Tag added";

    private final Range range;
    private final String tagName;
    private String tagValue;
    private final boolean isSupervisorOnlyTag;

    /**
     * @param range Range object for persons to tag
     * @param tagName Name of tag
     * @param tagValue Value of tag
     * @param isSupervisorOnlyTag Access level of tag
     */
    public TagCommand(Range range, String tagName, String tagValue, boolean isSupervisorOnlyTag) {
        CollectionUtil.requireAllNonNull(range);
        CollectionUtil.requireAllNonNull(tagName);
        this.range = range;
        this.tagName = tagName;
        this.tagValue = tagValue;
        this.isSupervisorOnlyTag = isSupervisorOnlyTag;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<String> feedbackMessages = new ArrayList<>();
        List<Person> rangePersons;

        try {
            rangePersons = model.getPersonsFromRange(range);
            new Tag(tagName);
        } catch (IllegalValueException ive) {
            feedbackMessages.add(COMMAND_WORD + ": Range of persons out of bounds");
            rangePersons = new ArrayList<>();
        } catch (IllegalArgumentException iae) {
            feedbackMessages.add(COMMAND_WORD + ": Invalid tag name");
            rangePersons = new ArrayList<>();
        }

        Role userRole = model.getUserPrefs().getUserProfile().getRole(); // TODO don't get friend of friend
        rangePersons.forEach(p -> {
            Role[] accessRoles;
            Tag tempNewTag = new Tag(tagName);
            Set<Tag> newTags = new HashSet<>(p.getTags());
            Optional<Tag> matchingTag = newTags.stream().filter(t -> t.equals(tempNewTag)).findFirst();
            boolean editable = matchingTag.isEmpty()
                    || List.of(matchingTag.get().editAccessRoles).contains(userRole);

            if (tagValue == null) {
                tagValue = matchingTag.isPresent()
                    ? matchingTag.get().getTagValue()
                    : "";
            }

            accessRoles = isSupervisorOnlyTag
                    ? new Role[]{ Role.SUPERVISOR }
                    : matchingTag.isPresent()
                        ? matchingTag.get().editAccessRoles
                        : new Role[]{ Role.SUPERVISOR, Role.TELEMARKETER };

            Tag newTag = new Tag(tagName, tagValue, accessRoles);
            if (editable) {
                newTags.remove(newTag);
                newTags.add(newTag);
            } else {
                feedbackMessages.add("You don't have permission to modify this tag - "
                        + "Person number:" + p.getPhone());
            }

            Person editedPerson = new Person(p.getName(), p.getPhone(), p.getEmail(),
                    p.getAddress(), p.getCompletionStatus(), newTags);

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
        if (!(other instanceof TagCommand)) {
            return false;
        }

        // state check
        TagCommand e = (TagCommand) other;
        return tagName.equals(e.tagName)
                && tagValue.equals(e.tagValue)
                && isSupervisorOnlyTag == e.isSupervisorOnlyTag
                && (this.range.equals(e.range));
    }
}
