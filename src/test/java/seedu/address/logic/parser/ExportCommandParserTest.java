package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;


class ExportCommandParserTest {

    private final ExportCommandParser parser = new ExportCommandParser();

    @Test
    void parse_emptyArg_doesNotThrowException() {
        assertDoesNotThrow(() -> parser.parse(""));
    }

    @Test
    void parse_validArgs_returnsExportCommand() throws ParseException {
        System.out.println();
        assertEquals(parser.parse(" t/ 1     2 3"), parser.parse(" t/ 1 2 3"));
    }

    @Test
    void parse_jumbledArg_equal() throws ParseException {
        assertEquals(parser.parse(" t/ 1 2 3"), parser.parse(" t/ 3 2 1"));
    }

    @Test
    void parse_differentArgs_notEqual() throws ParseException {
        assertNotEquals(parser.parse(" t/ 1 1 1"), parser.parse("13"));
    }
}
