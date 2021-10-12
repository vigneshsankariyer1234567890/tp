package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DoneCommand;

public class DoneCommandParserTest {
    private DoneCommandParser parser = new DoneCommandParser();

    @Test
    public void parse_indexSpecified_success() {
        String userInput = String.valueOf(INDEX_FIRST_PERSON.getOneBased());
        DoneCommand expectedCommand = new DoneCommand(INDEX_FIRST_PERSON);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
