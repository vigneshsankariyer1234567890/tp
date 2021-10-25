package teletubbies.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import javafx.util.Pair;
import teletubbies.model.Model;
import teletubbies.model.ModelManager;
import teletubbies.model.UserPrefs;
import teletubbies.testutil.TypicalPersons;

public class ExportCommandTest {

    private Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
    private final Optional<String> none = Optional.empty();

    @Test
    public void filteredAddress_noTags_equalsOriginal() {
        // Set-up
        Set<Pair<String, Optional<String>>> tags = new HashSet<>();
        ExportCommand exportCommand = new ExportCommand(tags);

        assertEquals(exportCommand.filteredPersonList(model), TypicalPersons.getTypicalPersons());
    }

    @Test
    public void filteredAddress_friendsTag_lengthEqualsTwo() {
        // Set-up
        Set<Pair<String, Optional<String>>> tags = new HashSet<>();
        tags.add(new Pair<>("friends", none));
        ExportCommand exportCommand = new ExportCommand(tags);

        assertEquals(exportCommand.filteredPersonList(model).size(), 3);
    }

    @Test
    public void filteredAddress_friendsAndOwesMoneyTag_lengthEqualsOne() {
        // Set-up
        Set<Pair<String, Optional<String>>> tags = new HashSet<>();
        tags.add(new Pair<>("friends", none));
        tags.add(new Pair<>("owesMoney", none));
        ExportCommand exportCommand = new ExportCommand(tags);

        assertEquals(exportCommand.filteredPersonList(model).size(), 1);
    }

    @Test
    public void equals() {
        final Set<Pair<String, Optional<String>>> tags = new HashSet<>();
        tags.add(new Pair<>("tag1", none));
        tags.add(new Pair<>("tag2", none));
        final ExportCommand standardCommand = new ExportCommand(tags);

        // same object -> returns true
        assertEquals(standardCommand, standardCommand);

        // null -> returns false
        assertNotEquals(null, standardCommand);

        // different types -> returns false
        assertEquals(standardCommand.equals(new ClearCommand()), false);

        final Set<Pair<String, Optional<String>>> testTags1 = new HashSet<>();
        testTags1.add(new Pair<>("tag2", none));
        testTags1.add(new Pair<>("tag1", none));

        // same set -> returns true
        assertEquals(standardCommand.equals(new ExportCommand(testTags1)), true);

        final Set<Pair<String, Optional<String>>> testTags2 = new HashSet<>();
        testTags2.add(new Pair<>("tag3", none));
        testTags2.add(new Pair<>("tag4", none));

        // different descriptor -> returns false
        assertEquals(standardCommand.equals(new ExportCommand(testTags2)), false);
    }

}
