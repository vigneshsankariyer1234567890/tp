package teletubbies.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static teletubbies.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.Model;
import teletubbies.model.ModelManager;
import teletubbies.model.UserPrefs;


/**
 * An integration test to assess if ModelManager can be fully integrated. Invalid inputs not added as they will fail
 * according to ModelManager tests.
 */
public class HistoryCommandIntegrationTest {
    private final List<String> validCommandInputs = CommandTestUtil.VALID_COMMAND_HISTORY_INPUTS;

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void modelManagerIntegrationTest_validInputs_success() throws CommandException {
        for (String s: validCommandInputs) {
            model.addCommandInput(s);
        }
        HistoryCommand historyCommand = new HistoryCommand();
        CommandResult commandResult = historyCommand.execute(model);

        List<String> copyOfInputs = new ArrayList<>(validCommandInputs);
        Collections.reverse(copyOfInputs);
        String expectedResult = String.format(HistoryCommand.MESSAGE_SUCCESS, String.join("\n", copyOfInputs));

        assertEquals(expectedResult, commandResult.getFeedbackToUser());
    }

}
