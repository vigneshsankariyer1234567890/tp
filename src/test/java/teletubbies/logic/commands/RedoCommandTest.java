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

    // @@author: sijie123
    // Reused from
    // https://github.com/se-edu/addressbook-level4/blob/master/src/test/java/seedu/address/logic/commands
    // RedoCommandTest.java
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final int numberOfDeletes = 5;

    @BeforeEach
    public void setUp() throws EarliestVersionException {
        // set up of model's' undo/redo history
        deleteFirstPerson(model);
        deleteFirstPerson(model);
        model.undoAddressBook();
        model.undoAddressBook();

        deleteFirstPerson(expectedModel);
        deleteFirstPerson(expectedModel);
        expectedModel.undoAddressBook();
        expectedModel.undoAddressBook();

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
