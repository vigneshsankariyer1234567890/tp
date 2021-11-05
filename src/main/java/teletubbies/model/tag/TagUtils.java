package teletubbies.model.tag;

import java.util.Optional;
import java.util.Set;

public class TagUtils {

    public static final String INVALID_TAG_NAME = "Invalid tag name.\n"
            + Tag.MESSAGE_CONSTRAINTS + "\nReserved tag name(s): "
            + String.join(", ", Tag.RESERVED_TAG_NAMES);
    public static String noPermissionsMessage(String tagName) {
        return "You don't have permission to modify this tag - " + tagName;
    }

    /**
     * Find tag in a set with given tagName
     *
     * @param tagSet tag set to search in
     * @param tagName tag name to find
     * @return tag found (potentially empty)
     */
    public static Optional<Tag> findMatchingTag(Set<Tag> tagSet, String tagName) {
        Tag tempTag = new Tag(tagName);
        return tagSet.stream().filter(t -> t.equals(tempTag)).findFirst();
    }

}
