package teletubbies.logic.parser;

import teletubbies.commons.core.Messages;
import teletubbies.commons.core.Range;
import teletubbies.logic.commands.TagCommand;
import teletubbies.logic.parser.exceptions.ParseException;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static teletubbies.logic.parser.CliSyntax.PREFIX_NAME;
import static teletubbies.logic.parser.CliSyntax.PREFIX_SUPERVISOR_FLAG;
import static teletubbies.logic.parser.CliSyntax.PREFIX_VALUE;

/**
 * Parses input arguments and creates a new {@code TagCommand} object
 */
public class TagCommandParser implements Parser<TagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code TagCommand}
     * and returns a {@code TagCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public TagCommand parse(String args) throws ParseException {
        requireNonNull(args);

        try {
            ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME,
                    PREFIX_VALUE, PREFIX_SUPERVISOR_FLAG);
            String preambleString = argumentMultimap.getPreamble();
            Range personRange = preambleString.contains("-")
                    ? ParserUtil.parseRange(preambleString)
                    : ParserUtil.parseRangeSeparatedByCommas(preambleString);
            boolean supervisorFlag = argumentMultimap.getAllValues(PREFIX_SUPERVISOR_FLAG).isPresent();
            Optional<String> optTagName = argumentMultimap.getValue(PREFIX_NAME);
            if (optTagName.isEmpty()) {
                throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        TagCommand.MESSAGE_USAGE));
            }
            Optional<String> optTagValue = argumentMultimap.getValue(PREFIX_VALUE);
            String tagValue = optTagValue.orElse("");

            return new TagCommand(personRange, optTagName.get(), tagValue, supervisorFlag);
        } catch (ParseException parseException) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE),
                    parseException
            );
        }

    }
}
