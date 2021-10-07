package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DoneCommand;

public class DoneCommandParserTest {
    private DoneCommandParser parser = new DoneCommandParser();

    @Test
    public void parse_indexSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = String.valueOf(targetIndex.getOneBased());
        DoneCommand expectedCommand = new DoneCommand(INDEX_FIRST_PERSON);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
