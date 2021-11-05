package teletubbies.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static teletubbies.testutil.TypicalPersons.BENSON;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import teletubbies.commons.core.Range;
import teletubbies.commons.core.UserProfile;
import teletubbies.commons.exceptions.IllegalValueException;
import teletubbies.commons.exceptions.UserRoleSetException;
import teletubbies.logic.commands.exceptions.CommandException;
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
    void execute_supervisor_success() throws CommandException {
        // userPrefs.setUserProfile(supervisorProfile);
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

}
