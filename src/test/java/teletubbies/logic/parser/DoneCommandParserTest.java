package teletubbies.logic.parser;

import org.junit.jupiter.api.Test;

import teletubbies.logic.commands.DoneCommand;
import teletubbies.testutil.TypicalIndexes;

public class DoneCommandParserTest {
    private DoneCommandParser parser = new DoneCommandParser();

    @Test
    public void parse_indexSpecified_success() {
        String userInput = String.valueOf(TypicalIndexes.INDEX_FIRST_PERSON.getOneBased());
        DoneCommand expectedCommand = new DoneCommand(TypicalIndexes.INDEX_FIRST_PERSON);
        CommandParserTestUtil.assertParseSuccess(parser, userInput, expectedCommand);
    }
}
