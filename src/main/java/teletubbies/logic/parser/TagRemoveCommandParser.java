package teletubbies.logic.parser;

import static java.util.Objects.requireNonNull;
import static teletubbies.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.Optional;

import teletubbies.commons.core.Messages;
import teletubbies.commons.core.Range;
import teletubbies.logic.commands.TagRemoveCommand;
import teletubbies.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code TagRemoveCommand} object
 */
public class TagRemoveCommandParser implements Parser<TagRemoveCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code TagRemoveCommand}
     * and returns a {@code TagRemoveCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public TagRemoveCommand parse(String args) throws ParseException {
        requireNonNull(args);

        try {
            ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME);

            String preambleString = argumentMultimap.getPreamble();
            Range personRange = preambleString.contains("-")
                    ? ParserUtil.parseRange(preambleString)
                    : ParserUtil.parseRangeSeparatedByCommas(preambleString);

            Optional<String> optTagName = argumentMultimap.getValue(PREFIX_NAME);
            if (optTagName.isEmpty()) {
                throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        TagRemoveCommand.MESSAGE_USAGE));
            }

            return new TagRemoveCommand(personRange, optTagName.get());
        } catch (ParseException parseException) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, TagRemoveCommand.MESSAGE_USAGE),
                    parseException
            );
        }

    }
}
