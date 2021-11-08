package teletubbies.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static teletubbies.logic.commands.CommandTestUtil.assertCommandFailure;
import static teletubbies.testutil.TypicalPersons.ALICE;
import static teletubbies.testutil.TypicalPersons.BENSON;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import teletubbies.commons.core.Range;
import teletubbies.commons.core.UserProfile;
import teletubbies.commons.exceptions.UserRoleSetException;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.Model;
import teletubbies.model.ModelManager;
import teletubbies.model.UserPrefs;
import teletubbies.model.person.Person;
import teletubbies.model.tag.Tag;
import teletubbies.model.tag.TagUtils;
import teletubbies.testutil.TypicalPersons;

class RemoveTagCommandTest {
    private UserProfile supervisorProfile = new UserProfile("John", UserProfile.Role.SUPERVISOR);
    private UserProfile telemarketerProfile = new UserProfile("John", UserProfile.Role.TELEMARKETER);


    @Test
    void execute_supervisor_success() throws CommandException, UserRoleSetException {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setUserProfile(supervisorProfile);
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), userPrefs);

        RemoveTagCommand command = new RemoveTagCommand(
                new Range(new HashSet<>(List.of(1, 2, 3, 4, 5, 6, 7))),
                "owesMoney"
        );
        command.execute(model);

        Set<Tag> newTags = new HashSet<>(BENSON.getTags());
        newTags.remove(new Tag("owesMoney"));

        Person correctBenson = new Person(BENSON.getUuid(), BENSON.getName(), BENSON.getPhone(), BENSON.getEmail(),
                BENSON.getAddress(), BENSON.getCompletionStatus(), BENSON.getRemark(), newTags);
        Person newBenson = model.getFilteredPersonList().get(1);

        assertEquals(newBenson, correctBenson);
    }

    @Test
    void execute_telemarketer_failure() throws CommandException, UserRoleSetException {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setUserProfile(telemarketerProfile);

        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), userPrefs);
        Person editedPerson = new Person(ALICE.getUuid(), ALICE.getName(), ALICE.getPhone(),
                ALICE.getEmail(), ALICE.getAddress(), ALICE.getCompletionStatus(), ALICE.getRemark(),
                new HashSet<>(List.of(new Tag("friends", "", true))));
        model.setPerson(ALICE, editedPerson);

        RemoveTagCommand command = new RemoveTagCommand(
                new Range(new HashSet<>(List.of(1))),
                "friends"
        );

        assertCommandFailure(command, model, TagUtils.noPermissionsMessage("friends"));
    }

}
