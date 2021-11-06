package teletubbies.logic.parser;

import static java.util.Objects.requireNonNull;
import static teletubbies.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.stream.Stream;

import teletubbies.commons.core.Messages;
import teletubbies.commons.core.Range;
import teletubbies.logic.commands.RemoveTagCommand;
import teletubbies.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code TagRemoveCommand} object
 */
public class RemoveTagCommandParser implements Parser<RemoveTagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code TagRemoveCommand}
     * and returns a {@code TagRemoveCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public RemoveTagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME);
        String preambleString = argumentMultimap.getPreamble();

        if (!arePrefixesPresent(argumentMultimap, PREFIX_NAME) || preambleString.isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    RemoveTagCommand.MESSAGE_USAGE));
        }

        String tagName = argumentMultimap.getValue(PREFIX_NAME).get();
        Range personRange = ParserUtil.parseRange(preambleString);

        return new RemoveTagCommand(personRange, tagName);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
