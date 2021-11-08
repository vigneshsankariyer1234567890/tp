package teletubbies.model.util;

import static teletubbies.logic.parser.CliSyntax.EMPTY_REMARK;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import teletubbies.model.AddressBook;
import teletubbies.model.ReadOnlyAddressBook;
import teletubbies.model.person.Address;
import teletubbies.model.person.Email;
import teletubbies.model.person.Name;
import teletubbies.model.person.Person;
import teletubbies.model.person.Phone;
import teletubbies.model.person.Remark;
import teletubbies.model.person.Uuid;
import teletubbies.model.tag.CompletionStatusTag;
import teletubbies.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {

    public static final Remark DEFAULT_REMARK = new Remark(EMPTY_REMARK);

    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Uuid("375004d8-5636-4084-977d-8d2b18a90e59"), new Name("Alex Yeoh"),
                new Phone("87438807"), new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"), new CompletionStatusTag(),
                new Remark("Call back at 2pm"), getTagSet("ProductA")),
            new Person(new Uuid("9cd01b04-7964-4d18-98a4-ab90c6520a19"), new Name("Bernice Yu"),
                new Phone("99272758"), new Email("berniceyu@example.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                new CompletionStatusTag(CompletionStatusTag.CompletionStatus.COMPLETE),
                new Remark("Successful sale, $500"),
                getTagSet("ProductA")),
            new Person(new Uuid("376fc044-f407-44a4-9abd-3035aedf27e3"), new Name("Charlotte Oliveiro"),
                new Phone("93210283"), new Email("charlotte@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"), new CompletionStatusTag(), DEFAULT_REMARK,
                getTagSet("ProductA", "ProductB")),
            new Person(new Uuid("89f38212-6066-466e-aa67-8391d4136a62"), new Name("David Li"),
                new Phone("91031282"), new Email("lidavid@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                new CompletionStatusTag(CompletionStatusTag.CompletionStatus.ONGOING), DEFAULT_REMARK,
                getTagSet("ProductA")),
            new Person(new Uuid("5adca839-2825-49c2-82e2-78830d923aa4"), new Name("Irfan Ibrahim"),
                new Phone("92492021"), new Email("irfan@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"), new CompletionStatusTag(), DEFAULT_REMARK,
                getTagSet("ProductB")),
            new Person(new Uuid("1e53b8d3-0303-4177-bb77-3ff9f010922f"), new Name("Roy Balakrishnan"),
                new Phone("92624417"), new Email("royb@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"), new CompletionStatusTag(), DEFAULT_REMARK,
                getTagSet("ProductA"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
