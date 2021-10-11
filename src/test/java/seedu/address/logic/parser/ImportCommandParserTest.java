package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_NO_IMPORT_FILE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ImportCommand;


class ImportCommandParserTest {
    private final ImportCommandParser parser = new ImportCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, " ", String.format(MESSAGE_NO_IMPORT_FILE, ImportCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        ImportCommand expectedImportCommand =
                new ImportCommand("./data/addressbook_importTest.json");
        assertParseSuccess(parser, "./data/addressbook_importTest.json", expectedImportCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " ./data/addressbook_importTest.json", expectedImportCommand);
    }
}
