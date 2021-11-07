package teletubbies.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static teletubbies.model.VersionedAddressBookTest.makeVersionedAddressBook;
import static teletubbies.testutil.TypicalPersons.AMY;
import static teletubbies.testutil.TypicalPersons.BOB;
import static teletubbies.testutil.TypicalPersons.CARL;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import teletubbies.commons.exceptions.EarliestVersionException;
import teletubbies.commons.exceptions.LatestVersionException;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.Model;
import teletubbies.model.ModelManager;
import teletubbies.model.ReadOnlyAddressBook;
import teletubbies.model.UserPrefs;
import teletubbies.model.VersionedAddressBook;
import teletubbies.model.person.NameContainsKeywordsPredicate;
import teletubbies.model.person.PersonHasTagsPredicate;
import teletubbies.model.tag.Tag;
import teletubbies.testutil.AddressBookBuilder;

public class ModifiableAndUnmodifiableCommandIntegrationTest {

    private final ReadOnlyAddressBook addressBookWithAmyBobAndCarl =
            new AddressBookBuilder().withPerson(AMY).withPerson(BOB).withPerson(CARL).build();
    private final List<ReadOnlyAddressBook> fullStateList = List.of(addressBookWithAmyBobAndCarl);

    @Test
    public void execute_filterCommand_doesNotModifyAddressBook() {
        VersionedAddressBook book = makeVersionAddressBook(fullStateList);
        assertNotNull(book);
        Model model = makeModel(book);

        FilterCommand filterCommand = new FilterCommand(new PersonHasTagsPredicate(Set.of()));
        filterCommand.execute(model);

        Model expectedModel = makeModel(book);
        expectedModel.updateFilteredPersonList(new PersonHasTagsPredicate(Set.of()));

        assertEquals(model, expectedModel);
    }

    @Test
    public void execute_listCommand_doesNotModifyAddressBook() {
        VersionedAddressBook book = makeVersionAddressBook(fullStateList);
        assertNotNull(book);
        Model model = makeModel(book);

        ListCommand listCommand = new ListCommand();
        listCommand.execute(model);

        Model expectedModel = makeModel(book);

        assertEquals(model, expectedModel);
    }

    @Test
    public void execute_exportCommandAndCancel_doesNotModifyAddressBook() throws CommandException {
        VersionedAddressBook book = makeVersionAddressBook(fullStateList);
        assertNotNull(book);
        Model model = makeModel(book);
        Model expectedModel = makeModel(book);

        model.cancelPendingExport();
        assertEquals(model, expectedModel);

        ExportCommand exportCommand = new ExportCommand(Set.of());
        exportCommand.execute(model);
        model.cancelPendingExport();

        assertEquals(model, expectedModel);
    }

    @Test
    public void execute_exportCommandWithTagAndCancel_doesNotModifyAddressBook() throws CommandException {
        VersionedAddressBook book = makeVersionAddressBook(fullStateList);
        assertNotNull(book);
        Model model = makeModel(book);
        Model expectedModel = makeModel(book);

        model.cancelPendingExport();
        assertEquals(model, expectedModel);

        ExportCommand exportCommand = new ExportCommand(Set.of(new Tag("test")));
        exportCommand.execute(model);
        model.cancelPendingExport();

        assertEquals(model, expectedModel);
    }

    @Test
    public void execute_findCommand_doesNotModifyAddressBook() {
        VersionedAddressBook book = makeVersionAddressBook(fullStateList);
        assertNotNull(book);
        Model model = makeModel(book);
        Model expectedModel = makeModel(book);

        FindCommand findCommand = new FindCommand(new NameContainsKeywordsPredicate(List.of()));
        findCommand.execute(model);

        expectedModel.updateFilteredPersonList(new NameContainsKeywordsPredicate(List.of()));

        assertEquals(model, expectedModel);
    }

    @Test
    public void execute_clearCommand_modifiesAddressBook() throws CommandException {
        VersionedAddressBook book = makeVersionAddressBook(fullStateList);
        assertNotNull(book);
        Model model = makeModel(book);
        Model expectedModel = makeModel(book);

        ClearCommand clearCommand = new ClearCommand();
        clearCommand.execute(model);

        assertNotEquals(model, expectedModel);

        model.cancelPendingExport();

        assertNotEquals(model, expectedModel);
    }

    @Test
    public void execute_clearThenExportAndCancel_addsOneMoreState()
            throws EarliestVersionException, LatestVersionException, CommandException {
        VersionedAddressBook book = makeVersionAddressBook(fullStateList);
        assertNotNull(book);
        Model model = makeModel(book);

        ClearCommand clearCommand = new ClearCommand();
        clearCommand.execute(model);

        int undoCount = 0;
        while (model.canUndoAddressBook()) {
            model.undoAddressBook();
            undoCount++;
        }

        assertEquals(undoCount, fullStateList.size());

        while (model.canRedoAddressBook()) {
            model.redoAddressBook();
        }

        ExportCommand exportCommand = new ExportCommand(Set.of(new Tag("friends")));
        exportCommand.execute(model);
        model.cancelPendingExport();

        int undoAgainCount = 0;

        while (model.canUndoAddressBook()) {
            model.undoAddressBook();
            undoAgainCount++;
        }

        assertEquals(undoAgainCount, undoCount);
    }

    private VersionedAddressBook makeVersionAddressBook(List<ReadOnlyAddressBook> addressBooks) {
        assertFalse(addressBooks.isEmpty());
        VersionedAddressBook book = null;
        try {
            book = makeVersionedAddressBook(addressBooks);
        } catch (VersionedAddressBook.EmptyAddressBookStateListException ignored) {
            throw new AssertionError();
        }
        return book;
    }

    private Model makeModel(VersionedAddressBook versionedAddressBook) {
        return new ModelManager(versionedAddressBook, new UserPrefs());
    }

}
