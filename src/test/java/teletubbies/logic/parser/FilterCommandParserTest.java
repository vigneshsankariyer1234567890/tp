package teletubbies.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import teletubbies.logic.commands.FilterCommand;
import teletubbies.logic.parser.exceptions.ParseException;
import teletubbies.model.person.PersonHasTagsPredicate;
import teletubbies.model.tag.Tag;

class FilterCommandParserTest {

    @Test
    void parse_singleTag_success() throws ParseException {
        Set<Tag> tags = new HashSet<>(List.of(
                new Tag("friends")
        ));
        PersonHasTagsPredicate predicate = new PersonHasTagsPredicate(tags);
        FilterCommand expectedCommand = new FilterCommand(predicate);
        String userInput = " -t friends";

        assertEquals(new FilterCommandParser().parse(userInput), expectedCommand);
    }

    @Test
    void parse_multipleTag_success() throws ParseException {
        Set<Tag> tags = new HashSet<>(List.of(
                new Tag("friends"),
                new Tag("owesMoney")
        ));
        PersonHasTagsPredicate predicate = new PersonHasTagsPredicate(tags);
        FilterCommand expectedCommand = new FilterCommand(predicate);
        String userInput = " -t friends -t owesMoney";

        assertEquals(new FilterCommandParser().parse(userInput), expectedCommand);
    }

    @Test
    void parse_reservedTagName_failure() {
        FilterCommandParser parser = new FilterCommandParser();
        String userInput = " -t CompletionStatus";

        CommandParserTestUtil.assertParseFailure(parser, userInput, Tag.MESSAGE_CONSTRAINTS);
    }

}
