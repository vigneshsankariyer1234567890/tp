package teletubbies.logic.commands;

import static teletubbies.logic.commands.CommandTestUtil.assertCommandFailure;
import static teletubbies.logic.commands.CommandTestUtil.assertCommandSuccess;
import static teletubbies.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import teletubbies.commons.exceptions.EarliestVersionException;
import teletubbies.model.Model;
import teletubbies.model.ModelManager;
import teletubbies.model.UserPrefs;
import teletubbies.model.person.Person;

public class UndoCommandTest {
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final int numberOfDeletes = 5;

    @BeforeEach
    public void setUp() {
        for (int i = 0; i < numberOfDeletes; i++) {
            deleteFirstPerson(model);
            deleteFirstPerson(expectedModel);
        }
    }

    @Test
    public void execute() throws EarliestVersionException {

        for (int i = 0; i < numberOfDeletes; i++) {
            expectedModel.undoAddressBook();
            assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS, expectedModel);
        }

        assertCommandFailure(new UndoCommand(), model, UndoCommand.MESSAGE_FAILURE);
    }


    public static void deleteFirstPerson(Model model) {
        Person firstPerson = model.getFilteredPersonList().get(0);
        model.deletePerson(firstPerson);
        model.commitAddressBook();
    }
}
