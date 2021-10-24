package teletubbies.logic.commands;

import org.junit.jupiter.api.Test;
import teletubbies.commons.core.Messages;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.AddressBook;
import teletubbies.model.Model;
import teletubbies.model.ModelManager;
import teletubbies.model.UserPrefs;
import teletubbies.model.tag.Tag;
import teletubbies.testutil.Assert;
import teletubbies.testutil.TypicalPersons;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ConfirmExportCommandTest {
    private Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
    ConfirmExportCommand confirmExportCommand = new ConfirmExportCommand();

    @Test
    public void execute_noPendingExport_throwsException() {
        CommandTestUtil.assertCommandFailure(confirmExportCommand, model, Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void saveAddressBookToPath_emptyPath_throwsException() {
        AddressBook original = TypicalPersons.getTypicalAddressBook();
        Assert.assertThrows(CommandException.class, () -> confirmExportCommand.saveAddressBookToPath(original, ""));
    }

    @Test
    public void includeDotWithJson_addJson_true() {
        String s = "hello";
        assertEquals(confirmExportCommand.includeDotJson(s), s + ".json");
    }

    @Test
    public void includeDotWithJson_addJson_false() {
        String s = "hello.json";
        assertEquals(confirmExportCommand.includeDotJson(s), s);
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
