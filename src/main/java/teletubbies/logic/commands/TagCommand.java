package teletubbies.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import teletubbies.commons.core.Range;
import teletubbies.commons.core.UserProfile.Role;
import teletubbies.commons.util.CollectionUtil;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.logic.parser.CliSyntax;
import teletubbies.model.Model;
import teletubbies.model.person.Person;
import teletubbies.model.tag.Tag;
import teletubbies.model.tag.TagUtils;

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
    private final String tagValue;
    private final boolean isSupervisorOnlyTag;

    /**
     * Creates a TagCommand to add the specified {@code Tag}
     *
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
        requireNonNull(model);
        if (!Tag.isValidTagName(tagName)) {
            throw new CommandException(TagUtils.INVALID_TAG_NAME);
        }

        List<String> feedbackMessages = new ArrayList<>();
        List<Person> rangePersons = getPersonsFromRange(model, range);
        Role userRole = model.getUserRole();

        for (Person p: rangePersons) {
            Set<Tag> tags = p.getTags();
            Optional<Tag> matchingTag = TagUtils.findMatchingTag(tags, tagName);
            Tag newTag = generateNewTag(matchingTag, feedbackMessages, userRole);

            Set<Tag> newTags = new HashSet<>(tags);
            newTags.remove(newTag);
            newTags.add(newTag);

            Person editedPerson = new Person(p.getUuid(), p.getName(), p.getPhone(), p.getEmail(),
                    p.getAddress(), p.getCompletionStatus(), newTags);

            model.setPerson(p, editedPerson);
        }

        throwMessages(feedbackMessages);
        return new CommandResult(MESSAGE_COMPLETED_SUCCESS);
    }

    /**
     * Generates a new tag based on matchingTag. Creates tag with
     * given command's name and tag if matching tag is not present.
     * If matching tag is present, copy the existing tag, and replace
     * value/editable roles if they are specified by the command.
     *
     * @param matchingTag matching tag (possible empty)
     * @param feedback feedback messages
     * @param userRole user's role
     * @return new generated tag
     */
    public Tag generateNewTag(Optional<Tag> matchingTag, List<String> feedback, Role userRole) {
        if (matchingTag.isEmpty()) {
            // No matching tag, so we create a new one
            return new Tag(tagName, tagValue, isSupervisorOnlyTag);
        } else if (matchingTag.get().isEditableByRole(userRole)) {
            // Copy matching tag
            String previousValue = matchingTag.get().getTagValue();
            Tag newTag = new Tag(tagName, previousValue, isSupervisorOnlyTag);
            if (tagValue != null) {
                newTag.setTagValue(tagValue);
            }
            return newTag;
        } else {
            feedback.add(
                TagUtils.noPermissionsMessage(matchingTag.get().tagName)
            );
            return null;
        }
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
