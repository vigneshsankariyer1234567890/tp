package teletubbies.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static teletubbies.logic.commands.CommandTestUtil.assertCommandFailure;
import static teletubbies.logic.commands.CommandTestUtil.assertCommandSuccess;
import static teletubbies.logic.commands.CommandTestUtil.showPersonAtIndex;
import static teletubbies.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static teletubbies.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static teletubbies.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import teletubbies.commons.core.Messages;
import teletubbies.commons.core.index.Index;
import teletubbies.model.Model;
import teletubbies.model.ModelManager;
import teletubbies.model.UserPrefs;
import teletubbies.model.person.Person;
import teletubbies.model.person.Phone;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validPhoneNumberUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        DeleteCommand deleteCommand = new DeleteCommand(personToDelete.getPhone());
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, personToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        expectedModel.commitAddressBook();

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPhoneNumberUnfilteredList_throwsCommandException() {
        DeleteCommand deleteCommand = new DeleteCommand(new Phone("000"));

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_PHONE_NUMBER);
    }

    @Test
    public void execute_validPhoneNumberFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        DeleteCommand deleteCommand = new DeleteCommand(personToDelete.getPhone());

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, personToDelete);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        expectedModel.commitAddressBook();
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        expectedModel.commitAddressBook();

        // delete person first from model
        deleteCommand.execute(model);

        // check that undo reverts to previous state
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // check that redo reverts to next available state
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        // address book state should not be added into model
        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // cannot undo and redo as only one left.
        assertCommandFailure(new UndoCommand(), model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Deletes a {@code Person} from a filtered list.
     * 2. Undo the deletion.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously deleted person in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the deletion. This ensures {@code RedoCommand} deletes the person object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_samePersonDeleted() throws Exception {
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        expectedModel.deletePerson(personToDelete);
        expectedModel.commitAddressBook();

        // delete -> deletes second person in unfiltered person list / first person in filtered person list
        deleteCommand.execute(model);

        // undo -> reverts addressbook back to previous state and filtered person list to show all persons
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(personToDelete, model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()));
        // redo -> deletes same second person in unfiltered person list
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        Person firstPersonToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPersonToDelete = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        // delete by index
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_PERSON);

        // delete by phone number
        DeleteCommand deleteThirdCommand = new DeleteCommand(firstPersonToDelete.getPhone());
        DeleteCommand deleteForthCommand = new DeleteCommand(secondPersonToDelete.getPhone());

        // test cases for deleting via phone number
        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));

        // test cases for deleting via index
        // same object -> returns true
        assertTrue(deleteThirdCommand.equals(deleteThirdCommand));

        // same values -> returns true
        DeleteCommand deleteThirdCommandCopy = new DeleteCommand(firstPersonToDelete.getPhone());
        assertTrue(deleteThirdCommand.equals(deleteThirdCommandCopy));

        // different types -> returns false
        assertFalse(deleteThirdCommand.equals(1));

        // null -> returns false
        assertFalse(deleteThirdCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteThirdCommand.equals(deleteForthCommand));
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, personToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        expectedModel.commitAddressBook();
        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, personToDelete);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        expectedModel.commitAddressBook();
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
