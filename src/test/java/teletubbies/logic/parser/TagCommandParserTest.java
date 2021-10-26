package teletubbies.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import teletubbies.commons.core.Messages;
import teletubbies.commons.core.Range;
import teletubbies.logic.commands.TagCommand;
import teletubbies.logic.parser.exceptions.ParseException;

class TagCommandParserTest {

    private TagCommandParser parser = new TagCommandParser();
    private Range testRange = new Range(new HashSet<>(List.of(1, 2, 3)));

    @Test
    public void parse_validHyphenRange_success() throws ParseException {
        TagCommand expectedCommand = new TagCommand(testRange, "Test", "", false);

        assertEquals(new TagCommandParser().parse("1-3 -n Test"),
                expectedCommand);
    }

    @Test
    public void parse_validCommaRange_success() throws ParseException {
        TagCommand expectedCommand = new TagCommand(testRange, "Test", "", false);

        assertEquals(new TagCommandParser().parse("1,2,3 -n Test"),
                expectedCommand);
    }

    @Test
    public void parse_missingRange_failure() {
        String userInput = " -n Test";

        CommandParserTestUtil.assertParseFailure(parser, userInput,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingName_failure() {
        String userInput = " 5-10";

        CommandParserTestUtil.assertParseFailure(parser, userInput,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }
}
