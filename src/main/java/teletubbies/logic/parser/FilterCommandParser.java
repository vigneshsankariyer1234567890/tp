package teletubbies.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Set;

import teletubbies.logic.commands.FilterCommand;
import teletubbies.logic.parser.exceptions.ParseException;
import teletubbies.model.person.PersonHasTagsPredicate;
import teletubbies.model.tag.Tag;

/**
 * Parses input arguments and creates a new ExportCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ExportCommand
     * and returns a ExportCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                CliSyntax.PREFIX_TAG);

        Collection<String> tagValues = argMultimap.getAllValues(CliSyntax.PREFIX_TAG).getValues();
        Set<Tag> tagStrings = ParserUtil.parseTagsWithValue(tagValues);

        return new FilterCommand(new PersonHasTagsPredicate(tagStrings));
    }

}
