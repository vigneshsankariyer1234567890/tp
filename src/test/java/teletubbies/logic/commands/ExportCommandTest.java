package teletubbies.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static teletubbies.testutil.Assert.assertThrows;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.AddressBook;
import teletubbies.model.Model;
import teletubbies.model.ModelManager;
import teletubbies.model.UserPrefs;
import teletubbies.model.tag.Tag;
import teletubbies.testutil.Assert;
import teletubbies.testutil.TypicalPersons;


public class ExportCommandTest {

    private Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());

    @Test
    public void filteredAddress_noTags_equalsOriginal() {
        // Set-up
        Set<Tag> tags = new HashSet<>();
        ExportCommand exportCommand = new ExportCommand(tags);

        assertEquals(exportCommand.filteredAddressBook(model), TypicalPersons.getTypicalAddressBook());
    }

    @Test
    public void filteredAddress_friendsTag_lengthEqualsTwo() {
        // Set-up
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friends"));
        ExportCommand exportCommand = new ExportCommand(tags);

        assertEquals(exportCommand.filteredAddressBook(model).getPersonList().size(), 3);
    }

    @Test
    public void filteredAddress_friendsAndOwesMoneyTag_lengthEqualsOne() {
        // Set-up
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friends"));
        tags.add(new Tag("owesMoney"));
        ExportCommand exportCommand = new ExportCommand(tags);

        assertEquals(exportCommand.filteredAddressBook(model).getPersonList().size(), 1);
    }

    @Test
    public void saveAddressBookToPath_emptyPath_throwsException() {
        // Set-up
        Set<Tag> tags = new HashSet<>();
        ExportCommand exportCommand = new ExportCommand(tags);

        AddressBook original = TypicalPersons.getTypicalAddressBook();
        Assert.assertThrows(CommandException.class, () -> exportCommand.saveAddressBookToPath(original, ""));
    }

    @Test
    public void includeDotWithJson_addJson_true() {
        // Set-up
        Set<Tag> tags = new HashSet<>();
        ExportCommand exportCommand = new ExportCommand(tags);

        String s = "hello";
        assertEquals(exportCommand.includeDotJson(s), s + ".json");
    }

    @Test
    public void includeDotWithJson_addJson_false() {
        // Set-up
        Set<Tag> tags = new HashSet<>();
        ExportCommand exportCommand = new ExportCommand(tags);

        String s = "hello.json";
        assertEquals(exportCommand.includeDotJson(s), s);
    }

    @Test
    public void equals() {
        final Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("tag1"));
        tags.add(new Tag("tag2"));
        final ExportCommand standardCommand = new ExportCommand(tags);

        // same object -> returns true
        assertEquals(standardCommand, standardCommand);

        // null -> returns false
        assertNotEquals(null, standardCommand);

        // different types -> returns false
        assertEquals(standardCommand.equals(new ClearCommand()), false);

        final Set<Tag> testTags1 = new HashSet<>();
        testTags1.add(new Tag("tag2"));
        testTags1.add(new Tag("tag1"));

        // same set -> returns true
        assertEquals(standardCommand.equals(new ExportCommand(testTags1)), true);

        final Set<Tag> testTags2 = new HashSet<>();
        testTags2.add(new Tag("tag3"));
        testTags2.add(new Tag("tag4"));

        // different descriptor -> returns false
        assertEquals(standardCommand.equals(new ExportCommand(testTags2)), false);
    }

}
