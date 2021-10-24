package teletubbies.logic.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import teletubbies.logic.parser.exceptions.ParseException;


class ExportCommandParserTest {

    private final ExportCommandParser parser = new ExportCommandParser();

    @Test
    void parse_emptyArg_doesNotThrowException() {
        assertDoesNotThrow(() -> parser.parse(""));
    }

    @Test
    void parse_validArgs_returnsExportCommand() throws ParseException {
        Assertions.assertEquals(parser.parse(" t/ 1     2 3"), parser.parse(" t/ 1 2 3"));
    }

    @Test
    void parse_jumbledArg_equal() throws ParseException {
        Assertions.assertEquals(parser.parse(" t/ 1 2 3"), parser.parse(" t/ 3 2 1"));
    }

    @Test
    void parse_differentArgs_notEqual() throws ParseException {
        Assertions.assertNotEquals(parser.parse(" --t 1 1 1"), parser.parse("13"));
    }
}
