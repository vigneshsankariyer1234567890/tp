package teletubbies.testutil;

import java.util.HashSet;
import java.util.Set;

import teletubbies.model.person.Address;
import teletubbies.model.person.Email;
import teletubbies.model.person.Name;
import teletubbies.model.person.Person;
import teletubbies.model.person.Phone;
import teletubbies.model.person.Remark;
import teletubbies.model.person.Uuid;
import teletubbies.model.tag.CompletionStatusTag;
import teletubbies.model.tag.CompletionStatusTag.CompletionStatus;
import teletubbies.model.tag.Tag;
import teletubbies.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {
    public static final String DEFAULT_UUID = "5adca888-2825-49c2-82e2-78830d923aa4";
    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_REMARK = "";
    public static final Set<Tag> DEFAULT_TAGS = new HashSet<>();

    private Uuid uuid;
    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private CompletionStatusTag completionStatusTag;
    private Remark remark;
    private Set<Tag> tags;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        uuid = new Uuid(DEFAULT_UUID);
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        completionStatusTag = new CompletionStatusTag();
        remark = new Remark(DEFAULT_REMARK);
        tags = new HashSet<>(DEFAULT_TAGS);
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        uuid = personToCopy.getUuid();
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        completionStatusTag = personToCopy.getCompletionStatus();
        remark = personToCopy.getRemark();
        tags = new HashSet<>(personToCopy.getTags());
    }

    /**
     * Sets the {@code Uuid} of the {@code Person} that we are building.
     */
    public PersonBuilder withUuid(String uuid) {
        this.uuid = new Uuid(uuid);
        return this;
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code CompletionStatus} of the {@code Person} that we are building.
     */
    public PersonBuilder withCompletionStatus(CompletionStatus completionStatus) {
        this.completionStatusTag = new CompletionStatusTag(completionStatus);
        return this;
    }

    /**
     * Sets the {@code Remark} of the {@code Person} that we are building.
     */
    public PersonBuilder withRemark(String remark) {
        this.remark = new Remark(remark);
        return this;
    }

    public Person build() {
        return new Person(uuid, name, phone, email, address, completionStatusTag, remark, tags);
    }

}
