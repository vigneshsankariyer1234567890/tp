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
import teletubbies.logic.parser.Prefix;
import teletubbies.model.Model;
import teletubbies.model.person.Person;
import teletubbies.model.tag.Tag;
import teletubbies.model.tag.TagUtils;

public class RemoveTagCommand extends Command {

    public static final String COMMAND_WORD = "tagrm";

    public static final List<Prefix> REQUIRED_FLAGS = List.of(CliSyntax.PREFIX_NAME);

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes tag from "
            + "persons specified by the indices.\n"
            + "Parameters: RANGE (can be hyphen separated or comma separated integers)\n"
            + "Example: " + COMMAND_WORD + " 1-10 " + CliSyntax.PREFIX_NAME + " Assignee";

    public static final String MESSAGE_COMPLETED_SUCCESS = " Tag removed";

    public final Range range;
    public final String tagName;

    /**
     * Creates a TagCommand to remove the specified {@code Tag}
     *
     * @param range Range of
     * @param tagName
     */
    public RemoveTagCommand(Range range, String tagName) {
        CollectionUtil.requireAllNonNull(range);
        CollectionUtil.requireAllNonNull(tagName);
        this.range = range;
        this.tagName = tagName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.cancelPendingExport();

        if (!Tag.isValidTagName(tagName)) {
            throw new CommandException(TagUtils.INVALID_TAG_NAME);
        }

        List<String> feedbackMessages = new ArrayList<>();
        List<Person> rangePersons = getPersonsFromRange(model, range);
        Role userRole = model.getUserRole();

        for (Person p: rangePersons) {
            Set<Tag> tags = new HashSet<>(p.getTags());
            Optional<Tag> matchingTag = TagUtils.findMatchingTag(tags, tagName);
            Set<Tag> newTags = getRemovedTagSet(tags, matchingTag, userRole, feedbackMessages);

            Person editedPerson = new Person(p.getUuid(), p.getName(), p.getPhone(), p.getEmail(),
                    p.getAddress(), p.getCompletionStatus(), p.getRemark(), newTags);

            model.setPerson(p, editedPerson);
        }

        throwMessages(feedbackMessages);
        return new CommandResult(MESSAGE_COMPLETED_SUCCESS);
    }

    /**
     * Remove matchingTag from the tag set if matching tag exists
     * and user role has permissions to modify the tag.
     *
     * Note: this function modifies the messages list
     *
     * @param tags tag set to remove from
     * @param matchingTag tag to remove from the set
     * @param userRole user role doing the deletion
     * @param messages feedback messages
     * @return new tag set without matchingTag
     */
    public Set<Tag> getRemovedTagSet(Set<Tag> tags, Optional<Tag> matchingTag, Role userRole,
                                     List<String> messages) {
        if (matchingTag.isEmpty()) {
            return tags;
        }
        Set<Tag> newTags = new HashSet<>(tags);
        if (matchingTag.get().isEditableByRole(userRole)) {
            newTags.remove(matchingTag.get());
        } else {
            messages.add(
                    TagUtils.noPermissionsMessage(matchingTag.get().tagName)
            );
        }
        return newTags;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RemoveTagCommand)) {
            return false;
        }

        // state check
        RemoveTagCommand e = (RemoveTagCommand) other;
        return tagName.equals(e.tagName)
                && (this.range.equals(e.range));
    }
}
