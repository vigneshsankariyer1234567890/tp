package teletubbies.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import teletubbies.model.Model;
import teletubbies.model.ModelManager;
import teletubbies.model.UserPrefs;
import teletubbies.model.tag.Tag;
import teletubbies.testutil.TypicalPersons;

public class ExportCommandTest {

    private Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
    private final Optional<String> none = Optional.empty();

    @Test
    public void filteredAddress_noTags_equalsOriginal() {
        // Set-up
        Set<Tag> tags = new HashSet<>();
        ExportCommand exportCommand = new ExportCommand(tags);

        assertEquals(exportCommand.filterPersonList(model.getFilteredPersonList()),
                TypicalPersons.getTypicalPersons());
    }

    @Test
    public void filteredAddress_friendsTag_lengthEqualsThree() {
        // Set-up
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friends"));
        ExportCommand exportCommand = new ExportCommand(tags);

        assertEquals(exportCommand.filterPersonList(model.getFilteredPersonList()).size(), 3);
    }

    @Test
    public void filteredAddress_friendsAndOwesMoneyTag_lengthEqualsOne() {
        // Set-up
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friends"));
        tags.add(new Tag("owesMoney"));
        ExportCommand exportCommand = new ExportCommand(tags);

        assertEquals(exportCommand.filterPersonList(model.getFilteredPersonList()).size(), 1);
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
