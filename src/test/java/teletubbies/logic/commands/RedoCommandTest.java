package teletubbies.logic.commands;

import static teletubbies.logic.commands.CommandTestUtil.assertCommandFailure;
import static teletubbies.logic.commands.CommandTestUtil.assertCommandSuccess;
import static teletubbies.logic.commands.UndoCommandTest.deleteFirstPerson;
import static teletubbies.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import teletubbies.commons.exceptions.EarliestVersionException;
import teletubbies.commons.exceptions.LatestVersionException;
import teletubbies.model.Model;
import teletubbies.model.ModelManager;
import teletubbies.model.UserPrefs;

public class RedoCommandTest {
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final int numberOfDeletes = 5;

    @BeforeEach
    public void setUp() throws EarliestVersionException {
        for (int i = 0; i < numberOfDeletes; i++) {
            deleteFirstPerson(model);
            deleteFirstPerson(expectedModel);
        }

        for (int i = 0; i < numberOfDeletes; i++) {
            model.undoAddressBook();
            expectedModel.undoAddressBook();
        }
    }

    @Test
    public void execute() throws LatestVersionException {
        for (int i = 0; i < numberOfDeletes; i++) {
            expectedModel.redoAddressBook();
            assertCommandSuccess(new RedoCommand(), model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
        }

        assertCommandFailure(new RedoCommand(), model, RedoCommand.MESSAGE_FAILURE);
    }

}
