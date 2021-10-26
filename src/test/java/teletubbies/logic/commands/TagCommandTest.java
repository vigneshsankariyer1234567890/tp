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

class TagCommandTest {

    private UserProfile supervisorProfile = new UserProfile("John", UserProfile.Role.SUPERVISOR);
    private UserPrefs userPrefs = new UserPrefs();

    @Test
    void execute_supervisor_success() {
        userPrefs.setUserProfile(supervisorProfile);
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), userPrefs);

        TagCommand command = new TagCommand(
                new Range(new HashSet<>(List.of(2))),
                "important",
                "",
                false
        );

        Set<Tag> newTags = new HashSet<>(BENSON.getTags());
        newTags.add(new Tag("important"));
        Person newBenson = new Person(BENSON.getName(), BENSON.getPhone(), BENSON.getEmail(), BENSON.getAddress(),
                BENSON.getCompletionStatus(), newTags);

        AddressBook ab = TypicalPersons.getTypicalAddressBook();
        ab.setPerson(BENSON, newBenson);

        Model testModel = new ModelManager(ab, userPrefs);

        CommandTestUtil.assertCommandSuccess(command, model,
                new CommandResult(TagCommand.MESSAGE_COMPLETED_SUCCESS), testModel);
    }

}
