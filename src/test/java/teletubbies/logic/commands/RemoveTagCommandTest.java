package teletubbies.logic.commands;

import static teletubbies.testutil.TypicalPersons.BENSON;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import teletubbies.commons.core.Range;
import teletubbies.commons.core.UserProfile;
import teletubbies.model.AddressBook;
import teletubbies.model.Model;
import teletubbies.model.ModelManager;
import teletubbies.model.UserPrefs;
import teletubbies.model.person.Person;
import teletubbies.model.tag.Tag;
import teletubbies.testutil.TypicalPersons;

class RemoveTagCommandTest {
    private UserProfile supervisorProfile = new UserProfile("John", UserProfile.Role.SUPERVISOR);
    // TODO test telemarketer failure
    private UserPrefs userPrefs = new UserPrefs();

    @Test
    void execute_supervisor_success() {
        userPrefs.setUserProfile(supervisorProfile);
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), userPrefs);

        RemoveTagCommand command = new RemoveTagCommand(
                new Range(new HashSet<>(List.of(1, 2, 3, 4, 5, 6, 7))),
                "owesMoney"
        );

        Set<Tag> newTags = new HashSet<>(BENSON.getTags());
        newTags.remove(new Tag("owesMoney"));
        Person newBenson = new Person(BENSON.getName(), BENSON.getPhone(), BENSON.getEmail(), BENSON.getAddress(),
                BENSON.getCompletionStatus(), newTags);
        AddressBook ab = TypicalPersons.getTypicalAddressBook();
        ab.setPerson(BENSON, newBenson);
        Model testModel = new ModelManager(ab, userPrefs);

        CommandTestUtil.assertCommandSuccess(command, model,
                new CommandResult(RemoveTagCommand.MESSAGE_COMPLETED_SUCCESS), testModel);
    }

}
