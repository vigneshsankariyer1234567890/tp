package teletubbies.model.person;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import teletubbies.commons.util.CollectionUtil;
import teletubbies.model.tag.CompletionStatusTag;
import teletubbies.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Uuid uuid;
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final CompletionStatusTag completionStatusTag;
    private final Set<Tag> tags = new HashSet<>();
    private final Remark remark;

    /**
     * Every field must be present and not null.
     */
    public Person(Uuid uuid, Name name, Phone phone, Email email, Address address,
                  CompletionStatusTag completionStatusTag,
                  Remark remark, Set<Tag> tags) {
        CollectionUtil.requireAllNonNull(name, phone, email, address, tags);
        this.uuid = uuid;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.remark = remark;
        this.completionStatusTag = completionStatusTag;
        this.tags.addAll(tags);
    }

    public Uuid getUuid() {
        return uuid;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Remark getRemark() {
        return remark;
    }

    /**
     * Returns an immutable CompletionStatus
     * @return CompletionStatus
     */
    public CompletionStatusTag getCompletionStatus() {
        return completionStatusTag;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same uuid.
     * This defines a stronger notion of equality between two persons.
     */
    public boolean isSameUuid(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getUuid().equals(getUuid());
    }

    /**
     * Returns an immutable tag set, which
     *
     * @return
     */
    public Set<Tag> getAllTags() {
        HashSet<Tag> allTags = new HashSet<>(Set.copyOf(tags));
        allTags.add(completionStatusTag);
        return Collections.unmodifiableSet(allTags);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSameName(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same phone number.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePhoneNumber(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getPhone().equals(getPhone());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return otherPerson.getUuid().equals(getUuid())
                && otherPerson.getName().equals(getName())
                && otherPerson.getPhone().equals(getPhone())
                && otherPerson.getEmail().equals(getEmail())
                && otherPerson.getAddress().equals(getAddress())
                && otherPerson.getCompletionStatus().equals(getCompletionStatus())
                && otherPerson.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(uuid, name, phone, email, address, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append("; Phone: ")
                .append(getPhone());

        if (email.isPresent()) {
            builder.append("; Email: ")
                    .append(getEmail());
        }

        if (address.isPresent()) {
            builder.append("; Address: ")
                    .append(getAddress());
        }
        builder.append("; Completed: ")
               .append(getCompletionStatus());

        if (remark.isPresent()) {
            builder.append(" Remark: ")
                   .append(getRemark());
        }

        Set<Tag> tags = getTags();
        if (!tags.isEmpty()) {
            builder.append("; Tags: ");
            builder.append(tags.stream()
                    .map(Tag::toString)
                    .collect(Collectors.joining(", ")));
        }
        return builder.toString();
    }

}
