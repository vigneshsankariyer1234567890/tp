package teletubbies.model.tag;

import teletubbies.commons.core.UserProfile.Role;

/**
 * Represents a Person's completion status in the address book (to check if
 * the contact has been contacted).
 * Guarantees: immutable; is always valid
 */
public class CompletionStatusTag extends Tag {
    public enum CompletionStatus {
        INCOMPLETE, ONGOING, COMPLETE
    }

    public final CompletionStatus status;

    public CompletionStatusTag(CompletionStatus status) {
        super("CompletionStatus", new Role[]{});

        this.status = status;
        this.setTagValue(status.toString());
    }

    public CompletionStatusTag() {
        super("CompletionStatus", new Role[]{});

        this.status = CompletionStatus.INCOMPLETE;
        this.setTagValue(this.status.toString());
    }

    @Override
    public String toString() {
        return String.valueOf(status);
    }

}
