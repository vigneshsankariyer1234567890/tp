package teletubbies.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import teletubbies.model.Model;
import teletubbies.model.ModelManager;
import teletubbies.model.UserPrefs;
import teletubbies.model.person.PersonHasTagsPredicate;
import teletubbies.model.tag.Tag;
import teletubbies.testutil.TypicalPersons;

class FilterCommandTest {

    @Test
    void execute_multipleTags_success() {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());

        FilterCommand command = new FilterCommand(
            new PersonHasTagsPredicate(new HashSet<>(
                    List.of(new Tag("owesMoney"),
                            new Tag("friends"))))
        );

        command.execute(model);
        assertEquals(model.getFilteredPersonList().size(), 1);
    }

    @Test
    void execute_singleTag_success() {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());

        FilterCommand command = new FilterCommand(
                new PersonHasTagsPredicate(new HashSet<>(
                        List.of(new Tag("friends"))))
        );

        command.execute(model);
        assertEquals(model.getFilteredPersonList().size(), 3);
    }

}
