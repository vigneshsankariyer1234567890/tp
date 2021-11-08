package teletubbies.testutil;

import static teletubbies.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static teletubbies.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static teletubbies.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static teletubbies.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static teletubbies.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static teletubbies.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static teletubbies.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static teletubbies.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static teletubbies.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static teletubbies.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static teletubbies.logic.commands.CommandTestUtil.VALID_UUID_AMY;
import static teletubbies.logic.commands.CommandTestUtil.VALID_UUID_BOB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import teletubbies.logic.parser.CliSyntax;
import teletubbies.model.AddressBook;
import teletubbies.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder().withUuid("5adca888-2825-49c2-82e2-78830d923aa4")
            .withName("Alice Pauline").withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
            .withPhone("94351253").withTags("friends").withRemark("She likes aardvarks.").build();
    public static final Person BENSON = new PersonBuilder()
            .withName("Benson Meier").withAddress("311, Clementi Ave 2, #02-25").withEmail("johnd@example.com")
            .withPhone("98765432").withTags("owesMoney", "friends").withRemark("She likes aardvarks.")
            .withUuid(UUID.randomUUID().toString()).build();
    public static final Person CARL = new PersonBuilder().withUuid("5adca839-2825-49c2-82e2-78830d923aa4")
            .withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@example.com").withAddress("wall street").withUuid(UUID.randomUUID().toString()).build();
    public static final Person DANIEL = new PersonBuilder().withUuid("5adca839-2825-49c2-81e2-78830d923aa4")
            .withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@example.com").withAddress("10th street").withTags("friends")
            .withUuid(UUID.randomUUID().toString()).build();
    public static final Person ELLE = new PersonBuilder().withUuid("5adca839-2825-49c2-82e4-78830d923aa4")
            .withName("Elle Meyer").withPhone("9482224")
            .withEmail("werner@example.com").withAddress("michegan ave").withUuid(UUID.randomUUID().toString()).build();
    public static final Person FIONA = new PersonBuilder().withUuid("5adca819-2825-49c2-82e2-78830d923aa4")
            .withName("Fiona Kunz").withPhone("9482427")
            .withEmail("lydia@example.com").withAddress("little tokyo").withUuid(UUID.randomUUID().toString()).build();
    public static final Person GEORGE = new PersonBuilder().withUuid("5adca839-2925-49c2-82e2-78830d923aa4")
            .withName("George Best").withPhone("9482442")
            .withEmail("anna@example.com").withAddress("4th street").withUuid(UUID.randomUUID().toString()).build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withUuid("9cd01b04-7964-4d18-98a4-a666c6520a21")
            .withName("Hoon Meier").withPhone("8482424")
            .withEmail("stefan@example.com").withAddress("little india").build();
    public static final Person IDA = new PersonBuilder().withUuid("9cd01b04-7964-4d18-98a4-ab9996520a21")
            .withName("Ida Mueller").withPhone("8482131")
            .withEmail("hans@example.com").withAddress("chicago ave").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withUuid(VALID_UUID_AMY)
            .withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
            .withAddress(VALID_ADDRESS_AMY).withTags(VALID_TAG_FRIEND).build();
    public static final Person NO_EMAIL_AMY = new PersonBuilder().withUuid(VALID_UUID_AMY)
            .withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY).withEmail(CliSyntax.DEFAULT_EMAIL)
            .withAddress(VALID_ADDRESS_AMY).build();
    public static final Person NO_ADDRESS_AMY = new PersonBuilder().withUuid(VALID_UUID_AMY)
            .withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
            .withAddress(CliSyntax.DEFAULT_ADDRESS).build();
    public static final Person NO_TAGS_AMY = new PersonBuilder().withUuid(VALID_UUID_AMY)
            .withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
            .withAddress(VALID_ADDRESS_AMY).build();
    public static final Person BOB = new PersonBuilder().withUuid(VALID_UUID_BOB)
            .withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
            .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
    public static final Person NO_TAGS_BOB = new PersonBuilder().withUuid(VALID_UUID_BOB)
            .withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
            .withAddress(VALID_ADDRESS_BOB).build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
