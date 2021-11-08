package teletubbies.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static teletubbies.testutil.TypicalPersons.BENSON;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import teletubbies.commons.core.Range;
import teletubbies.commons.exceptions.UserRoleSetException;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.Model;
import teletubbies.model.ModelManager;
import teletubbies.model.UserPrefs;
import teletubbies.model.person.Person;
import teletubbies.model.tag.Tag;
import teletubbies.testutil.TypicalPersons;

class TagCommandTest {

    private UserPrefs userPrefs = new UserPrefs();

    @Test
    void execute_supervisor_success() throws CommandException, UserRoleSetException {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), userPrefs);

        TagCommand command = new TagCommand(
                new Range(new HashSet<>(List.of(2))),
                "important",
                "",
                false
        );

        command.execute(model);

        Set<Tag> newTags = new HashSet<>(BENSON.getTags());
        newTags.add(new Tag("important"));
        Person correctBenson = new Person(BENSON.getUuid(), BENSON.getName(), BENSON.getPhone(), BENSON.getEmail(),
                BENSON.getAddress(), BENSON.getCompletionStatus(), BENSON.getRemark(), newTags);

        Person newBenson = model.getFilteredPersonList().get(1);

        assertEquals(newBenson, correctBenson);
    }

}
