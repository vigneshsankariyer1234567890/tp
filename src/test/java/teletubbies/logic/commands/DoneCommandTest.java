package teletubbies.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static teletubbies.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import teletubbies.commons.core.Messages;
import teletubbies.commons.core.index.Index;
import teletubbies.model.AddressBook;
import teletubbies.model.Model;
import teletubbies.model.ModelManager;
import teletubbies.model.UserPrefs;
import teletubbies.model.person.Person;
import teletubbies.model.tag.CompletionStatusTag.CompletionStatus;
import teletubbies.testutil.PersonBuilder;
import teletubbies.testutil.TypicalIndexes;
import teletubbies.testutil.TypicalPersons;

/**
 * Contains integration tests (interaction with the Model) and unit tests for DoneCommand.
 */
public class DoneCommandTest {

    private Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_completedUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withCompletionStatus(CompletionStatus.COMPLETE).build();

        DoneCommand doneCommand = new DoneCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DoneCommand.MESSAGE_COMPLETED_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        CommandTestUtil.assertCommandSuccess(doneCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        CommandTestUtil.showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withCompletionStatus(CompletionStatus.COMPLETE).build();

        DoneCommand doneCommand = new DoneCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DoneCommand.MESSAGE_COMPLETED_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        CommandTestUtil.assertCommandSuccess(doneCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DoneCommand remarkCommand = new DoneCommand(outOfBoundIndex);

        CommandTestUtil.assertCommandFailure(remarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        CommandTestUtil.showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = TypicalIndexes.INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DoneCommand doneCommand = new DoneCommand(outOfBoundIndex);
        CommandTestUtil.assertCommandFailure(doneCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final DoneCommand standardCommand = new DoneCommand(INDEX_FIRST_PERSON);

        // same values -> returns true
        DoneCommand commandWithSameValues = new DoneCommand(INDEX_FIRST_PERSON);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new DoneCommand(TypicalIndexes.INDEX_SECOND_PERSON)));
    }
}
