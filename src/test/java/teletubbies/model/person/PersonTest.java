package teletubbies.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static teletubbies.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static teletubbies.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static teletubbies.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static teletubbies.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static teletubbies.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static teletubbies.logic.commands.CommandTestUtil.VALID_UUID_BOB;
import static teletubbies.testutil.Assert.assertThrows;
import static teletubbies.testutil.TypicalPersons.ALICE;
import static teletubbies.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import teletubbies.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSameUuid() {
        // same object -> returns true
        assertTrue(ALICE.isSameUuid(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameUuid(null));

        // same uuid, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSameUuid(editedAlice));

        // different uuid, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withUuid(VALID_UUID_BOB).build();
        assertFalse(ALICE.isSameUuid(editedAlice));

        // uuid has trailing spaces, all other attributes same -> returns false
        String uuidWithTrailingSpaces = VALID_UUID_BOB + " ";
        Person editedBob = new PersonBuilder(BOB).withUuid(uuidWithTrailingSpaces).build();
        assertFalse(BOB.isSameUuid(editedBob));
    }
    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSameName(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameName(null));

        // same name, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSameName(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSameName(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSameName(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSameName(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }
}
