package teletubbies.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;

import teletubbies.logic.commands.ExportCommand;
import teletubbies.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ExportCommand object
 */
public class ExportCommandParser implements Parser<ExportCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ExportCommand
     * and returns a ExportCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ExportCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                CliSyntax.PREFIX_TAG);
        Collection<String> tagValues = argMultimap.getAllValues(CliSyntax.PREFIX_TAG).getValues();

        return new ExportCommand(ParserUtil.parseTagsWithValue(tagValues));
    }

}
