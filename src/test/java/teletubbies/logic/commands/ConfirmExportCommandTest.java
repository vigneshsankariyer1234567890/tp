package teletubbies.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static teletubbies.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import teletubbies.commons.core.Messages;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.logic.commands.uieffects.ExportUiConsumer;
import teletubbies.model.AddressBook;
import teletubbies.model.Model;
import teletubbies.model.ModelManager;
import teletubbies.model.UserPrefs;
import teletubbies.testutil.TypicalPersons;

class ConfirmExportCommandTest {
    private Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
    private ConfirmExportCommand confirmExportCommand = new ConfirmExportCommand();
    private ExportUiConsumer exportUiConsumer = new ExportUiConsumer(model);

    @Test
    public void execute_noPendingExport_throwsException() {
        CommandTestUtil.assertCommandFailure(confirmExportCommand, model, Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void saveAddressBookToPath_emptyPath_throwsException() {
        AddressBook original = TypicalPersons.getTypicalAddressBook();
        assertThrows(CommandException.class, () -> exportUiConsumer.saveAddressBookToPath(original, ""));
    }

    @Test
    public void includeDotWithJson_addJson_true() {
        String s = "hello";
        assertEquals(exportUiConsumer.includeDotJson(s), s + ".json");
    }

    @Test
    public void includeDotWithJson_addJson_false() {
        String s = "hello.json";
        assertEquals(exportUiConsumer.includeDotJson(s), s);
    }

    @Test
    public void equals() {
        final ConfirmExportCommand confirmExportFirstCommand = new ConfirmExportCommand();
        final ConfirmExportCommand confirmExportSecondCommand = new ConfirmExportCommand();

        // same object -> returns true
        assertTrue(confirmExportFirstCommand.equals(confirmExportFirstCommand));

        // same values -> returns true
        assertTrue(confirmExportFirstCommand.equals(confirmExportSecondCommand));

        // different types -> returns false
        assertFalse(confirmExportFirstCommand.equals(1));

        // null -> returns false
        assertFalse(confirmExportFirstCommand.equals(null));
    }
}
