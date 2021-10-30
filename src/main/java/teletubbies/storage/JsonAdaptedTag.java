package teletubbies.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import teletubbies.commons.core.UserProfile.Role;
import teletubbies.commons.exceptions.IllegalValueException;
import teletubbies.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Tag}.
 */
class JsonAdaptedTag {

    private final String tagName;
    private final String tagValue;
    private final Role[] editAccessRoles;


    /**
     * Constructs a {@code JsonAdaptedTag} with the given {@code tagName}, {@code tagValue},
     * {@code deleteAccessRoles} and {@code editAccessRoles}
     */
    @JsonCreator
    public JsonAdaptedTag(@JsonProperty("tagName") String tagName, @JsonProperty("tagValue") String tagValue,
                          @JsonProperty("editAccessRoles") Role[] editAccessRoles) {
        this.tagName = tagName;
        this.tagValue = tagValue;
        this.editAccessRoles = editAccessRoles;
    }

    /**
     * Constructs a {@code JsonAdaptedTag} with the given {@code tagName}
     */
    public JsonAdaptedTag(String tagName) {
        this.tagName = tagName;
        this.tagValue = "";
        this.editAccessRoles = new Role[]{};
    }

    /**
     * Converts a given {@code Tag} into this class for Jackson use.
     */
    public JsonAdaptedTag(Tag source) {
        tagName = source.tagName;
        tagValue = source.getTagValue();
        editAccessRoles = source.editAccessRoles;
    }

    /**
     * Converts this Jackson-friendly adapted tag object into the model's {@code Tag} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted tag.
     */
    public Tag toModelType() throws IllegalValueException {
        if (!Tag.isValidTagName(tagName)) {
            throw new IllegalValueException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(tagName, tagValue, editAccessRoles, false);
    }

}
